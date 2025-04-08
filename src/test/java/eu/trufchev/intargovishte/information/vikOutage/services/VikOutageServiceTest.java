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
    void testFetchAndParseVikOutage_SuccessfulParsingWithModifiedService() {
        // Mock HTML response with valid data
        String mockHtml = """

                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml">
                <head>
                	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                	<link rel="stylesheet" type="text/css" href="/styles/style.css">
                	<title>ВИК - Търговище, Водоснабдяване и канализация ООД - Търговище - Информация - Предстоящи дейности</title>	<meta name="Author" content="Stefan Vasilev" />

                	<script type="text/javascript" src="/web_control/scripts/jquery/js/jquery-1.4.2.min.js"></script>
                	<script type="text/javascript" src="/scripts/check.js"></script>
                	<script type="text/javascript" src="/scripts/animate.js"></script>
                	<script src="/scripts/jquery.validate.js" type="text/javascript"></script>
                	<script src="/scripts/jquery.metadata.js" type="text/javascript"></script>
                </head>

                <body>
                <div align="center">
                <div id="skin" class="skin" align="center">
                <div id="main" class="main" align="center">

                		<div id="header_top" class="header_top" align="center"></div>
                		<div id="header_middle" class="header_middle" align="center">
                			<div id="header_middle_left" class="header_middle_left" ></div>
                			<div id="header_middle_middle" class="header_middle_middle">
                				<div id="header_middle_logo" class="header_middle_logo">
                					<div id="header_middle_logo_left" class="header_middle_logo_left">
                						<img src="/images/logo_gotovo.jpg" alt="vik" height="184px" width="420px"><br />
                					</div>
                					<div id="header_middle_logo_right" class="header_middle_logo_right">
                						<div id="header_middle_toptext" class="header_middle_toptext">Вторник, 8.Април.2025</div>
                						<div id="header_middle_toplinks" class="header_middle_toplinks">
                						<!--<a href="/">Начало</a>&nbsp;&nbsp;|&nbsp;&nbsp;-->
                						<a href="/info/infolist?tid=29&&id=56">Въпроси и отговори</a>&nbsp;&nbsp;|&nbsp;&nbsp;
                						<a href="/info/sitemap">Карта на сайта</a>
                						</div>
                						<div id="header_middle_topsearch" class="header_middle_topsearch">
                						<form id="searchform" name="searchform" action="/info/search" method="POST">
                						<input class="search_button" value="ТЪРСИ" type="submit"><input class="search_field" name="tarsi" id="tarsi" value="" type="text">
                						</form>
                						</div>
                					</div>
                					<div id="clr" class="clr"></div>
                				</div>
                				<div id="header_middle_menu" class="header_middle_menu">
                					<ul class="top_menu_ul">
                					<li class="top_menu_ul_li"><a href="/">НАЧАЛО</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=28&&title=ЗА НАС">ЗА НАС</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=43&&title=КЛИЕНТИ">КЛИЕНТИ</a></li>
                										<li class="top_menu_ul_li"><a href="/info/infolist?tid=29&&title=УСЛУГИ">УСЛУГИ</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=41&&title=ЦЕНИ">ЦЕНИ</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=44&&title=ЛИВ">ЛИВ</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=74&&title=ПСОВ">ПСОВ</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=76&&title=ПЛАЩАНИЯ">ПЛАЩАНИЯ</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=79&&title=ЗЛД">ЗЛД</a></li>
                										<li class="top_menu_ul_li"><a href="/info/?tid=33&&title=КОНТАКТИ">КОНТАКТИ</a></li>
                											<!--<li class="top_menu_ul_li"><a href="">ВОДОМЕРИ</a></li>-->
                					</ul>
                				</div>
                				
                			
                			
                			</div>
                			<div id="header_middle_right" class="header_middle_right" ></div>
                			<div id="clr" class="clr"></div>
                			
                			
                			
                			
                			
                		</div>
                		
                		<div class="info_main_text" align="center">
                		
                	<marquee style="color:#fc3e02; font: bold 13px  Arial, Helvetica, sans-serif; text-decoration:none; margin: 5px 20px 0px 20px;" behavior="scroll" direction="left">“В и К” ООД  гр.Търговище  уведомява своите клиенти , че от 01.09.2012 г. могат да заплащат консумираната питейна вода по електронен път ( E-Pay ) както и да получават електронни фактури (eFaktura)</marquee>
                		
                		
                		
                		
                		</div>
                		<div id="clr" class="clr"></div>



                		<div id="info_main" class="info_main" align="center">
                			<div id="info_main_left_menu" class="info_main_left_menu">
                			
                								<!--<div id="info_main_left_menu_head" class="info_main_left_menu_head"></div>-->
                				<div id="info_main_left_menu_info" class="info_main_left_menu_info">
                				<ul class="info_main_object_title">
                					<li class="info_main_object_title_li_blue"><span><a href="javascript: void(0);">Информация</a></span></li>
                				</ul>
                				<ul class="menu">

                										<li class="menu_li"><a href="/info/infolist?tid=15&&id=24&&title=Новини">Новини</a></li>
                										<li class="menu_li"><a href="/info/infolist?tid=15&&id=53&&title=Предстоящи дейности">Предстоящи дейности</a></li>
                										<li class="menu_li"><a href="/info/infolist?tid=15&&id=54&&title=Профил на купувача">Профил на купувача</a></li>
                										<li class="menu_li"><a href="/info/?tid=15&&id=84&&title=Проект по ПОС 2021-2027г.">Проект по ПОС 2021-2027г.</a></li>
                										<li class="menu_li"><a href="/info/?tid=15&&id=81&&title=График за отчитане показанията на абонати">График за отчитане показанията на абонати</a></li>
                					
                					<!--<li class="menu_li"><a href="">Работа във ВИК</a></li>-->
                					<div id="clear" class="clr"></div>
                				</ul>
                				</div>
                				<div id="info_main_left_menu_foot" class="info_main_left_menu_foot"></div>
                							
                			
                			
                				<div id="info_main_left_menu_info" class="info_main_left_menu_info">
                				<script type="text/javascript">
                				$(document).ready(function() {
                					$("#smetka1").validate();
                					$("#smetka2 ").validate({
                						messages: {
                							nomer: {
                								required: true,
                								maxlength: 10,
                								digits: true
                						}
                						}
                					});
                					});
                			</script>

                			<style type="text/css">
                			form label { width: 200px; }
                			form label.error{ width: 200px; color: #fc3e02;}\s
                			</style>
                				<ul class="info_main_object_title">
                					<li class="info_main_object_title_li_blue"><span><a href="javascript: void(0);">Проверка на сметка</a></span></li>
                				</ul>
                				<ul class="menu">
                					<li class="menu_li">Физически лица / Фирми</li>
                					<li class="menu_text">
                						<form id="smetka1" name="smetka1" action="/info/smetki" method="POST">
                							<!--<p><label for="egn">ЕГН *</label>
                							<input class="egn_field {required:true, minlength:10, maxlength:10, messages:{required:'Въведи!', minlength:'10 символа!',maxlength:'10 символа!'}}" name="egn" id="egn" value="" type="text"></p>-->
                							<p><label for="nomer">Абонатен номер *</label>
                							<input class="nomer_field {required:true, digits: true, maxlength:10, messages:{required:'Въведи!',maxlength:'10 символа!',digits:'Цифри!'}}" name="nomer" id="nomer" value="" type="text"></p>
                							<p><input class="nomer_button" value="ПОКАЖИ" type="submit"></p>
                						</form>
                					</li>
                					<div id="clear" class="clr"></div>
                				</ul>
                				</div>
                				<div id="info_main_left_menu_foot" class="info_main_left_menu_foot"></div>
                			
                			
                			
                			
                			
                			
                			
                			
                								<!--<div id="info_main_left_menu_head" class="info_main_left_menu_head"></div>-->
                				<div id="info_main_left_menu_info" class="info_main_left_menu_info">
                				<ul class="info_main_object_title">
                					<li class="info_main_object_title_li_clear"><span><a href="javascript: void(0);">Анкета</a></span></li>
                				</ul>
                				<ul class="menu">

                					<li class="menu_text">
                					<b>Интересувате ли се от електронни разплащания ?</b><br />
                					<form id="pollform" name="pollform" action="./" method="GET">
                					</li>

                																								<li class="menu_text">\s
                								
                														<input type="radio" name="otgovor" value="8" checked> <b>Нямам мнение</b>
                																					</li>
                																				<li class="menu_text">\s
                								
                																					<input type="radio" name="otgovor" value="9"> <b>Не се интересувам</b>
                														</li>
                																				<li class="menu_text">\s
                								
                																					<input type="radio" name="otgovor" value="10"> <b>Интересувам се</b>
                														</li>
                													\s
                					
                					<li class="menu_text">
                					<input type="button" onClick="pollform.submit();" class="nomer_button"  value="ГЛАСУВАЙ">
                					</form>
                					</li>
                					<li class="info_main_object_title_li_clear"><span><a href="javascript: void(0);">Резултати:</a></span></li>
                																											<li class="menu_text">\s
                								
                							Нямам мнение - 49.34%
                							</li>
                														<li class="menu_text">\s
                								
                							Не се интересувам - 2.76%
                							</li>
                														<li class="menu_text">\s
                								
                							Интересувам се - 47.9%
                							</li>
                							\s
                												<li class="menu_text">\s
                							Общо гласували: <b>16666</b>\s
                						</li>
                						<div id="clear" class="clr"></div>
                				</ul>
                				</div>
                				<div id="info_main_left_menu_foot" class="info_main_left_menu_foot"></div>
                				\s


                			</div>


                			<div id="info_main_info_big" class="info_main_info_big">

                			<ul class="info_main_object_title_big_more">
                				<li class="info_main_object_title_li_bigest_blue"><span><a href="javascript: void(0);">Предстоящи дейности</a></span></li>
                			</ul>
                			</div>
                			
                			<div id="info_main_info_big_text" class="info_main_info_big_text">
                						<ul class="info_main_object_title_big_text">

                				<li class="info_main_object_title_bigest_text_li_title"><b>Аварии и прекъсване на водоподаването   </b></li>
                				<li class="info_main_object_title_bigest_text_li"><span style="color: rgb(255, 0, 0);"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">Район Търговище</font></font></font></font></font></font></font></font></font></font></font></font></font></font></font></font></span>
                <div>
                <div><br />
                <div>гр.Търговище - кв.Въбел на 08.04.2025г. от 09:30 до 16:00 часа<br />
                <br />
                с.Лиляк на 08.04.2025г. от 11:00 до 17:00 часа<br />
                <br />
                <span style="color: rgb(255, 0, 0);">Район Омуртаг</span></div>
                <div>
                <div>
                <div>&#160;<br />
                <br />
                гр. Омуртаг - Осигурено водоподаване:<br />
                <br />
                05:30 часа - 22:30 часа<br />
                <br />
                &#160;</div>
                <div><br />
                <br />
                &#160;</div>
                <div><span style="color: rgb(255, 0, 0);"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">Район Антоново</font></font></font></font></font></font></font></font></font></font></font></font></font></font></font></font></span><br />
                <br />
                <br />
                <br />
                <span style="color: rgb(255, 0, 0);"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;"> Район Попово&#160;</font></font></font></font></font></font></font></font></font></font></font></font></font></font></font></font></span><br />
                &#160;<br />
                <br />
                гр.Попово /висока част/ на 04.04.2025г. от 15:00 до отстраняване на аварията<br />
                <br />
                <br />
                <br />
                &#160;</div>
                </div>
                </div>
                </div>
                </div><Br />
                					<!--<a href="javascript: void(0);">Подробно »</a>-->
                				</li>
                			</ul>
                			\s
                			
                			
                			<!--
                			<ul class="info_main_object_title_big">
                				<li class="info_main_object_title_li_big_orange"><span><a href="javascript: void(0);">Новини</a></span></li>
                			</ul>-->



                			</div>
                			

                			
                			
                			
                <!--
                			<div id="info_main_right_menu" class="info_main_right_menu">
                			
                				<div id="info_main_left_menu_info" class="info_main_left_menu_info">
                				<ul class="info_main_object_title">
                					<li class="info_main_object_title_li_blue"><span><a href="javascript: void(0);">Проверка на сметка</a></span></li>
                				</ul>
                				<ul class="menu">
                					<li class="menu_li">Физически лица</li>
                					<li class="menu_text">
                						<form id="smetka1" name="smetka1" action="../info/check" method="GET">
                							<p><label for="egn">ЕГН *</label>
                							<input class="egn_field {required:true, minlength:10, maxlength:10, messages:{required:'Въведи!', minlength:'10 символа!',maxlength:'10 символа!'}}" name="egn" id="egn" value="" type="text"></p>
                							<p><label for="nomer">Абонатен номер *</label>
                							<input class="nomer_field {required:true, minlength:10, maxlength:10, messages:{required:'Въведи!', minlength:'10 символа!',maxlength:'10 символа!'}}" name="nomer" id="nomer" value="" type="text"></p>
                							<p><input class="nomer_button" value="ПОКАЖИ" type="submit"></p>
                						</form>
                					</li>
                					<div id="clear" class="clr"></div>
                				</ul>
                				</div>
                				<div id="info_main_left_menu_foot" class="info_main_left_menu_foot"></div>

                				<div id="info_main_left_menu_info" class="info_main_left_menu_info">
                				<ul class="info_main_object_title">
                					<li class="info_main_object_title_li_blue"><span><a href="javascript: void(0);">Проверка на сметка</a></span></li>
                				</ul>
                				<ul class="menu">

                					<li class="menu_li">Фирми</li>
                					<li class="menu_text">
                						<form id="smetka2" name="smetka2" action="../info/check" method="GET">
                							<p><label for="bul">БУЛСТАТ *</label>
                							<input class="egn_field {required:true, minlength:10, maxlength:10, messages:{required:'Въведи!', minlength:'10 символа!',maxlength:'10 символа!'}}" name="bul" id="bul" value="" type="text">
                							<p><label for="nomer">Абонатен номер *</label>
                							<input class="nomer_field {required:true, minlength:10, maxlength:10, messages:{required:'Въведи!', minlength:'10 символа!',maxlength:'10 символа!'}}" name="nomer" id="nomer" value="" type="text"></p>
                							<p><input class="nomer_button" value="ПОКАЖИ" type="submit"></p>
                						</form>
                					</li>
                					<div id="clear" class="clr"></div>
                				</ul>
                				</div>
                				<div id="info_main_left_menu_foot" class="info_main_left_menu_foot"></div>
                			
                			
                				
                				<div id="info_main_left_menu_info" class="info_main_left_menu_info">
                				<ul class="info_main_object_title">
                					<li class="info_main_object_title_li_clear"><span><a href="javascript: void(0);">Новини</a></span></li>
                				</ul>
                				<ul class="menu">
                					<li class="menu_li"><a href="">Факти и цифри</a></li>
                					<li class="menu_li"><a href="">Нормативна база</a></li>
                					<li class="menu_li"><a href="">Любопитно и полезно</a></li>
                					<li class="menu_li"><a href="">Екология</a></li>
                					<li class="menu_li"><a href="">Работа във ВИК</a></li>
                					<li class="menu_li"><a href="">Факти и цифри</a></li>
                					<li class="menu_li"><a href="">Нормативна база</a></li>
                					<li class="menu_li"><a href="">Любопитно и полезно</a></li>
                					<li class="menu_li"><a href="">Екология</a></li>
                					<li class="menu_li"><a href="">Работа във ВИК</a></li>
                					<div id="clear" class="clr"></div>
                				</ul>
                				</div>
                				<div id="info_main_left_menu_foot" class="info_main_left_menu_foot"></div>
                			
                			
                			
                			
                			
                			
                			
                			
                			</div>-->
                		</div>

                <div id="info_footer_info" class="info_footer_info" align="center">
                						<br /><a href="/">НАЧАЛО</a>
                																	&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=28&&title=ЗА НАС">ЗА НАС</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=43&&title=КЛИЕНТИ">КЛИЕНТИ</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/infolist?tid=29&&title=УСЛУГИ">УСЛУГИ</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=41&&title=ЦЕНИ">ЦЕНИ</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=44&&title=ЛИВ">ЛИВ</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=74&&title=ПСОВ">ПСОВ</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=76&&title=ПЛАЩАНИЯ">ПЛАЩАНИЯ</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=79&&title=ЗЛД">ЗЛД</a>
                										&nbsp;&nbsp;|&nbsp;&nbsp;<a href="/info/?tid=33&&title=КОНТАКТИ">КОНТАКТИ</a>
                											
                						<br />"Водоснабдяване и канализация" ООД - Търговище
                		</div>
                		<div id="info_footer" class="info_footer" align="center"></div>




                </div>
                </div>
                </div>
                </body>
                </html>

                        """;
        when(vikOutageClient.getOutage("15", "53", "50")).thenReturn(mockHtml);

        // Call the method under test
        List<VikOutage> result = vikOutageService.fetchAndParseVikOutage();
        // Assertions
        assertNotNull(result);
        System.out.println(result);
        assertEquals(2, result.size()); // Expecting 3 outages based on the modified service processing all li

        // Assertions for the first outage (from Район Търговище)
        VikOutage firstOutage = result.get(0);
        assertEquals("08.04.2025", firstOutage.getDate());
        assertEquals("09:30", firstOutage.getStartTime());
        assertEquals("16:00", firstOutage.getEndTime());
        assertEquals("гр.Търговище  кв.Въбел", firstOutage.getDescription());

        // Assertions for the second outage (from Район Търговище)
        VikOutage secondOutage = result.get(1);
        assertEquals("08.04.2025", secondOutage.getDate());
        assertEquals("11:00", secondOutage.getStartTime());
        assertEquals("17:00", secondOutage.getEndTime());
        assertEquals("с.Лиляк", secondOutage.getDescription());

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
            <ul class="info_main_object_title_big_text">
                <li class="info_main_object_title_bigest_text_li_title"><b>Аварии и прекъсване на водоподаването&nbsp; &nbsp;</b></li>
                <li class="info_main_object_title_bigest_text_li">
                    <div>
                        <div><br>
                            <div>Invalid data entry<br><br></div>
                        </div>
                    </div>
                </li>
            </ul>
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