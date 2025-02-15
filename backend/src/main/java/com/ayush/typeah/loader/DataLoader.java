package com.ayush.typeah.loader;

import com.ayush.typeah.service.TrieService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader {

    private final TrieService trieService;
    private final ObjectMapper objectMapper;

    public DataLoader(TrieService trieService, ObjectMapper objectMapper) {
        this.trieService = trieService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void load() {
        try {
            InputStream stream = getClass().getResourceAsStream("/data/queries.json");
            List<String> queries = objectMapper.readValue(stream, new TypeReference<>() {});
            queries.forEach(trieService::insert);
            System.out.println("Trie loaded with " + queries.size() + " queries.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load query dataset", e);
        }
    }
}
