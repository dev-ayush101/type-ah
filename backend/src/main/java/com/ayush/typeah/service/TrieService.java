package com.ayush.typeah.service;

import com.ayush.typeah.model.TrieNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrieService {

    private final TrieNode root;

    public TrieService() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toLowerCase().toCharArray()) {
            current.getChildren().computeIfAbsent(c, k -> new TrieNode());
            current = current.getChildren().get(c);
        }
        current.setEndOfWord(true);
        current.setWord(word);
    }

    public List<String> search(String prefix, int limit) {
        List<String> results = new ArrayList<>();

        if (prefix == null || prefix.isBlank()) {
            return results;
        }

        TrieNode current = root;

        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!current.getChildren().containsKey(c)) {
                return results; // No words with this prefix
            }
            current = current.getChildren().get(c);
        }

        dfs(current, results, limit);
        return results;
    }

    private void dfs(TrieNode node, List<String> results, int limit) {
        if(results.size() >= limit) return;
        if(node.isEndOfWord()) results.add(node.getWord());
        for (TrieNode child: node.getChildren().values()) {
            if (results.size() >= limit) return;
            dfs(child, results, limit);
        }
    }
}
