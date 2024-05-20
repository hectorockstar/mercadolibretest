package com.mercadolibretest.controller;

import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.service.UrlManegementService;
import com.mercadolibretest.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url-management")
public class UrlManagementController {

    private final UrlManegementService urlManegementService;

    @Autowired
    public UrlManagementController(UrlManegementService urlManegementService) {
        this.urlManegementService = urlManegementService;
    }

    @Operation(summary = "Endpoint acortador de URL", description = "Este servicio crea una configuracion de URL corta cuando se le entrega una URL normal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "La creacion de la configuracion de la URL fue exitosa", content = @Content(schema = @Schema(implementation = UrlDataRequest.class))),
            @ApiResponse(responseCode = "202", description = "La URL que intentas registar ya existe"),
            @ApiResponse(responseCode = "400", description = "La informacion que proporcionas no es valida")
    })
    @PostMapping("/create-short-url")
    @ResponseBody
    public ResponseEntity<Object> createShortUrl(
            @Schema(
                name =  "urlDataRequest",
                description  = "Datos de prueba",
                example = "{    \"longUrl\": \"https://www.mercadolibre.cl/apple-iphone-11-128-gb-blanco-distribuidor-autorizado/p/MLC1015149568?pdp_filters=category:MLC1055#searchVariation=MLC1015149568&position=2&search_layout=stack&type=product&tracking_id=4681ed81-13fa-4db5-bf85-47b8d5d0986f\",    \"expiredAt\": \"2024-06-17 23:59:00\",    \"isAvailable\": \"true\"}"
            ) @Valid @RequestBody UrlDataRequest urlDataRequest
    ) {
        UrlDataResponse urlDataResponse = urlManegementService.createShortUrl(urlDataRequest);
        return new ResponseEntity<>(Utils.toJSONFromObject(urlDataResponse), HttpStatus.CREATED);
    }

    @Operation(summary = "Endpoint de consulta de URL corta con una URL larga y viceversa", description = "Puedes consultar una URL corta brindando una URL larga y al reves. Puedes consultar una URL larga brindando una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La Url que buscas fue encontrada satisfactoriamente"),
            @ApiResponse(responseCode = "202", description = "La Url que obtener no existe")
    })
    @GetMapping("/get-url")
    @ResponseBody
    public ResponseEntity<Object> getUrlByUrl(
            @Parameter(
                    name =  "url",
                    description  = "URL corta de ejemplo",
                    example = "XXJJ3EA",
                    required = true
            ) @RequestParam("url") String url
    ) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(url);
        String urlResult = urlManegementService.showUrl(url, urlDataResponse);
        return ResponseEntity.ok(urlResult);
    }

    @Operation(summary = "Endpoint de consulta de informacion completa de una URL corta con una URL larga y viceversa", description = "Puedes consultar la informacion completa de una URL corta brindando una URL larga y al reves. Puedes consultar la informacion completa de una URL larga brindando una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La Url que buscas fue encontrada satisfactoriamente"),
            @ApiResponse(responseCode = "202", description = "La Url que obtener no existe")
    })
    @GetMapping("/get-url-info")
    @ResponseBody
    public ResponseEntity<Object> getUrlInfoByUrl(
            @Parameter(
                    name =  "url",
                    description  = "URL corta de ejemplo",
                    example = "XXJJ3EA",
                    required = true
            ) @RequestParam("url") String url
    ) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(url);
        return ResponseEntity.ok(Utils.toJSONFromObject(urlDataResponse));
    }

    @Operation(summary = "Servicio de redireccionamiento a una URL desde una URL corta", description = "cuando proporcionas una URL corta accederas a la URL original")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Fuiste redireccionado exitosamente"),
            @ApiResponse(responseCode = "202", description = "La Url que intentas acceder no existe o no esta disponible")
    })
    @GetMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Object> redirectToLongUrlByShortUrl(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @Parameter(
                    name =  "shortUrl",
                    description  = "URL corta para reedireccionar de ejemplo",
                    example = "XXJJ3EA",
                    required = true
            ) @PathVariable String shortUrl
    ) {
        UrlDataResponse urlDataResponse = urlManegementService.getLongUrlByShortUrl(shortUrl);
        urlManegementService.redirectToLonglUrlbyShortUrl(urlDataResponse, httpServletResponse);

        return new ResponseEntity<>(Utils.toJSONFromObject(urlDataResponse), HttpStatus.MOVED_TEMPORARILY);
    }

    @Operation(summary = "Endpoint para eliminar la configuracion de una URL corta", description = "Cuando proporciones una url corta eliminaras por completo su configuracion de reedireccion e informacion asociada a la URL corta que estas proporcionando")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La Url fue eliminada exitosamente"),
            @ApiResponse(responseCode = "202", description = "La Url que intentas eliminar no existe")
    })
    @DeleteMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Object> deleteUrlConfigByShortUrl(
            @Parameter(
                    name =  "shortUrl",
                    description  = "URL corta para eliminar de ejemplo",
                    example = "XXJJ3EA",
                    required = true
            ) @PathVariable String shortUrl
    ) {
        UrlEntity urlEntity = urlManegementService.deleteUrlConfigByShortUrl(shortUrl);
        return ResponseEntity.ok(String.format("Configuracion de URL: '%s' ha sido eliminada exitosamente", urlEntity.getLongUrl()));
    }

    @Operation(summary = "Endpoint para actualizar la configuracion de una URL", description = "En este endpoint podra actualizar la fecha de expiracion y si la URL estara activa o no para redireccionamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La actualizacion de la configuracion de la URL fue exitosa"),
            @ApiResponse(responseCode = "202", description = "La Url que intentas actualizar no existe"),
            @ApiResponse(responseCode = "400", description = "La informacion que proporcionas no es valida")
    })
    @PatchMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Object> updateUrlConfigByShortUrl(
            @Parameter(
                    name =  "shortUrl",
                    description  = "URL corta para actualizar de ejemplo",
                    example = "XXJJ3EA",
                    required = true
            ) @PathVariable String shortUrl,
            @Schema(
                    name =  "urlDataRequest",
                    description  = "Datos de prueba",
                    example = "{    \"expiredAt\": \"2024-05-20 23:59:59\",    \"isAvailable\": \"true\"}"
            ) @RequestBody UrlDataRequest urlDataRequest
    ) {
        UrlDataResponse urlDataResponse = urlManegementService.updateUrlConfigByShortUrl(shortUrl, urlDataRequest);
        return ResponseEntity.ok(urlDataResponse);
    }

}
