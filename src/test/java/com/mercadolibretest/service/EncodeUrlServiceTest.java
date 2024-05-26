package com.mercadolibretest.service;

import com.mercadolibretest.MercadolibretestApplication;
import com.mercadolibretest.constants.MercadoLibreTestConstants;
import com.mercadolibretest.exceptionhandler.custom.UrlException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MercadolibretestApplication.class)
public class EncodeUrlServiceTest {

    private static EncodeUrlService encodeUrlService;

    @BeforeAll
    public static void setUp() {
        encodeUrlService = new EncodeUrlService();
    }

    @Test
    public void getEncodedUrlShouldBeRightLongSize() {
        String url = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";

        String encodedUrl = encodeUrlService.getEncodedUrl(url);

        assertEquals(encodedUrl.length(), MercadoLibreTestConstants.URL_MAX_SIZE);
    }

    @Test
    public void getEncodedUrlShouldBeInvalidUrlException() {
        String url = "djshfsdkjfh";

        UrlException urlException = assertThrows(UrlException.class,
                () -> encodeUrlService.getEncodedUrl(url)
        );

        assertEquals("Lo sentimos! La URL que se esta intentando registrar no es valida!", urlException.getMessage());
    }

}
