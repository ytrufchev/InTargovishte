package eu.trufchev.intargovishte.information.vikOutage.services;

import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import eu.trufchev.intargovishte.information.vikOutage.feignClients.VikOutageClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VikOutageService {

    @Autowired
    VikOutageClient vikOutageClient;

    public List<VikOutage> fetchAndParseVikOutage() {
        String htmlResponse = vikOutageClient.getOutage("15", "53", "50");
        if (htmlResponse == null || htmlResponse.isBlank()) {
            return new ArrayList<>();
        }
        htmlResponse = htmlResponse.replace("<span style=\"color: rgb(255, 0, 0);\">Район Омуртаг</span>", "");
        Document doc = Jsoup.parse(htmlResponse);
        List<VikOutage> vikOutages = new ArrayList<>();
        // Select the <li> that contains multiple <br>-separated entries

        Element listItem = doc.selectFirst("html > body > div > div > div > div:nth-child(5) > div:nth-child(3) > ul > li:nth-child(2) > div > div > div");

        if (listItem != null) {
            // Split the inner HTML on <br> or <br/> (both valid)
            String[] entries = listItem.html().split("<br>");

            for (String entryHtml : entries) {
                String cleanEntry = Jsoup.parse(entryHtml).text().trim();

                if (cleanEntry.isEmpty()) continue;

                String date = "", startTime = "", endTime = "";

                Matcher dateMatcher = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})г").matcher(cleanEntry);
                if (dateMatcher.find()) date = dateMatcher.group(1);

                Matcher startTimeMatcher = Pattern.compile("от (\\d{2}:\\d{2})").matcher(cleanEntry);
                if (startTimeMatcher.find()) startTime = startTimeMatcher.group(1);

                Matcher endTimeMatcher = Pattern.compile("до (\\d{2}:\\d{2})").matcher(cleanEntry);
                if (endTimeMatcher.find()) endTime = endTimeMatcher.group(1);

                String desc = cleanEntry.replace("на " + date, "")
                        .replaceFirst("Район Търговище", "")
                        .replaceFirst("от\\s*(\\d{2}:\\d{2})", "")
                        .replaceFirst("до\\s*(\\d{2}:\\d{2})", "")
                        .replaceAll("\\s+", " ")
                        .replace("г.", "")
                        .replace("часа", "")
                        .replace("-", "")
                        .trim();

                VikOutage vikOutage = new VikOutage();
                vikOutage.setDate(date);
                vikOutage.setStartTime(startTime);
                vikOutage.setEndTime(endTime);
                vikOutage.setDescription(desc.replace("\\", ""));

                vikOutages.add(vikOutage);
            }
        }

        return vikOutages;
    }
}