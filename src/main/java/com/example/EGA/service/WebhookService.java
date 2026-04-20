package com.example.EGA.service;

import com.example.EGA.dto.WebhookPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);

    private final RestTemplate restTemplate;

    @Value("${laravel.webhook.url}")
    private String webhookUrl;

    @Value("${laravel.webhook.secret}")
    private String webhookSecret;

    public WebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Envoie le résultat du paiement à Laravel de façon non-bloquante.
     * Un échec du webhook ne doit jamais annuler une transaction déjà effectuée.
     */
    public void envoyer(WebhookPayload payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Webhook-Secret", webhookSecret);

            HttpEntity<WebhookPayload> request = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(webhookUrl, request, String.class);

            log.info("Webhook envoyé à Laravel : statut={}, montant={}", payload.getStatut(), payload.getMontant());
        } catch (Exception e) {
            log.error("Échec du webhook vers Laravel : {}", e.getMessage());
        }
    }
}
