package eu.trufchev.intargovishte.affiliate;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
                String method = template.method(); // GET
                URI url = URI.create(template.url());
                String path = url.getPath().substring(1); // remove leading "/"
                String queryString = (url.getQuery() != null) ? url.getQuery() : "";

                // RFC 1123 date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String date = dateFormat.format(new Date());

                String signatureString = method + path + (queryString.isEmpty() ? "" : "?" + queryString)
                        + "/" + apiUser + date;

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

        // convert to hex manually
        StringBuilder hex = new StringBuilder(2 * rawHmac.length);
        for (byte b : rawHmac) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
