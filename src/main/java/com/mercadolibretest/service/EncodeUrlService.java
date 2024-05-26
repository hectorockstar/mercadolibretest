package com.mercadolibretest.service;

import com.mercadolibretest.constants.MercadoLibreTestConstants;
import com.mercadolibretest.utils.UrlUtils;
import com.mercadolibretest.utils.Utils;
import org.springframework.stereotype.Service;

@Service
public class EncodeUrlService {

    public String getEncodedUrl(String url) {
        UrlUtils.urlValidator(url);

        byte[] sha256EncodedHash = Utils.getSha256EncodedHash(url);
        String base64Encoded = Utils.getBase64Encoded(sha256EncodedHash);

        StringBuilder sb = new StringBuilder(base64Encoded);
        return sb.substring(0, MercadoLibreTestConstants.URL_MAX_SIZE).toUpperCase();
    }

}
