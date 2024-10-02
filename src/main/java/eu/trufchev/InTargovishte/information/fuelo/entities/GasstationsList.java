package eu.trufchev.InTargovishte.information.fuelo.entities;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GasstationsList {
    private final List<String> gasStations = new ArrayList<>();

    public GasstationsList() {
        gasStations.add("42984"); // Shell
        gasStations.add("2808");  // Хемус Ойл
        gasStations.add("1406");  // Ром Петрол
        gasStations.add("190");   // Лукойл Б136
        gasStations.add("62566"); // Ванеда
        gasStations.add("62569"); // Йоана 2
        gasStations.add("62568"); // Йоана
        gasStations.add("1809");  // Озирис
        gasStations.add("570");   // OMV
        gasStations.add("1545");  // ЕКО
        gasStations.add("1885");  // Величков
        gasStations.add("1608");  // DSB Oil
        gasStations.add("1724");  // Тайфун
        gasStations.add("1271");  // Петрол
        gasStations.add("191");   // Лукойл Б112
        gasStations.add("2111");  // Сима
        gasStations.add("65037"); // Бенита
        gasStations.add("1884");  // Юкя
    }

    public List<String> getGasstations() {
        return gasStations;
    }
}
