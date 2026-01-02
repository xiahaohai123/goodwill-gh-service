package com.wangkang.goodwillghservice.core;

import com.wangkang.goodwillghservice.core.exception.BusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.system.model.SysParam;
import com.wangkang.goodwillghservice.dao.goodwillghservice.system.repository.SysParamRepository;
import com.wangkang.goodwillghservice.share.util.JacksonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class SysParamService {
    private final SysParamRepository sysParamRepository;

    public SysParamService(SysParamRepository sysParamRepository) {
        this.sysParamRepository = sysParamRepository;
    }

    /**
     * 通用的获取参数方法
     * @param key   参数键
     * @param clazz 想要转换的目标类型 (String, Integer, OffsetDateTime 等)
     */
    public <T> T getParam(String key, Class<T> clazz) {
        return sysParamRepository.findByParamKey(key)
                .map(param -> JacksonUtils.fromJson(param.getParamValue(), clazz))
                .orElseThrow(() -> new BusinessException("System parameter not found: " + key));
    }

    /**
     * 专门为你的 K3Cloud 提供的快捷方法
     */
    public OffsetDateTime getOffsetDateTime(String key) {
        return getParam(key, OffsetDateTime.class);
    }

    /**
     * 通用的更新/保存方法
     * @param key   参数键
     * @param value 参数值（可以是 POJO, String, Integer 等）
     */
    @Transactional
    public void updateParam(String key, Object value) {
        // 将对象序列化为 JSON 字符串
        String jsonValue = JacksonUtils.toJson(value);

        SysParam param = sysParamRepository.findByParamKey(key)
                .orElse(SysParam.builder().paramKey(key).build());

        param.setParamValue(jsonValue);
        // 顺便自动识别一下类型存入 value_type（可选）
        param.setValueType(value.getClass().getSimpleName().toLowerCase());

        sysParamRepository.save(param);
    }
}
