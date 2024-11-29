package eu.trufchev.intargovishte.information.vikOutage.services;

import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import eu.trufchev.intargovishte.information.vikOutage.feignClients.VikOutageClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VikOutageServiceTest {

    @Mock
    private VikOutageClient vikOutageClient;

    @InjectMocks
    private VikOutageService vikOutageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndParseVikOutage_SuccessfulParsing() {
        // Mock HTML response with valid data
        String mockHtml = """
            <div>
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                        <div>
                            <div></div>
                            <div></div>
                            <div>
                                <ul>
                                    <li></li>
                                    <li>
                                        <div>
                                            <div>
                                                <div>
                                                    <br>
                                                                                      гр.Търговище бул.29-ти януари /четни номера от ул.Скопие до бул.М.Андрей/ до 15:00 часа на 29.11.2024г.<br>
                                                                                      <br>
                                                                                      с.Здравец /ниска част/ на 29.11.2024г. до 16:00 часа<br>
                                                                                      <br>
                                                                                      с.Пробуда на 29.11.2024г.&nbsp; до 13:00 часа<br>
                                                                                      <br>
                                                                                      с.Миладиновци на 29.11.2024г. до 14:00 часа<br>
                                                                                      <br>
                                                                                      с.Осен на 29.11.2024г. до 11:00 часа<br>
                                                                                      <span style="color: rgb(255, 0, 0);">Район Омуртаг</span>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    
        """;
        when(vikOutageClient.getOutage("15", "53", "50")).thenReturn(mockHtml);

        // Call the method under test
        List<VikOutage> result = vikOutageService.fetchAndParseVikOutage();

        // Assertions
        assertNotNull(result);
        assertEquals(5, result.size());

        VikOutage firstOutage = result.get(0);
        assertEquals("29.11.2024", firstOutage.getDate());
        assertEquals("", firstOutage.getStartTime());
        assertEquals("15:00", firstOutage.getEndTime());
        assertEquals("гр.Търговище бул.29-ти януари /четни номера от ул.Скопие до бул.М.Андрей/ ", firstOutage.getDescription());

        VikOutage secondOutage = result.get(1);
        assertEquals("29.11.2024", secondOutage.getDate());
        assertEquals("", secondOutage.getStartTime());
        assertEquals("16:00", secondOutage.getEndTime());
        assertEquals("с.Здравец /ниска част/  ", secondOutage.getDescription());

        // Verify interactions
        verify(vikOutageClient, times(1)).getOutage("15", "53", "50");
    }

    @Test
    void testFetchAndParseVikOutage_NoTargetDiv() {
        // Mock HTML response without the target div
        String mockHtml = "<html><body><div>No relevant data here</div></body></html>";
        when(vikOutageClient.getOutage("15", "53", "50")).thenReturn(mockHtml);

        // Call the method under test
        List<VikOutage> result = vikOutageService.fetchAndParseVikOutage();

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(vikOutageClient, times(1)).getOutage("15", "53", "50");
    }

    @Test
    void testFetchAndParseVikOutage_InvalidEntries() {
        // Mock HTML response with invalid data
        String mockHtml = """
            <div>
                <div>
                    <ul>
                        <li>
                            <div>
                                <div>
                                    <div>
                                        <div>Invalid data entry<br><br></div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        """;
        when(vikOutageClient.getOutage("15", "53", "50")).thenReturn(mockHtml);

        // Call the method under test
        List<VikOutage> result = vikOutageService.fetchAndParseVikOutage();

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(vikOutageClient, times(1)).getOutage("15", "53", "50");
    }

    @Test
    void testFetchAndParseVikOutage_EmptyResponse() {
        // Mock HTML response with empty content
        when(vikOutageClient.getOutage("15", "53", "50")).thenReturn("");

        // Call the method under test
        List<VikOutage> result = vikOutageService.fetchAndParseVikOutage();

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(vikOutageClient, times(1)).getOutage("15", "53", "50");
    }

    @Test
    void testFetchAndParseVikOutage_NullResponse() {
        // Mock HTML response with null
        when(vikOutageClient.getOutage("15", "53", "50")).thenReturn(null);

        // Call the method under test
        List<VikOutage> result = vikOutageService.fetchAndParseVikOutage();

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(vikOutageClient, times(1)).getOutage("15", "53", "50");
    }
}
