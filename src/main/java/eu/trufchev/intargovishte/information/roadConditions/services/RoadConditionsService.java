package eu.trufchev.intargovishte.information.roadConditions.services;

import eu.trufchev.intargovishte.information.roadConditions.entities.RoadConditions;
import eu.trufchev.intargovishte.information.roadConditions.feignClients.RoadConditionsClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoadConditionsService {

    @Autowired
    RoadConditionsClient roadConditionsClient;

    public List<RoadConditions> getRoadConditions() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        // Fetch the XML response
        String xmlResponse = roadConditionsClient.getRoadConditions(formattedDate);
        Document doc = Jsoup.parse(xmlResponse, "", org.jsoup.parser.Parser.xmlParser());

        List<RoadConditions> roadConditionsList = new ArrayList<>();

        // Select the parent element that contains <startDate> and <generalPublicComment>
        Elements roadWorks = doc.select("situation");  // Adjust 'roadWork' if the parent tag differs

        // Iterate over each roadWork element
        for (Element roadWork : roadWorks) {
            RoadConditions roadConditions = new RoadConditions();

            // Find the <startDate>
            Element endDateElement = roadWork.selectFirst("overallEndTime");
            if (endDateElement != null) {
                roadConditions.setEndDate(endDateElement.text());
            } else {
                roadConditions.setEndDate("Начална дата: Няма информация");
            }

            // Find the <generalPublicComment> that contains "ОПУ Търговище"
            Element commentElement = roadWork.selectFirst("generalPublicComment");
            if (commentElement != null && commentElement.text().contains("ОПУ Търговище")) {
                roadConditions.setDescription(commentElement.text().replace("ОПУ ТърговищеlocationDescriptor", ""));

                // Find the <overallEndTime> if it exists
                Element overallEndTimeElement = roadWork.selectFirst("overallEndTime");
                if (overallEndTimeElement != null) {
                    roadConditions.setEndDate(overallEndTimeElement.text());
                } else {
                    roadConditions.setEndDate("Крайна дата: Няма информация");
                }

                // Add to the list only if there is a relevant comment
                roadConditionsList.add(roadConditions);
            }
        }

        return roadConditionsList;
    }
}