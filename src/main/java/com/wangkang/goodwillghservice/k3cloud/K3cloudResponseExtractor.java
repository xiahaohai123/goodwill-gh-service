package com.wangkang.goodwillghservice.k3cloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wangkang.goodwillghservice.k3cloud.model.response.Error;
import com.wangkang.goodwillghservice.k3cloud.model.response.K3cloudResponse;
import com.wangkang.goodwillghservice.k3cloud.model.response.ResponseStatus;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class K3cloudResponseExtractor {
    private K3cloudResponseExtractor() {
    }

    public static String getResponseContentNumber(String resp) {
        Map<String, Object> stringObjectMap = respStr2Map(resp);
        return getResponseContentNumber(stringObjectMap);
    }

    @SuppressWarnings("unchecked")
    public static String getResponseContentNumber(Map<String, Object> map) {
        if (!isResponseSuccess(map)) {
            return null;
        }
        Map<String, Object> responseStatus = getResponseStatus(map);
        List<Map<String, Object>> successEntities = (List<Map<String, Object>>) responseStatus.getOrDefault(
                "SuccessEntitys", Collections.emptyList());
        if (successEntities.isEmpty()) {
            return null;
        }

        return (String) successEntities.get(0).get("Number");
    }

    public static Integer getResponseContentId(String resp) {
        Map<String, Object> stringObjectMap = respStr2Map(resp);
        return getResponseContentId(stringObjectMap);
    }

    @SuppressWarnings("unchecked")
    public static Integer getResponseContentId(Map<String, Object> map) {
        if (!isResponseSuccess(map)) {
            return null;
        }
        Map<String, Object> responseStatus = getResponseStatus(map);
        List<Map<String, Object>> successEntities = (List<Map<String, Object>>) responseStatus.getOrDefault(
                "SuccessEntitys", Collections.emptyList());
        if (successEntities.isEmpty()) {
            return null;
        }

        return new BigDecimal(successEntities.get(0).get("Id").toString()).intValue();
    }

    public static boolean isResponseSuccess(Map<String, Object> map) {
        Map<String, Object> responseStatus = getResponseStatus(map);
        Object isSuccess = responseStatus.getOrDefault("IsSuccess", false);
        return Boolean.TRUE.equals(isSuccess);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSubResult(Map<String, Object> resp) {
        Map<String, Object> result = ((Map<String, Object>) resp.getOrDefault("Result", Collections.emptyMap()));
        return (Map<String, Object>) result.getOrDefault("Result", Collections.emptyMap());
    }

    public static boolean isResponseSuccess(String respStr) {
        Map<String, Object> map = respStr2Map(respStr);
        return isResponseSuccess(map);
    }

    public static Map<String, Object> respStr2Map(String respStr) {
        Type mapStringObjectType = new TypeToken<Map<String, Object>>() {
        }.getType();
        return new Gson().fromJson(respStr, mapStringObjectType);
    }

    public static String getResponseErrorMsg(String respStr) {
        Map<String, Object> map = respStr2Map(respStr);
        return getResponseErrorMsg(map);
    }

    @SuppressWarnings("unchecked")
    public static String getResponseErrorMsg(Map<String, Object> map) {
        Map<String, Object> responseStatus = getResponseStatus(map);
        boolean isSuccess = (Boolean) responseStatus.getOrDefault("IsSuccess", false);
        if (isSuccess) {
            return null;
        }
        List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.getOrDefault(
                "Errors", Collections.emptyList());
        if (errors.isEmpty()) {
            return null;
        }
        return errors.get(0).getOrDefault("Message", "").toString();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getResponseStatus(Map<String, Object> map) {
        Map<String, Object> result = ((Map<String, Object>) map.getOrDefault("Result", Collections.emptyMap()));
        return (Map<String, Object>) result.getOrDefault("ResponseStatus", Collections.emptyMap());
    }

    public static K3cloudResponse extract2Response(String respStr) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(respStr, K3cloudResponse.class);
        } catch (JsonProcessingException e) {
            throw new K3cloudBusinessException(e);
        }
    }

    public static void throwIfFailed(K3cloudResponse k3cloudResponse) {
        ResponseStatus responseStatus = k3cloudResponse.getResult().getResponseStatus();
        boolean success = responseStatus.isSuccess();
        if (!success) {
            List<Error> errors = responseStatus.getErrors();
            String collect = errors.stream()
                    .map(Error::getMessage)
                    .collect(Collectors.joining("\n"));
            throw new K3cloudBusinessException(collect);
        }
    }
}
