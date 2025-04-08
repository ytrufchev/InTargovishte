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
            System.out.println("Received empty or null HTML response from VIK client.");
            return new ArrayList<>();
        }

        Document doc = Jsoup.parse(htmlResponse);
        List<VikOutage> vikOutages = new ArrayList<>();

        Element targetLi = doc.select("ul.info_main_object_title_big_text > li:nth-child(2)").first();

        if (targetLi != null) {
            Element outageDiv = targetLi.select("div:first-child > div:first-child > div:first-child").first();

            if (outageDiv != null) {
                String outageInfoHtml = outageDiv.html();
                String[] outageEntries = outageInfoHtml.split("<br><br>");

                for (String entry : outageEntries) {
                    String cleanEntry = Jsoup.parse(entry).text().trim();

                    // Skip empty or whitespace entries
                    if (cleanEntry.isEmpty()) {
                        continue;
                    }

                    String date = "";
                    String startTime = "";
                    String endTime = "";
                    String description = "";

                    Pattern datePattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})г");
                    Matcher dateMatcher = datePattern.matcher(cleanEntry);
                    if (dateMatcher.find()) {
                        date = dateMatcher.group(1);
                    }

                    Pattern startTimePattern = Pattern.compile("от (\\d{2}:\\d{2})");
                    Matcher startTimeMatcher = startTimePattern.matcher(cleanEntry);
                    if (startTimeMatcher.find()) {
                        startTime = startTimeMatcher.group(1);
                    }

                    Pattern endTimePattern = Pattern.compile("до (\\d{2}:\\d{2})");
                    Matcher endTimeMatcher = endTimePattern.matcher(cleanEntry);
                    if (endTimeMatcher.find()) {
                        endTime = endTimeMatcher.group(1);
                    }

                    String desc = cleanEntry.replace(startTime, "").replace(endTime, "").replace(date, "").replace("до  часа", "").replaceAll("\\s+", " ")
                            .replace("на г.", "")
                            .replace("от часа", "")
                            .replace("- от - ", "")
                            .replaceAll("Район Омуртаг", "").trim(); // Added trim() here

                    VikOutage vikOutage = new VikOutage();
                    vikOutage.setDate(date);
                    vikOutage.setStartTime(startTime);
                    vikOutage.setEndTime(endTime);
                    vikOutage.setDescription(desc.replace("\\", ""));

                    vikOutages.add(vikOutage);
                }
            } else {
                System.out.println("Could not find the outage details div within the target li.");
            }
        } else {
            System.out.println("Could not find the target li element containing outage information.");
        }

        return vikOutages;
    }
}