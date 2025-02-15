package com.ayush.typeah.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TrieServiceTest {

    private TrieService trieService;

    @BeforeEach
    void setup() {
        trieService = new TrieService();
        trieService.insert("apple");
        trieService.insert("apple watch");
        trieService.insert("apple music");
        trieService.insert("amazon");
        trieService.insert("amazon prime");
        trieService.insert("car");
        trieService.insert("cardigan");
        trieService.insert("cards against humanity");
    }

    @Test
    void shouldReturnSuggestionsForValidPrefix() {
        List<String> results = trieService.search("app", 10);
        assertThat(results).containsExactlyInAnyOrder("apple", "apple watch", "apple music");
    }

    @Test
    void shouldReturnEmptyListForUnknownPrefix() {
        List<String> results = trieService.search("xyz", 10);
        assertThat(results).isEmpty();
    }

    @Test
    void shouldRespectLimit() {
        List<String> results = trieService.search("a", 2);
        assertThat(results).hasSize(2);
    }

    @Test
    void shouldBeCaseInsensitive() {
        List<String> results = trieService.search("APP", 10);
        assertThat(results).containsExactlyInAnyOrder("apple", "apple watch", "apple music");
    }

    @Test
    void shouldReturnWordWhenPrefixIsExactMatch() {
        List<String> results = trieService.search("car", 10);
        assertThat(results).contains("car", "cardigan", "cards against humanity");
    }

    @Test
    void shouldReturnEmptyListForBlankPrefix() {
        List<String> results = trieService.search("", 10);
        assertThat(results).isEmpty();
    }
}
