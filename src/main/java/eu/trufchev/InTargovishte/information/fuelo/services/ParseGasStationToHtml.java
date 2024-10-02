package eu.trufchev.InTargovishte.information.fuelo.services;
import eu.trufchev.InTargovishte.information.fuelo.entities.FuelPrice;
import eu.trufchev.InTargovishte.information.fuelo.entities.GasStation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParseGasStationToHtml {

    public GasStation parseGasStationHtml(String html) {
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
                    formattedPrice = formattedPrice.replaceAll("(\\d+\\.\\d{2}).*", "$1");
                    fuelPrices.add(new FuelPrice(fuelType, formattedPrice));
                }
            }
        }

        return new GasStation(name, address, fuelPrices);
    }
}
