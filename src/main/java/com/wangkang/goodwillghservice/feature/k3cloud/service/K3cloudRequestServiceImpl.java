package com.wangkang.goodwillghservice.feature.k3cloud.service;


import com.google.gson.JsonObject;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.wangkang.goodwillghservice.feature.k3cloud.K3cloudBusinessException;
import com.wangkang.goodwillghservice.feature.k3cloud.K3cloudResponseExtractor;
import com.wangkang.goodwillghservice.feature.k3cloud.model.FormType;
import com.wangkang.goodwillghservice.feature.k3cloud.model.RequestConstant;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class K3cloudRequestServiceImpl implements K3cloudRequestService {

    private static final Log log = LogFactory.getLog(K3cloudRequestServiceImpl.class);
    private final K3CloudApi client;

    @Autowired
    public K3cloudRequestServiceImpl(K3CloudApi client) {
        this.client = client;
    }

    @NotNull
    private static String addOrgIdFilter(String filterString, String fieldName) {
        String currentFilterString = fieldName + " = '" + RequestConstant.ORGANIZATION_NUMBER + "'";
        if (StringUtils.isNotBlank(filterString)) {
            currentFilterString += " and (" + filterString + ")";
        }
        return currentFilterString;
    }

    @NotNull
    private static String addSaleOrgId(String filterString) {
        return addOrgIdFilter(filterString, "FSaleOrgId.FNumber");
    }

    @NotNull
    private static String addUserOrgId(String filterString) {
        return addOrgIdFilter(filterString, "FUseOrgId.FNumber");
    }

    @NotNull
    private static String addStockOrgId(String filterString) {
        return addOrgIdFilter(filterString, "FStockOrgId.FNumber");
    }

    @Override
    public List<Map<String, Object>> billQueryOrderFieldsByFilter(String filterString, String fieldKeys) {
        String currentFilterString = addSaleOrgId(filterString);
        return billQueryFieldsByFilter(FormType.SALE_ORDER, currentFilterString, fieldKeys);
    }

    @Override
    public List<Map<String, Object>> billQueryOrderFieldsByFilter(String filterString,
                                                                  String fieldKeys,
                                                                  Integer startRow,
                                                                  Integer limit) {
        return billQueryFieldsByFilterSaleOrganization(FormType.SALE_ORDER, filterString, fieldKeys, startRow, limit);
    }

    @Override
    public List<Map<String, Object>> billQueryMaterialFieldsByFilter(String filterString, String fieldKeys) {
        return billQueryFieldsByFilterWithUserOrgId(FormType.BD_MATERIAL, filterString, fieldKeys);
    }

    @Override
    public List<Map<String, Object>> billQueryMaterialFieldsByFilter(String filterString,
                                                                     String fieldKeys,
                                                                     Integer startRow,
                                                                     Integer limit) {
        String currentFilterString = addUserOrgId(filterString);
        return billQueryFieldsByFilter(FormType.BD_MATERIAL, currentFilterString, fieldKeys, startRow, limit);
    }

    @Override
    public List<Map<String, Object>> billQuerySaleOutFieldsByFilter(String filterString, String fieldKeys) {
        String currentFilterString = addSaleOrgId(filterString);
        return billQueryFieldsByFilter(FormType.SALE_OUT_STOCK, currentFilterString, fieldKeys);
    }

    @Override
    public List<Map<String, Object>> billQuerySaleOutFieldsByFilter(String filterString,
                                                                    String fieldKeys,
                                                                    Integer startRow,
                                                                    Integer limit) {
        return billQueryFieldsByFilterSaleOrganization(FormType.SALE_OUT_STOCK, filterString, fieldKeys, startRow,
                limit);
    }

    private List<Map<String, Object>> billQueryFieldsByFilterSaleOrganization(FormType formType,
                                                                              String filterString,
                                                                              String fieldKeys,
                                                                              Integer startRow,
                                                                              Integer limit) {
        String currentFilterString = addSaleOrgId(filterString);
        return billQueryFieldsByFilter(formType, currentFilterString, fieldKeys, startRow, limit);
    }

    @Override
    public List<Map<String, Object>> billQueryFieldsByFilterWithUserOrgId(FormType formType,
                                                                          String filterString,
                                                                          String fieldKeys) {
        String currentFilterString = addUserOrgId(filterString);
        return billQueryFieldsByFilter(formType, currentFilterString, fieldKeys);
    }

    @Override
    public List<Map<String, Object>> billQueryFieldsByFilterWithStockOrgId(FormType formType,
                                                                           String filterString,
                                                                           String fieldKeys) {
        String currentFilterString = addStockOrgId(filterString);
        return billQueryFieldsByFilter(formType, currentFilterString, fieldKeys);
    }

    @Override
    public List<Map<String, Object>> billQueryFieldsByFilter(FormType formType,
                                                             String filterString,
                                                             String fieldKeys) {
        int startIndex = 0;
        int limit = 2000;
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> orderMapList;
        do {
            orderMapList = billQueryFieldsByFilter(formType, filterString, fieldKeys, startIndex, limit);
            resultList.addAll(orderMapList);
            startIndex += limit;
        } while (orderMapList.size() == limit);
        return resultList;
    }

    @Override
    public List<Map<String, Object>> billQueryFieldsByFilter(FormType formType,
                                                             String filterString,
                                                             String fieldKeys,
                                                             Integer startRow,
                                                             Integer limit) {
        if (StringUtils.isBlank(fieldKeys)) {
            return Collections.emptyList();
        }

        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("FormId", formType.getId());
        dataObject.addProperty("FieldKeys", fieldKeys);
        if (StringUtils.isNotBlank(filterString)) {
            dataObject.addProperty("FilterString", filterString);
        }
        if (startRow != null) {
            dataObject.addProperty("startRow", startRow);
        }
        if (limit != null) {
            dataObject.addProperty("limit", limit);
        }
        String queryJson = dataObject.toString();
        log.debug("Execute Bill Query, queryJson: " + queryJson);
        List<List<Object>> materialObjectListList;
        try {
            materialObjectListList = client.executeBillQuery(queryJson);
        } catch (Exception e) {
            throw new K3cloudBusinessException("Failed to execute bill query", e);
        }
        String failedResult = getFailedResult(materialObjectListList);
        if (StringUtils.isNotBlank(failedResult)) {
            throw new K3cloudBusinessException("Failed to execute bill query, reason: " + failedResult);
        }
        String[] keys = Arrays.stream(fieldKeys.split(","))
                .map(String::trim)
                .toArray(String[]::new);
        return materialObjectListList
                .stream()
                .map(materialObjectList ->
                        {
                            Map<String, Object> map = new HashMap<>();
                            int len = Math.min(keys.length, materialObjectList.size());
                            for (int i = 0; i < len; i++) {
                                map.put(keys[i], materialObjectList.get(i));
                            }
                            return map;
                        }
                )
                .toList();
    }

    @SuppressWarnings("unchecked")
    private String getFailedResult(List<List<Object>> response) {
        if (response.isEmpty()) {
            return null;
        }
        Object zeroObj = response.getFirst().getFirst();
        if (zeroObj instanceof Map<?, ?>) {
            Map<String, Object> resultMap = (Map<String, Object>) zeroObj;
            return K3cloudResponseExtractor.getResponseErrorMsg(resultMap);
        }
        return null;
    }

    @Override
    public String view(String formId, String queryString) {
        try {
            return client.view(formId, queryString);
        } catch (Exception e) {
            throw new K3cloudBusinessException(
                    "Failed to view " + formId + " by query: " + queryString + ": " + e.getMessage(), e);
        }
    }

}
