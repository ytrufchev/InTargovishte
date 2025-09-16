package eu.trufchev.intargovishte.affiliate;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class ProfitshareFeignConfig {

    @Value("${profitshare.api-user}")
    private String apiUser;

    @Value("${profitshare.api-key}")
    private String apiKey;

    // Inside ProfitshareFeignConfig
    @Bean
    public RequestInterceptor profitshareAuthInterceptor() {
        return (RequestTemplate template) -> {
            try {
                String method = template.method();
                String path = template.path().substring(1);
                String queryString = template.queryLine() != null ? template.queryLine() : "";

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String date = dateFormat.format(new Date());

                String signatureString;

                if (method.equals("POST")) {
                    // Correct signature for POST, as confirmed by the working cURL
                    signatureString = method + path + "_" + apiUser + date;
                } else if (method.equals("GET") && !queryString.isEmpty()) {
                    // Correct signature for GET with parameters, from previous examples
                    signatureString = method + path + "/?" + queryString + "/" + apiUser + date;
                } else {
                    // Correct signature for GET with no parameters
                    signatureString = method + path + "/" + apiUser + date;
                }

                String hmac = hmacSha1Hex(apiKey, signatureString);

                template.header("Date", date);
                template.header("X-PS-Client", apiUser);
                template.header("X-PS-Accept", "json");
                template.header("X-PS-Auth", hmac);

            } catch (Exception e) {
                throw new RuntimeException("Error generating Profitshare HMAC", e);
            }
        };
    }

    private String hmacSha1Hex(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder(2 * rawHmac.length);
        for (byte b : rawHmac) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    @Bean
    public Decoder feignDecoder() {
        ObjectFactory<HttpMessageConverters> objectFactory = () -> {
            HttpMessageConverters converters = new HttpMessageConverters(
                    new MappingJackson2HttpMessageConverter() {{
                        setSupportedMediaTypes(Collections.singletonList(
                                new MediaType("text", "json", StandardCharsets.UTF_8)
                        ));
                    }}
            );
            return converters;
        };
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }
}