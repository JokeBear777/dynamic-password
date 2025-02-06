package com.InfoSec.dynamic_password.global.utils.service;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class DomainExtractor {

    public String extractDomain(String url) throws URISyntaxException {
        return new URI(url).getHost();
    }

}
