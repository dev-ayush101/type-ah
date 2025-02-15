package com.ayush.typeah.controller;

import com.ayush.typeah.service.TrieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AutocompleteController {

    private final TrieService trieService;

    public AutocompleteController(TrieService trieService) {
        this.trieService = trieService;
    }

    @GetMapping("/suggest")
    public List<String> suggest(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "10") int limit) {

        if (prefix == null || prefix.isBlank()) {
            return List.of();
        }
        return trieService.search(prefix.trim(), limit);
    }
}
