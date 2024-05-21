package com.mercadolibretest.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibretest.MercadolibretestApplication;
import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.dto.UrlUpdateDataRequest;
import com.mercadolibretest.utils.Utils;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MercadolibretestApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class UrlManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createShortUrlShouldBeHttpStatus201() throws Exception {
        String expectedJson = Utils.getFileContentResource("testConfigs/controller/shortUrlCreatedHttpStatus201Expected.json");
        UrlDataResponse urlDataResponseExpected = (UrlDataResponse)Utils.toObjectFromJSON(expectedJson, UrlDataResponse.class);
        this.deleteAuxForTests(urlDataResponseExpected.getShortUrl());

        String uri = "/url-management/create-short-url";
        UrlDataRequest urlDataRequest = new UrlDataRequest();
        urlDataRequest.setLongUrl("https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f");
        urlDataRequest.setExpiredAt("2024-06-17 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.TRUE);

        String inputJson = Utils.toJSONFromObject(urlDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)
                ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        UrlDataResponse urlDataResponseResult = (UrlDataResponse)Utils.toObjectFromJSON(mvcResult.getResponse().getContentAsString(), UrlDataResponse.class);

        assertEquals(urlDataResponseResult.getShortUrl(), urlDataResponseExpected.getShortUrl());
    }

    @Test
    public void getShortUrlByLongUrlShouldBeHttpStatus200() throws Exception {
        String shortUrl = "XXJJ3EA";
        this.deleteAuxForTests(shortUrl);
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/get-url";

        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
                        .param("url", longUrl)
            ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String urlResult = mvcResult.getResponse().getContentAsString();
        String urlExpected = shortUrl;

        assertEquals(urlResult, urlExpected);

    }

    @Test
    public void getLongUrlByShortUrlShouldBeHttpStatus200() throws Exception {
        String shortUrl = "XXJJ3EA";
        this.deleteAuxForTests(shortUrl);
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/get-url";

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
                        .param("url", shortUrl)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String urlResult = mvcResult.getResponse().getContentAsString();
        String urlExpected = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";

        assertEquals(urlResult, urlExpected);

    }

    @Test
    public void getUrlInfoByUrlShouldBeHttpStatus200() throws Exception {
        String shortUrl = "XXJJ3EA";
        this.deleteAuxForTests(shortUrl);
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/get-url-info";

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
                        .param("url", shortUrl)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        UrlDataResponse urlDataResponseResult = (UrlDataResponse)Utils.toObjectFromJSON(mvcResult.getResponse().getContentAsString(), UrlDataResponse.class);

        assertEquals(urlDataResponseResult.getShortUrl(), shortUrl);
    }

    @Test
    public void redirectToLongUrlByShortUrlShouldBeHttpStatus302() throws Exception {
        String shortUrl = "XXJJ3EA";
        this.deleteAuxForTests(shortUrl);
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/" + shortUrl;

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(302, status);

    }

    @Test
    public void deleteUrlConfigByShortUrlShouldBeHttpStatus302() throws Exception {
        this.createShortUrlShouldBeHttpStatus201();

        String shortUrl = "XXJJ3EA";
        String uri = "/url-management/" + shortUrl;

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(uri)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String result = mvcResult.getResponse().getContentAsString();

        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String expected = String.format("Configuracion de URL: '%s' ha sido eliminada exitosamente", longUrl);

        assertEquals(result, expected);

    }

    @Test
    public void updateUrlConfigByShortUrlShouldBeHttpStatus302() throws Exception {
        String shortUrl = "XXJJ3EA";
        this.deleteAuxForTests(shortUrl);
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/" + shortUrl;
        UrlUpdateDataRequest urlUpdateDataRequest = new UrlUpdateDataRequest();
        urlUpdateDataRequest.setExpiredAt("2024-06-29 23:59:00");
        urlUpdateDataRequest.setIsAvailable(Boolean.FALSE);

        String inputJson = Utils.toJSONFromObject(urlUpdateDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        UrlDataResponse urlDataResponseResult = (UrlDataResponse)Utils.toObjectFromJSON(mvcResult.getResponse().getContentAsString(), UrlDataResponse.class);

        assertFalse(urlDataResponseResult.getIsAvailable());
        assertEquals(urlDataResponseResult.getExpiredAt(), "2024-06-29 23:59:00");
        assertEquals(urlDataResponseResult.getUpdatedAt(), Utils.dateToStringDateFormatter(new Date()));

    }

    // EXCEPTIONS TEST
    @Test
    public void createShortUrlShouldBeBadRequest() throws Exception {
        String uri = "/url-management/create-short-url";
        UrlDataRequest urlDataRequest = new UrlDataRequest();
        //urlDataRequest.setLongUrl("https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f");
        urlDataRequest.setExpiredAt("2024-06-17 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.TRUE);

        String inputJson = Utils.toJSONFromObject(urlDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    public void createShortUrlShouldBeUrlException() throws Exception {
        String uri = "/url-management/create-short-url";
        UrlDataRequest urlDataRequest = new UrlDataRequest();
        urlDataRequest.setLongUrl("jskdhdjsghf");
        urlDataRequest.setExpiredAt("2024-06-17 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.TRUE);

        String inputJson = Utils.toJSONFromObject(urlDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();

        String expectedMessage = "Lo sentimos! La URL que se esta intentando registrar no es valida!";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);
    }

    @Test
    public void createShortUrlShouldBeUrlExist() throws Exception {
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/create-short-url";
        UrlDataRequest urlDataRequest = new UrlDataRequest();
        urlDataRequest.setLongUrl("https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f");
        urlDataRequest.setExpiredAt("2024-06-29 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.TRUE);

        String inputJson = Utils.toJSONFromObject(urlDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(202, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();

        String expectedMessage = "Lo sentimos! La url que intentas registrar ya existe!";
        Map<String, String> expectedResult = new HashMap();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);

    }

    @Test
    public void createShortUrlShouldBeInvalidDateExpiredAt() throws Exception {
        String shortUrl = "XXJJ3EA";
        this.deleteAuxForTests(shortUrl);
        String uri = "/url-management/create-short-url";
        UrlDataRequest urlDataRequest = new UrlDataRequest();
        urlDataRequest.setLongUrl("https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f");
        urlDataRequest.setExpiredAt("2024-01-29 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.TRUE);

        String inputJson = Utils.toJSONFromObject(urlDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();

        String expectedMessage = "La fecha y hora de expiracion no puede ser menor a la fecha y hora actual!";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);

    }

    @Test
    public void getShortUrlByLongUrlShouldBeUrlNotAvailable() throws Exception {
        String shortUrl = "XXJJZZ";
        this.deleteAuxForTests(shortUrl);
        String uri = "/url-management/get-url";
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
                        .param("url", shortUrl)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(202, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();

        String expectedMessage = "Lo sentimos! La url que intentas acceder o ver, ya no esta disponible o no existe!";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);
    }

    @Test
    public void redirectToLongUrlByShortUrlShouldBeUrlNotAvailable() throws Exception {
        String shortUrl = "TTZPEBW";
        this.deleteAuxForTests(shortUrl);
        this.createShortUrlNotAvailableForTest();

        String uri = "/url-management/" + shortUrl;

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(202, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();
        String expectedMessage = "Lo sentimos! La url que intentas acceder o ver, ya no esta disponible o no existe!";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);
    }

    @Test
    public void deleteUrlConfigByShortUrlShouldUrlNotExist() throws Exception {
        String shortUrl = "XRRBBBS";
        String uri = "/url-management/" + shortUrl;

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(uri)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(202, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();
        String expectedMessage = "La configuracion de URL que intenta actualizar o eliminar no existe!";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);
    }

    @Test
    public void updateUrlConfigByShortUrlShouldBeUrlNotExist() throws Exception {
        String shortUrl = "HHAGGTT";
        String uri = "/url-management/" + shortUrl;
        UrlUpdateDataRequest urlUpdateDataRequest = new UrlUpdateDataRequest();
        urlUpdateDataRequest.setExpiredAt("2024-06-29 23:59:00");
        urlUpdateDataRequest.setIsAvailable(Boolean.FALSE);

        String inputJson = Utils.toJSONFromObject(urlUpdateDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(202, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();
        String expectedMessage = "La configuracion de URL que intenta actualizar o eliminar no existe!";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);
    }

    @Test
    public void updateUrlConfigByShortUrlShouldBeNotValidExpiredAtDate() throws Exception {
        String shortUrl = "XXJJ3EA";
        this.deleteAuxForTests(shortUrl);
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/" + shortUrl;
        UrlUpdateDataRequest urlUpdateDataRequest = new UrlUpdateDataRequest();
        urlUpdateDataRequest.setExpiredAt("2000-06-29 23:59:00");
        urlUpdateDataRequest.setIsAvailable(Boolean.FALSE);

        String inputJson = Utils.toJSONFromObject(urlUpdateDataRequest);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        String resultMessage = mvcResult.getResponse().getContentAsString();
        String expectedMessage = "La fecha y hora de expiracion no puede ser menor a la fecha y hora actual!";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("mensaje", expectedMessage);

        assertEquals(Utils.toJSONFromObject(expectedResult), resultMessage);
    }

    private void deleteAuxForTests(String shortUrl) throws Exception {
        String uri = "/url-management/" + shortUrl;
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
    }

    private void createShortUrlNotAvailableForTest() throws Exception {
        String uri = "/url-management/create-short-url";
        UrlDataRequest urlDataRequest = new UrlDataRequest();
        urlDataRequest.setLongUrl("https://www.youtube.com/watch?v=KRqgjpiwrY8");
        urlDataRequest.setExpiredAt("2028-06-17 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.FALSE);

        String inputJson = Utils.toJSONFromObject(urlDataRequest);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();
    }


}
