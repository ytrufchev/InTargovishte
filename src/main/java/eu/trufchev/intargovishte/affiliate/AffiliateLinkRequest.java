package eu.trufchev.intargovishte.affiliate;

import feign.form.FormProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AffiliateLinkRequest {

    @FormProperty("0[name]")
    private String name;

    @FormProperty("0[url]")
    private String url;

    public AffiliateLinkRequest(String name, String url) {
        this.name = name;
        this.url = url;
    }

}