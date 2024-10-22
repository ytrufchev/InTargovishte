package eu.trufchev.intargovishte.information.fuelo.services;
import eu.trufchev.intargovishte.information.fuelo.entities.FuelPrice;
import eu.trufchev.intargovishte.information.fuelo.entities.FuelStation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParseGasStationToHtml {

    public FuelStation parseGasStationHtml(String html) {
        // Decode any Unicode characters (like \u003C) and parse the HTML with Jsoup
        String decodedHtml = html.replace("\\u003C", "<").replace("\\u003E", ">").replace("\\/", "/").replace("\\", "");
        Document doc = Jsoup.parse(decodedHtml);

        // Extract gas station name
        String name = doc.select("h4").text(); // Extract <h4> content

        // Extract address
        String address = doc.select("h5").text(); // Extract <h5> content

        // Extract fuel prices
        Elements fuelElements = doc.select("img");
        List<FuelPrice> fuelPrices = new ArrayList<>();
        for (Element fuelElement : fuelElements) {
            String fuelType = fuelElement.attr("alt");
            String priceTitle = fuelElement.attr("title");
            String decodedPriceTile = priceTitle.replace("\\", "").replace("/", "");
            // Split the price only if the title contains ": "
            if (decodedPriceTile.contains(": ")) {
                String[] priceParts = decodedPriceTile.split(": ");
                if (priceParts.length == 2) {
                    String price = priceParts[1].trim(); // Extract the price part
                    String formattedPrice = price.replace(",", ".");
                    formattedPrice = formattedPrice.replaceAll("(\\d+\\.\\d{2})[^\\d]*", "$1");
                    try {
                        double priceValue = Double.parseDouble(formattedPrice);
                        fuelPrices.add(new FuelPrice(fuelType, priceValue)); // Assuming FuelPrice takes a double as the second parameter
                    } catch (NumberFormatException e) {
                        // Handle the exception if the price cannot be parsed to a double
                        System.err.println("Invalid price format: " + formattedPrice);
                    }
                }
            }
        }

        return new FuelStation(name, address, fuelPrices);
    }
}
