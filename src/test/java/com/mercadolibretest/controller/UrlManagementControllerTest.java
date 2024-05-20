package com.mercadolibretest.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibretest.MercadolibretestApplication;
import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.utils.Utils;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MercadolibretestApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class UrlManagementControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void createShortUrlShouldBeHttpStatus201() throws Exception {
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

        String expectedJson = Utils.getFileContentResource("testConfigs/controller/shortUrlCreatedHttpStatus201Expected.json");
        UrlDataResponse urlDataResponseExpected = (UrlDataResponse)Utils.toObjectFromJSON(expectedJson, UrlDataResponse.class);

        assertEquals(urlDataResponseResult.getShortUrl(), urlDataResponseExpected.getShortUrl());
    }

    @Test
    public void getShortUrlByLongUrlShouldBeHttpStatus200() throws Exception {
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/get-url";

        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
                        .param("url", longUrl)
            ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String urlResult = mvcResult.getResponse().getContentAsString();
        String urlExpected = "XXJJ3EA";

        assertEquals(urlResult, urlExpected);

    }

    @Test
    public void getLongUrlByShortUrlShouldBeHttpStatus200() throws Exception {
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/get-url";

        String shortUrl = "XXJJ3EA";

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
        this.createShortUrlShouldBeHttpStatus201();

        String uri = "/url-management/get-url-info";

        String shortUrl = "XXJJ3EA";

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
        this.createShortUrlShouldBeHttpStatus201();

        String shortUrl = "XXJJ3EA";
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
        this.createShortUrlShouldBeHttpStatus201();

        String shortUrl = "XXJJ3EA";
        String uri = "/url-management/" + shortUrl;
        UrlDataRequest urlDataRequest = new UrlDataRequest();
        urlDataRequest.setExpiredAt("2024-06-29 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.FALSE);

        String inputJson = Utils.toJSONFromObject(urlDataRequest);
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




}
