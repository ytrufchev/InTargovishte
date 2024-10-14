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
        Document doc = Jsoup.parse(htmlResponse);

        List<VikOutage> vikOutages = new ArrayList<>();

        // Assuming the target div is still the one you extracted via XPath
        Element targetDiv = doc.select("div:nth-of-type(5) > div:nth-of-type(3) > ul > li:nth-of-type(2) > div > div > div:first-child").first();

        if (targetDiv != null) {
            String[] outageEntries = targetDiv.html().split("<br><br>");

            for (String entry : outageEntries) {
                String cleanEntry = Jsoup.parse(entry).text().trim();  // Clean HTML tags and trim whitespace

                // Skip empty or whitespace entries
                if (cleanEntry.isEmpty()) {
                    continue;
                }

                // Set default values
                String date = "";
                String startTime = ""; // Default start time
                String endTime = ""; // Default end time
                String description = cleanEntry; // Initialize description with the entire entry

                // Match the date
                Pattern datePattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})");
                Matcher dateMatcher = datePattern.matcher(cleanEntry);
                if (dateMatcher.find()) {
                    date = dateMatcher.group(1);
                    description = description.replace(date, "").trim(); // Remove date from description
                }

                // Match the startTime
                Pattern startTimePattern = Pattern.compile("от (\\d{2}:\\d{2})");
                Matcher startTimeMatcher = startTimePattern.matcher(cleanEntry);
                if (startTimeMatcher.find()) {
                    startTime = startTimeMatcher.group(1);
                    description = description.replace("от " + startTime, "").trim(); // Remove start time from description
                }

                // Match the endTime
                Pattern endTimePattern = Pattern.compile("до (\\d{2}:\\d{2})");
                Matcher endTimeMatcher = endTimePattern.matcher(cleanEntry);
                if (endTimeMatcher.find()) {
                    endTime = endTimeMatcher.group(1);
                    description = description.replace("до " + endTime, "").trim(); // Remove end time from description
                }

                // Clean up description by removing any leading or trailing whitespace
                description = description.replaceAll("\\s+", " "); // Normalize whitespace
                description = description.trim();

                // Create the VikOutage entity and set the fields
                VikOutage vikOutage = new VikOutage();
                vikOutage.setDate(date);
                vikOutage.setStartTime(startTime);
                vikOutage.setEndTime(endTime);
                vikOutage.setDescription(description.replace("\\", "")); // Remove any backslashes

                vikOutages.add(vikOutage);
            }
        }

        return vikOutages;
    }
}
