package com.wangkang.goodwillghservice.k3cloud;

import com.kingdee.bos.webapi.sdk.K3CloudApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public K3CloudApi getClient() {
        return new K3CloudApi();
    }
}
