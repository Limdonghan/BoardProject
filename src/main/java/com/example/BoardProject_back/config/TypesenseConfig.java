package com.example.BoardProject_back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.typesense.api.Client;
import org.typesense.resources.Node;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TypesenseConfig {

    @Value("${typesense.protocol}")
    private String http;

    @Value("${typesense.host}")
    private String host;

    @Value("${typesense.port}")
    private String port;

    @Value("${typesense.api-key}")
    private String apyKey;

    @Bean
    public Client typesenceClient() {
        List<Node> nodes = new ArrayList<>();

        nodes.add(new Node(http, host, port));

        org.typesense.api.Configuration config = new org.typesense.api.Configuration(nodes, Duration.ofSeconds(2), apyKey);

        return new Client(config);


    }
}
