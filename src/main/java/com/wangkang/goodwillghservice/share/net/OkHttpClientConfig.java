package com.wangkang.goodwillghservice.share.net;

import okhttp3.OkHttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Arrays;

@Configuration
public class OkHttpClientConfig {

    private static final Log log = LogFactory.getLog(OkHttpClientConfig.class);
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * 内部系统专用（可根据 profile 放宽 SSL）
     */
    @Bean(name = "innerOkHttpClient")
    public OkHttpClient innerOkHttpClient() {

        boolean insecureProfile = Arrays.asList("dev", "test", "local")
                .contains(activeProfile);

        if (insecureProfile) {
            return buildInsecureClient();
        }

        if ("pro".equals(activeProfile)) {
            // 可加日志
            log.info("OkHttpClient running in PRODUCTION mode with strict SSL validation");
        }

        return buildSecureClient();
    }

    /**
     * 第三方服务专用（永远安全）
     */
    @Bean(name = "secureOkHttpClient")
    public OkHttpClient secureOkHttpClient() {
        return buildSecureClient();
    }

    // ================== private ==================

    private OkHttpClient buildSecureClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .build();
    }

    private OkHttpClient buildInsecureClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(Duration.ofSeconds(10))
                    .readTimeout(Duration.ofSeconds(30))
                    .build();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to create insecure OkHttpClient", e);
        }
    }
}