package com.ayush.typeah.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {

    @Getter
    private final Map<Character, TrieNode> children;

    @Getter
    @Setter
    private boolean endOfWord;

    @Getter
    @Setter
    private String word;

    public TrieNode() {
        this.children = new HashMap<>();
        this.endOfWord = false;
        this.word = null;
    }
}
