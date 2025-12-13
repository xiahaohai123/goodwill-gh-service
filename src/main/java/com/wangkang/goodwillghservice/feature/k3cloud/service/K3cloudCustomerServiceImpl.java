package com.wangkang.goodwillghservice.feature.k3cloud.service;

import com.wangkang.goodwillghservice.feature.k3cloud.model.customer.Customer;
import com.wangkang.goodwillghservice.share.util.ChoreUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wangkang.goodwillghservice.feature.k3cloud.model.FormType.BD_CUSTOMER;

@Service
public class K3cloudCustomerServiceImpl implements K3cloudCustomerService {


    private final K3cloudRequestServiceImpl k3cloudRequestService;

    public K3cloudCustomerServiceImpl(K3cloudRequestServiceImpl k3cloudRequestService) {
        this.k3cloudRequestService = k3cloudRequestService;
    }

    @Override
    public List<Customer> getCustomerList() {
        List<Map<String, Object>> customerListMap = getCustomerListMap();
        List<Customer> resultList = new ArrayList<>();
        for (Map<String, Object> objMap : customerListMap) {
            Customer customer = new Customer();
            customer.setCustomerId(ChoreUtil.toInteger(objMap.get("FCUSTID")));
            customer.setCode(ChoreUtil.toString(objMap.get("FNumber")));
            customer.setName(ChoreUtil.toString(objMap.get("FName")));
            customer.setType(ChoreUtil.toString(objMap.get("FCustTypeId.FDataValue")));
            resultList.add(customer);
        }
        return resultList;
    }

    private List<Map<String, Object>> getCustomerListMap() {
        String fieldKeys = "FCUSTID, FNumber, FName, FCustTypeId.FDataValue";
        return k3cloudRequestService.billQueryFieldsByFilterWithUserOrgId(BD_CUSTOMER, "", fieldKeys);
    }
}
