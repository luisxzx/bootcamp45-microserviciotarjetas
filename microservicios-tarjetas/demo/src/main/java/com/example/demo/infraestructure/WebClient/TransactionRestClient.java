package com.example.demo.infraestructure.WebClient;

import com.example.demo.api.DTOS.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

@Component
public class TransactionRestClient {

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Void> sendTransaction(Transaction transaction) {
        WebClient webClient = webClientBuilder.baseUrl(transactionServiceUrl).build();
        return webClient.post()
                .uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .retrieve()
                // Manejo de errores:
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Client Error: " + clientResponse.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Server Error: " + clientResponse.statusCode())))
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    System.err.println("Error while sending transaction: " + e.getMessage());
                    return Mono.empty();
                });
    }
}