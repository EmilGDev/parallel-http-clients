package com.example.parallelhttpclients.service;


import com.example.parallelhttpclients.client.AstronautsClient;
import com.example.parallelhttpclients.dto.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class HttpClientService {
    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final RestClient restClient;
    private final AstronautsClient astronautsClient;

    public HttpClientService(WebClient.Builder webClientBuilder,
                             RestTemplate restTemplate,
                             RestClient.Builder restClientBuilder,
                             AstronautsClient astronautsClient) {
        this.webClient = webClientBuilder.baseUrl("http://api.open-notify.org").build();
        this.restTemplate = restTemplate;
        this.restClient = restClientBuilder.baseUrl("http://api.open-notify.org").build();
        this.astronautsClient = astronautsClient;
    }

    public List<Result> executeRequests() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<CompletableFuture<Result>> futures = List.of(
                CompletableFuture.supplyAsync(this::callWithWebClient, executorService),
                CompletableFuture.supplyAsync(this::callWithRestTemplate, executorService),
                CompletableFuture.supplyAsync(this::callWithHttpClient, executorService),
                CompletableFuture.supplyAsync(this::callWithRestClient, executorService),
                CompletableFuture.supplyAsync(this::callWithFeignClient, executorService)
        );

        List<Result> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        executorService.shutdown();

        results.sort(Comparator.comparing(Result::duration));

        return results;
    }

    private Result callWithWebClient() {
        long start = System.currentTimeMillis();
        String response = webClient.get()
                .uri("/astros.json")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        long duration = System.currentTimeMillis() - start;
        return new Result("WebClient", duration, response);
    }

    private Result callWithRestTemplate() {
        long start = System.currentTimeMillis();
        String response = restTemplate.getForObject("http://api.open-notify.org/astros.json", String.class);
        long duration = System.currentTimeMillis() - start;
        return new Result("RestTemplate", duration, response);
    }

    private Result callWithHttpClient() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://api.open-notify.org/astros.json"))
                    .GET()
                    .build();
            long start = System.currentTimeMillis();
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            long duration = System.currentTimeMillis() - start;
            return new Result("HttpClient", duration, response);
        } catch (Exception e) {
            return new Result("HttpClient", -1, e.getMessage());
        }
    }

    private Result callWithRestClient() {
        long start = System.currentTimeMillis();
        String response = restClient.get()
                .uri("/astros.json")
                .retrieve()
                .body(String.class);
        long duration = System.currentTimeMillis() - start;
        return new Result("RestClient", duration, response);
    }

    private Result callWithFeignClient() {
        long start = System.currentTimeMillis();
        String response = astronautsClient.getAstronauts();
        long duration = System.currentTimeMillis() - start;
        return new Result("FeignClient", duration, response);
    }

}
