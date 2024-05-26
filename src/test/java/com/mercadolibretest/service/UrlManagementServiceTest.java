package com.mercadolibretest.service;

import com.mercadolibretest.MercadolibretestApplication;
import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.dto.UrlUpdateDataRequest;
import com.mercadolibretest.exceptionhandler.custom.DateCustomException;
import com.mercadolibretest.exceptionhandler.custom.UrlConfigActionException;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.repository.UrlManagementRepository;
import com.mercadolibretest.utils.Utils;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MercadolibretestApplication.class)
public class UrlManagementServiceTest {

    public static final EncodeUrlService ENCODE_URL_SERVICE = mock(EncodeUrlService.class);
    public static final UrlManagementRepository URL_MANAGEMENT_REPOSITORY = mock(UrlManagementRepository.class);

    public static UrlManagementService urlManagementService;

    @BeforeAll
    public static void setUp() {
        urlManagementService = new UrlManagementService(ENCODE_URL_SERVICE, URL_MANAGEMENT_REPOSITORY);
    }

    @AfterEach
    public void afterEachTest() {
        clearInvocations(ENCODE_URL_SERVICE);
        clearInvocations(URL_MANAGEMENT_REPOSITORY);
    }

    @Test
    public void createShortUrlShouldBeSuccess() {
        String shortUrl = "XXJJ3EA";
        UrlDataRequest urlDataRequest = this.getUrlDataRequest();

        when(ENCODE_URL_SERVICE.getEncodedUrl(urlDataRequest.getLongUrl())).thenReturn(shortUrl);
        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(null);

        UrlDataResponse urlDataResponse = urlManagementService.createShortUrl(urlDataRequest);

        assertEquals(urlDataResponse.getLongUrl(), urlDataRequest.getLongUrl());
        assertEquals(urlDataResponse.getExpiredAt(), urlDataRequest.getExpiredAt());
        assertEquals(urlDataResponse.getIsAvailable(), urlDataRequest.getIsAvailable());

        verify(ENCODE_URL_SERVICE, times(1)).getEncodedUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(1)).save(isA(UrlEntity.class));
    }

    @Test
    public void createShortUrlShouldBeUrlExistException() {
        String shortUrl = "XXJJ3EA";
        UrlDataRequest urlDataRequest = this.getUrlDataRequest();

        when(ENCODE_URL_SERVICE.getEncodedUrl(urlDataRequest.getLongUrl())).thenReturn(shortUrl);
        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(this.getUrlEntityToDataBaseMock(urlDataRequest, shortUrl));

        UrlConfigActionException urlConfigActionException = assertThrows(UrlConfigActionException.class,
                () -> urlManagementService.createShortUrl(urlDataRequest)
        );

        assertEquals("Lo sentimos! La url que intentas registrar ya existe!", urlConfigActionException.getMessage());

        verify(ENCODE_URL_SERVICE, times(1)).getEncodedUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(0)).save(isA(UrlEntity.class));
    }

    @Test
    public void createShortUrlShouldBeDateExpiredException() {
        String shortUrl = "XXJJ3EA";
        UrlDataRequest urlDataRequest = this.getUrlDataRequest();
        urlDataRequest.setExpiredAt("2000-06-17 23:59:00");

        when(ENCODE_URL_SERVICE.getEncodedUrl(urlDataRequest.getLongUrl())).thenReturn(shortUrl);
        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(null);

        DateCustomException dateCustomException = assertThrows(DateCustomException.class,
                () -> urlManagementService.createShortUrl(urlDataRequest)
        );

        assertEquals("La fecha y hora de expiracion no puede ser menor a la fecha y hora actual!", dateCustomException.getMessage());

        verify(ENCODE_URL_SERVICE, times(1)).getEncodedUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(0)).save(isA(UrlEntity.class));
    }

    @Test
    public void getLongUrlByShortUrlShouldBeLongUrlSuccess(){
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(this.getUrlEntityFromDataBaseMock(shortUrl));

        UrlDataResponse urlDataResponse = urlManagementService.getLongUrlByShortUrl(shortUrl);

        assertEquals(shortUrl, urlDataResponse.getShortUrl());
        assertEquals("https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f", urlDataResponse.getLongUrl());

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
    }

    @Test
    public void getLongUrlByShortUrlShouldBeShortUrlSuccess(){
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        when(ENCODE_URL_SERVICE.getEncodedUrl(longUrl)).thenReturn(shortUrl);
        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(this.getUrlEntityFromDataBaseMock(shortUrl));

        UrlDataResponse urlDataResponse = urlManagementService.getLongUrlByShortUrl(longUrl);

        assertEquals(shortUrl, urlDataResponse.getShortUrl());
        assertEquals(longUrl, urlDataResponse.getLongUrl());

        verify(ENCODE_URL_SERVICE, times(1)).getEncodedUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
    }

    @Test
    public void getLongUrlByShortUrlShouldBeUrlNotAvailableException(){
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(null);

        UrlConfigActionException urlConfigActionException = assertThrows(UrlConfigActionException.class,
                () -> urlManagementService.getLongUrlByShortUrl(shortUrl)
        );

        assertEquals("Lo sentimos! La url que intentas acceder o ver, ya no esta disponible o no existe!", urlConfigActionException.getMessage());

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
    }

    @Test
    public void showUrlShouldBeShortUrlSuccess(){
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        UrlDataResponse urlDataResponse = this.getUrlDataResponse();
        urlDataResponse.setLongUrl(longUrl);
        urlDataResponse.setShortUrl(shortUrl);

        String urlToShow = urlManagementService.showUrl(longUrl, urlDataResponse);

        assertEquals(shortUrl, urlToShow);
    }

    @Test
    public void showUrlShouldBeLongUrlSuccess(){
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        UrlDataResponse urlDataResponse = this.getUrlDataResponse();
        urlDataResponse.setLongUrl(longUrl);
        urlDataResponse.setShortUrl(shortUrl);

        String urlToShow = urlManagementService.showUrl(shortUrl, urlDataResponse);

        assertEquals(longUrl, urlToShow);
    }

    @Test
    public void redirectToLongUrlByShortUrlShouldBeRedirectSuccess(){
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        UrlDataResponse urlDataResponse = this.getUrlDataResponse();
        urlDataResponse.setLongUrl(longUrl);
        urlDataResponse.setShortUrl(shortUrl);

        HttpServletResponse httpServletResponse = new MockHttpServletResponse();

        urlManagementService.redirectToLongUrlByShortUrl(urlDataResponse, httpServletResponse);

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).updateUrlEntity(isA(BigInteger.class));
    }

    @Test
    public void redirectToLongUrlByShortUrlShouldBeUrlUrlExpired(){
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        UrlDataResponse urlDataResponse = this.getUrlDataResponse();
        urlDataResponse.setLongUrl(longUrl);
        urlDataResponse.setShortUrl(shortUrl);
        urlDataResponse.setExpiredAt("2000-06-17 23:59:00");

        HttpServletResponse httpServletResponse = new MockHttpServletResponse();

        UrlConfigActionException urlConfigActionException = assertThrows(UrlConfigActionException.class,
                () -> urlManagementService.redirectToLongUrlByShortUrl(urlDataResponse, httpServletResponse)
        );

        assertEquals("Lo sentimos! La fecha de acceso a la Url ha expirado!", urlConfigActionException.getMessage());

        verify(URL_MANAGEMENT_REPOSITORY, times(0)).updateUrlEntity(isA(BigInteger.class));
    }

    @Test
    public void redirectToLongUrlByShortUrlShouldBeNotAvailable(){
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        UrlDataResponse urlDataResponse = this.getUrlDataResponse();
        urlDataResponse.setLongUrl(longUrl);
        urlDataResponse.setShortUrl(shortUrl);
        urlDataResponse.setIsAvailable(Boolean.FALSE);

        HttpServletResponse httpServletResponse = new MockHttpServletResponse();

        UrlConfigActionException urlConfigActionException = assertThrows(UrlConfigActionException.class,
                () -> urlManagementService.redirectToLongUrlByShortUrl(urlDataResponse, httpServletResponse)
        );

        assertEquals("Lo sentimos! La url que intentas acceder o ver, ya no esta disponible o no existe!", urlConfigActionException.getMessage());

        verify(URL_MANAGEMENT_REPOSITORY, times(0)).updateUrlEntity(isA(BigInteger.class));
    }

    @Test
    public void deleteUrlConfigByShortUrlShouldBeSuccess() {
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(this.getUrlEntityFromDataBaseMock(shortUrl));

        UrlDataResponse urlDataResponse = urlManagementService.deleteUrlConfigByShortUrl(shortUrl);

        assertEquals(shortUrl, urlDataResponse.getShortUrl());

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).deleteById(isA(BigInteger.class));
    }

    @Test
    public void deleteUrlConfigByShortUrlShouldBeUrlNotExists() {
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(null);

        UrlConfigActionException urlConfigActionException = assertThrows(UrlConfigActionException.class,
                () -> urlManagementService.deleteUrlConfigByShortUrl(shortUrl)
        );

        assertEquals("La configuracion de URL que intenta actualizar o eliminar no existe!", urlConfigActionException.getMessage());
    }

    @Test
    public void updateUrlConfigByShortUrlShouldBeSuccess() {
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(this.getUrlEntityFromDataBaseMock(shortUrl));

        UrlUpdateDataRequest urlUpdateDataRequest = this.getUrlUpdateDataRequest();
        UrlDataResponse urlDataResponse = urlManagementService.updateUrlConfigByShortUrl(shortUrl, urlUpdateDataRequest);

        assertEquals(urlUpdateDataRequest.getIsAvailable(), urlDataResponse.getIsAvailable());
        assertEquals(urlUpdateDataRequest.getExpiredAt(), urlDataResponse.getExpiredAt());

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(1)).save(isA(UrlEntity.class));
    }

    @Test
    public void updateUrlConfigByShortUrlShouldBeUrlNotExist() {
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(null);

        UrlUpdateDataRequest urlUpdateDataRequest = this.getUrlUpdateDataRequest();
        UrlConfigActionException urlConfigActionException = assertThrows(UrlConfigActionException.class,
                () -> urlManagementService.updateUrlConfigByShortUrl(shortUrl, urlUpdateDataRequest)
        );

        assertEquals("La configuracion de URL que intenta actualizar o eliminar no existe!", urlConfigActionException.getMessage());

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(0)).save(isA(UrlEntity.class));
    }

    @Test
    public void updateUrlConfigByShortUrlShouldBeDateExpired() {
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(this.getUrlEntityFromDataBaseMock(shortUrl));

        UrlUpdateDataRequest urlUpdateDataRequest = this.getUrlUpdateDataRequest();
        urlUpdateDataRequest.setExpiredAt("2000-06-17 23:59:00");
        DateCustomException dateCustomException = assertThrows(DateCustomException.class,
                () -> urlManagementService.updateUrlConfigByShortUrl(shortUrl, urlUpdateDataRequest)
        );

        assertEquals("La fecha y hora de expiracion no puede ser menor a la fecha y hora actual!", dateCustomException.getMessage());

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(0)).save(isA(UrlEntity.class));
    }

    @Test
    public void updateUrlConfigByShortUrlShouldBeDateNull() {
        String shortUrl = "XXJJ3EA";

        when(URL_MANAGEMENT_REPOSITORY.findByShortUrl(shortUrl)).thenReturn(this.getUrlEntityFromDataBaseMock(shortUrl));

        UrlUpdateDataRequest urlUpdateDataRequest = this.getUrlUpdateDataRequest();
        urlUpdateDataRequest.setExpiredAt(null);
        DateCustomException dateCustomException = assertThrows(DateCustomException.class,
                () -> urlManagementService.updateUrlConfigByShortUrl(shortUrl, urlUpdateDataRequest)
        );

        assertEquals("La fecha y hora de expiracion no puede ser menor a la fecha y hora actual!", dateCustomException.getMessage());

        verify(URL_MANAGEMENT_REPOSITORY, times(1)).findByShortUrl(anyString());
        verify(URL_MANAGEMENT_REPOSITORY, times(0)).save(isA(UrlEntity.class));
    }

    private UrlDataRequest getUrlDataRequest() {
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";

        UrlDataRequest urlDataRequest = new UrlDataRequest();
        urlDataRequest.setLongUrl(longUrl);
        urlDataRequest.setExpiredAt("2030-06-17 23:59:00");
        urlDataRequest.setIsAvailable(Boolean.TRUE);
        return urlDataRequest;
    }

    private UrlUpdateDataRequest getUrlUpdateDataRequest() {
        UrlUpdateDataRequest urlUpdateDataRequest = new UrlUpdateDataRequest();
        urlUpdateDataRequest.setIsAvailable(Boolean.TRUE);
        urlUpdateDataRequest.setExpiredAt("2030-06-17 23:59:00");
        return urlUpdateDataRequest;
    }

    private UrlDataResponse getUrlDataResponse() {
        String longUrl = "https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f";
        String shortUrl = "XXJJ3EA";

        UrlDataResponse urlDataResponse = new UrlDataResponse();
        urlDataResponse.setId("1");
        urlDataResponse.setLongUrl(longUrl);
        urlDataResponse.setShortUrl(shortUrl);
        urlDataResponse.setIsAvailable(Boolean.TRUE);
        urlDataResponse.setExpiredAt("2030-06-17 23:59:00");
        return urlDataResponse;
    }

    private UrlEntity getUrlEntityToDataBaseMock(UrlDataRequest urlDataRequest, String shortUrl) {
        UrlEntity urlEntity = UrlEntity.getUrlEntityBuilder(urlDataRequest, shortUrl);
        urlEntity.setId(BigInteger.ONE);
        return  urlEntity;
    }

    private UrlEntity getUrlEntityFromDataBaseMock(String shortUrl) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setId(BigInteger.ONE);
        urlEntity.setUrlKeyId(UUID.randomUUID().toString());
        urlEntity.setLongUrl("https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f");
        urlEntity.setShortUrl(shortUrl);
        urlEntity.setCreatedAt(new Date());
        urlEntity.setExpiredAt(new Date());
        urlEntity.setLastVisitedAt(new Date());
        urlEntity.setIsAvailable(Boolean.TRUE);
        urlEntity.setVisitsNumber(BigInteger.ZERO);
        urlEntity.setUpdatedAt(new Date());
        return urlEntity;
    }

}
