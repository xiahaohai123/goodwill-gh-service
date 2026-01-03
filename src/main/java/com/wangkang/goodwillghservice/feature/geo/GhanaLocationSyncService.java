package com.wangkang.goodwillghservice.feature.geo;

import com.fasterxml.jackson.databind.JsonNode;
import com.wangkang.goodwillghservice.core.exception.BusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.geo.model.LocationNode;
import com.wangkang.goodwillghservice.dao.goodwillghservice.geo.repository.LocationNodeRepository;
import com.wangkang.goodwillghservice.share.util.JacksonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class GhanaLocationSyncService {
    private static final String HDX_API_URL = "https://data.humdata.org/api/3/action/package_show?id=cod-ab-gha";
    private static final Log log = LogFactory.getLog(GhanaLocationSyncService.class);
    private final LocationNodeRepository locationNodeRepository;
    private final OkHttpClient httpClient;

    public GhanaLocationSyncService(LocationNodeRepository locationNodeRepository,
                                    @Qualifier("secureOkHttpClient") OkHttpClient httpClient) {
        this.locationNodeRepository = locationNodeRepository;
        this.httpClient = httpClient;
    }

    /**
     * 核心同步入口
     * @param force 是否强制更新（忽略时间戳检查）
     */
    @Transactional
    public void syncLocations(boolean force) {
        log.info("开始加纳地理位置数据同步检查...");

        try {
            // 1. 获取 HDX 元数据
            JsonNode hdxResource = fetchXlsxResourceMetadata();
            String downloadUrl = hdxResource.get("download_url").asText();
            String lastModifiedStr = hdxResource.get("last_modified").asText();

            // 2. 检查是否需要更新 (你可以根据需要将上次同步时间存入 DB 的配置表或 Redis)
            if (!force && isAlreadyUpToDate(lastModifiedStr)) {
                log.info("数据已是最新 (Last Modified: " + lastModifiedStr + ")，跳过同步。");
                return;
            }

            // 3. 下载文件到临时目录
            File tempFile = downloadToTempFile(downloadUrl);
            log.info("临时文件已下载: " + tempFile.getAbsolutePath());

            try {
                // 4. 解析并处理数据
                processExcelData(tempFile);


                log.info("加纳地理位置数据同步成功完成！");
            } finally {
                // 6. 无论如何删除临时文件
                Files.deleteIfExists(tempFile.toPath());
            }

        } catch (Exception e) {
            log.error("同步过程中发生异常: ", e);
            throw new BusinessException("Location sync failed: " + e.getMessage());
        }
    }

    private void processExcelData(File file) throws IOException {
        Set<String> activePcodes = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(file)) {
            // 定位到 gha_admin2
            Sheet sheet = workbook.getSheet("gha_admin2");
            if (sheet == null) throw new BusinessException("未找到名为 'gha_admin2' 的 Sheet");

            // 获取表头映射，增强代码健壮性
            Row header = sheet.getRow(0);
            Map<String, Integer> colMap = getHeaderMap(header);

            // 从第一行开始读取
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String regName = getCellValue(row, colMap.get("adm1_name"));
                String regPcode = getCellValue(row, colMap.get("adm1_pcode"));
                String disName = getCellValue(row, colMap.get("adm2_name"));
                String disPcode = getCellValue(row, colMap.get("adm2_pcode"));

                // A. 处理一级区划 (Region)
                LocationNode region = upsertNode(regName, regPcode, 1, null);
                activePcodes.add(regPcode);

                // B. 处理二级区划 (District)
                upsertNode(disName, disPcode, 2, region.getId());
                activePcodes.add(disPcode);
            }
        }

        // C. 清理无效的 HDX 数据 (逻辑删除)
        // 找出所有 source='HDX' 且不在本次活跃名单中的 P-Code 进行软删除
        // 关键：排除掉那些下面挂载了 MANUAL 三级区域的节点
        if (!activePcodes.isEmpty()) {
            locationNodeRepository.markObsoleteHdxNodes(activePcodes);
        }
    }

    private LocationNode upsertNode(String name, String pcode, int level, Long parentId) {
        // 以 p_code + source 作为联合识别
        return locationNodeRepository.findByPCodeAndSource(pcode, "HDX")
                .map(existing -> {
                    existing.setName(name);
                    existing.setParentId(parentId);
                    existing.setDeleted(false); // 如果之前被删除，现在回来了，则激活
                    return locationNodeRepository.save(existing);
                })
                .orElseGet(() -> {
                    LocationNode newNode = new LocationNode(name, pcode, level, parentId, "HDX");
                    return locationNodeRepository.save(newNode);
                });
    }

    private JsonNode fetchXlsxResourceMetadata() throws IOException {
        Request request = new Request.Builder().url(HDX_API_URL).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new BusinessException("无法获取 HDX 元数据");

            JsonNode root = JacksonUtils.getObjectMapper().readTree(Objects.requireNonNull(response.body()).string());
            JsonNode resources = root.path("result").path("resources");

            for (JsonNode res : resources) {
                if ("XLSX".equalsIgnoreCase(res.path("format").asText())) {
                    return res;
                }
            }
        }
        throw new BusinessException("元数据中未发现 XLSX 资源");
    }

    private File downloadToTempFile(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new BusinessException("文件下载失败: " + url);

            File tempFile = File.createTempFile("gha_locations_", ".xlsx");
            try (InputStream is = Objects.requireNonNull(response.body()).byteStream();
                 FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        }
    }

    // ================= 辅助工具方法 =================

    private Map<String, Integer> getHeaderMap(Row header) {
        Map<String, Integer> map = new HashMap<>();
        for (Cell cell : header) {
            map.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        return map;
    }

    private String getCellValue(Row row, Integer index) {
        if (index == null) return null;
        Cell cell = row.getCell(index);
        if (cell == null) return null;
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.toString();
    }

    /**
     * 判断是否已经是最新数据
     */
    private boolean isAlreadyUpToDate(String lastModified) {
        try {
            if (lastModified == null || lastModified.isEmpty()) {
                return false;
            }

            // 1. 解析 HDX 的时间字符串 (例如: 2025-12-18T16:52:21.908085)
            // 转换为 OffsetDateTime，假设 HDX 返回的是 UTC
            LocalDateTime ldt = LocalDateTime.parse(lastModified, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            OffsetDateTime hdxModifiedTime = ldt.atOffset(ZoneOffset.UTC);

            // 2. 从数据库获取 HDX 来源数据的最大更新时间
            Optional<OffsetDateTime> lastUpdateInDb = locationNodeRepository.findMaxUpdatedAtBySource();

            if (lastUpdateInDb.isPresent()) {
                // 3. 如果数据库最新的更新时间 >= HDX 文件的修改时间，则认为是无需更新
                // 注意：由于同步时数据库记录的是“同步开始的时间”，所以这个值通常会比文件修改时间晚很久
                boolean upToDate = !lastUpdateInDb.get().isBefore(hdxModifiedTime);
                log.info(
                        "检查更新: DB最新记录时间=" + lastUpdateInDb.get() + ", HDX文件时间=" + hdxModifiedTime + ", 结果=" + (upToDate ? "无需同步" : "需要同步"));
                return upToDate;
            }

            return false; // 数据库没数据，必须同步
        } catch (Exception e) {
            log.error("解析修改时间失败: " + lastModified, e);
            return false; // 解析失败则稳妥起见执行同步
        }
    }


}
