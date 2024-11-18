package com.example.parallelhttpclients.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "astronautsClient", url = "http://api.open-notify.org")
public interface AstronautsClient {
    @GetMapping("/astros.json")
    String getAstronauts();
}
