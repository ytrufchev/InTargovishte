package eu.trufchev.intargovishte.affiliate;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
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

    @Bean
    public RequestInterceptor profitshareAuthInterceptor() {
        return (RequestTemplate template) -> {
            try {
                String method = template.method();
                URI url = URI.create(template.url());
                String path = url.getPath();
                String queryString = (url.getQuery() != null) ? url.getQuery() : "";

                // The signature string should not start with a slash
                String signaturePath = path.startsWith("/") ? path.substring(1) : path;

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String date = dateFormat.format(new Date());

                String signatureString;

                // Define logic for each known endpoint type
                if (method.equals("POST") && signaturePath.equals("affiliate-links/")) {
                    // Special case for POST to affiliate-links, confirmed by working cURL
                    signatureString = method + signaturePath + apiUser + date;
                } else if (method.equals("GET") && !queryString.isEmpty()) {
                    // GET requests with query parameters (e.g., campaigns)
                    signatureString = method + signaturePath + "?" + queryString + "/" + apiUser + date;
                } else {
                    // Default case for GET with no query string (e.g., advertisers) and other POSTs
                    signatureString = method + signaturePath + "/" + apiUser + date;
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

    @Bean
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        // This encoder handles both application/json and application/x-www-form-urlencoded
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
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