package com.wangkang.goodwillghservice.feature.gwtoolstation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkang.goodwillghservice.core.exception.BusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.product.model.Tile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.product.repository.TileRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.gwtoolstation.model.MaterialDTO;
import com.wangkang.goodwillghservice.feature.gwtoolstation.model.MaterialPageResponse;
import com.wangkang.goodwillghservice.security.service.JwtService;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TileServiceImpl implements TileService {

    private final TileRepository tileRepository;

    /** 用于系统间调用 API 的 KEY */
    @Value("${goodwill-tool-station.host}")
    private String innerSystemHost;
    @Value("${goodwill-tool-station.url-prefix}")
    private String innerSystemUrlPrefix;

    private final JwtService jwtService;

    private final OkHttpClient httpClient;

    @Autowired
    public TileServiceImpl(JwtService jwtService,
                           TileRepository tileRepository,
                           @Qualifier("innerOkHttpClient") OkHttpClient httpClient) {
        this.jwtService = jwtService;
        this.tileRepository = tileRepository;
        this.httpClient = httpClient;
    }

    @Auditable(actionType = ActionType.PRODUCT, actionName = "Update material list", logResult = false)
    @Override
    public void updateAllTile() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("MATERIAL_QUERY");
        String jwt = jwtService.generateToken4GWToolStation(Collections.singleton(authority));

        ObjectMapper objectMapper = new ObjectMapper();

        int page = 0;
        int size = 100;
        int totalPages = 1; // 先假设 1，第一次请求后修正


        while (page < totalPages) {

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(innerSystemHost)
                    .addPathSegments(innerSystemUrlPrefix)
                    .addPathSegments("api/k3cloud/material/all")
                    .addQueryParameter("page", String.valueOf(page))
                    .addQueryParameter("size", String.valueOf(size))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + jwt)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {

                if (!response.isSuccessful()) {
                    throw new IllegalStateException("Material sync failed, httpStatus=" + response.code());
                }

                String body = Objects.requireNonNull(response.body()).string();
                MaterialPageResponse pageResponse =
                        objectMapper.readValue(body, MaterialPageResponse.class);

                // 初始化总页数（只做一次）
                if (page == 0) {
                    totalPages = pageResponse.getPage().getTotalPages();
                }

                List<MaterialDTO> materialList = Optional.ofNullable(pageResponse.getEmbedded())
                        .map(MaterialPageResponse.Embedded::getMaterialList)
                        .orElse(Collections.emptyList());

                if (!materialList.isEmpty()) {
                    syncMaterialBatch(materialList);
                }

                page++;

            } catch (IOException e) {
                throw new BusinessException("Material sync error on page " + page, e);
            }
        }
    }

    private void syncMaterialBatch(List<MaterialDTO> dtoList) {

        // 提前获取已有的物料记录（按 code 映射）
        List<String> allCodes = dtoList.stream()
                .map(MaterialDTO::getCode)
                .toList();
        Map<String, Tile> existingTileMap = tileRepository.findAllByCodeIn(allCodes)
                .stream()
                .collect(Collectors.toMap(Tile::getCode, Function.identity()));

        List<Tile> tileToSaveList = new ArrayList<>();
        for (MaterialDTO dto : dtoList) {
            String code = dto.getCode();
            Tile tile = existingTileMap.getOrDefault(code, new Tile());
            tile.setCode(code);
            tile.setName(dto.getName());
            tile.setModel(dto.getModel());
            tile.setStockUnit(dto.getStockUnit());
            tile.setWeightGross(dto.getWeightGross().floatValue());
            tileToSaveList.add(tile);
        }
        tileRepository.saveAll(tileToSaveList);
    }
}
