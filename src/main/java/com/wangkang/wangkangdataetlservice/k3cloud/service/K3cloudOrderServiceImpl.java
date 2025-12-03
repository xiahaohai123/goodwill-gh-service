package com.wangkang.wangkangdataetlservice.k3cloud.service;

import com.wangkang.wangkangdataetlservice.k3cloud.model.OrderBillType;
import com.wangkang.wangkangdataetlservice.metabase.sale.SaleOrder;
import com.wangkang.wangkangdataetlservice.util.ChoreUtil;
import com.wangkang.wangkangdataetlservice.util.DateUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Service
public class K3cloudOrderServiceImpl implements K3cloudOrderService {

    private static final String BASE_ORDER_FILTER = "FUnitID.FNumber = 'CTN'" +
            " and FBillTypeId.FNumber = '" + OrderBillType.STANDARD.code + "'" +
            " and FCUSTID.FName NOT LIKE '%Nana Jojo(破砖)%' and FCUSTID.FName NOT LIKE '%N Boss(破砖)%'" +
            " and FCUSTID.FName NOT LIKE '%废品销售%' and FCUSTID.FName NOT LIKE '%旺康物流车队%'";

    /** 给 BI 用的订单列 */
    private static final String ORDER_FIELD_BI = "FDate, FBillNo, FBillTypeId.FName, FDocumentStatus, FCustId.FNumber, FCustId.FName, " +
            "FSaleDeptId.FName, FCloseStatus, F_ora_CityId.FDataValue, FSettleCurrId.FName, F_ora_KHGJ, " +
            "F_ora_JNSF, FMaterialId.FNumber, FMaterialName, FUnitID.FNumber, FQty, " +
            "FMaterialModel, FPrice, FAmount, F_ora_ZhekouType, FReviewStatus, FReviewDate, FCreateDate, " +
            "FCloseDate, F_ora_SumTon";

    private final K3cloudRequestService k3cloudRequestService;

    public K3cloudOrderServiceImpl(K3cloudRequestService k3cloudRequestService) {
        this.k3cloudRequestService = k3cloudRequestService;
    }

    @Override
    public List<SaleOrder> getOrderBIListByCreateDate(String startDate, String endDate) {
        String currentFilterString = BASE_ORDER_FILTER;
        if (startDate != null) {
            String startTime = DateUtil.toBeijingStartOfDayFormatted(startDate);
            currentFilterString = currentFilterString + " and FCreateDate >= '" + startTime + "'";
        }
        if (endDate != null) {
            String endTime = DateUtil.toBeijingStartOfDayFormatted(endDate);
            currentFilterString = currentFilterString + " and FCreateDate < '" + endTime + "'";
        }
        List<Map<String, Object>> orderMapList;
        orderMapList = k3cloudRequestService.billQueryOrderFieldsByFilter(currentFilterString, ORDER_FIELD_BI);
        return map2SaleOrderDTO(orderMapList);
    }

    private static List<SaleOrder> map2SaleOrderDTO(List<Map<String, Object>> orderMapList) {
        return orderMapList.stream().map(obj -> {
            SaleOrder bi = new SaleOrder();
            bi.setBillNo(ChoreUtil.toString(obj.get("FBillNo")));
            bi.setDate(ChoreUtil.toString(obj.get("FDate")));
            bi.setBillType(ChoreUtil.toString(obj.get("FBillTypeId.FName")));
            bi.setDocumentStatus(ChoreUtil.toString(obj.get("FDocumentStatus")));
            bi.setCustomerNumber(ChoreUtil.toString(obj.get("FCustId.FNumber")));
            bi.setCustomerName(ChoreUtil.toString(obj.get("FCustId.FName")));
            bi.setSaleDepartment(ChoreUtil.toString(obj.get("FSaleDeptId.FName")));
            bi.setCloseStatus(ChoreUtil.toString(obj.get("FCloseStatus")));
            bi.setCloseDate(ChoreUtil.toString(obj.get("FCloseDate")));
            bi.setCity(ChoreUtil.toString(obj.get("F_ora_CityId.FDataValue")));
            bi.setSettlementCurrency(ChoreUtil.toString(obj.get("FSettleCurrId.FName")));
            bi.setCustomerCountry(ChoreUtil.toString(obj.get("F_ora_KHGJ")));
            bi.setProvince(ChoreUtil.toString(obj.get("F_ora_JNSF")));
            bi.setMaterialNumber(ChoreUtil.toString(obj.get("FMaterialId.FNumber")));
            bi.setMaterialName(ChoreUtil.toString(obj.get("FMaterialName")));
            bi.setSpecificationModel(ChoreUtil.toString(obj.get("FMaterialModel")));
            bi.setUnit(ChoreUtil.toString(obj.get("FUnitID.FNumber")));
            bi.setQuantity(new BigDecimal(ChoreUtil.toString(obj.get("FQty"))).intValue());
            bi.setPrice(new BigDecimal(ChoreUtil.toString(obj.get("FPrice"))).doubleValue());
            bi.setAmount(new BigDecimal(ChoreUtil.toString(obj.get("FAmount"))).doubleValue());
            bi.setDiscountType(ChoreUtil.toString(obj.get("F_ora_ZhekouType")));
            bi.setVerifyStatus(ChoreUtil.toString(obj.get("FReviewStatus")));
            bi.setVerifyDate(ChoreUtil.toString(obj.get("FReviewDate")));
            bi.setCreateDate(ChoreUtil.toString(obj.get("FCreateDate")));
            bi.setTotalWeightTon(ChoreUtil.toDouble(obj.get("F_ora_SumTon")));
            return bi;
        }).toList();
    }

    @Override
    public Collection<SaleOrder> getOrderBIListByCreateDate(String startDate, String endDate, int startIndex, int limit) {
        String currentFilterString = BASE_ORDER_FILTER;
        if (startDate != null) {
            String startTime = DateUtil.toBeijingStartOfDayFormatted(startDate);
            currentFilterString = currentFilterString + " and FCreateDate >= '" + startTime + "'";
        }
        if (endDate != null) {
            String endTime = DateUtil.toBeijingStartOfDayFormatted(endDate);
            currentFilterString = currentFilterString + " and FCreateDate < '" + endTime + "'";
        }
        List<Map<String, Object>> orderMapList;
        orderMapList = k3cloudRequestService.billQueryOrderFieldsByFilter(
                currentFilterString, ORDER_FIELD_BI, startIndex, limit);
        return map2SaleOrderDTO(orderMapList);
    }
}
