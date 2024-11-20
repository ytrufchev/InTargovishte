package eu.trufchev.intargovishte;


import feign.*;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Configuration
public class FeignConfigMunicipalityOnly {

    @Bean
    public Client feignClientMunicipalityONLY() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) { }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) { }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());

            return new Client.Default(sslContext.getSocketFactory(), (hostname, session) -> true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
