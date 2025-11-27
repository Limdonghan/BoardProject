package com.example.BoardProject_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.typesense.api.Client;
import org.typesense.resources.Node;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TypesenseConfig {

    @Bean
    public Client typesenceClient() {
        List<Node> nodes = new ArrayList<>();

        nodes.add(new Node("http", "localhost", "8108"));

        org.typesense.api.Configuration config = new org.typesense.api.Configuration(nodes, Duration.ofSeconds(2), "xyz");

        return new Client(config);


    }
}
