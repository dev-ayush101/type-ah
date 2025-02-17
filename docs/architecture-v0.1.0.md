# Architecture — v0.1.0 (Phase 1: Core Autocomplete)

## System Overview

```mermaid
flowchart TD
    subgraph Client["Browser"]
        UI["React UI\nSearchBox.jsx"]
        DB["Debounce\n300ms"]
        UI -->|"user types"| DB
    end

    subgraph Dev["Dev Mode"]
        VP["Vite Proxy\nlocalhost:5173"]
    end

    subgraph Prod["Docker / Production"]
        NX["Nginx\nlocalhost:80"]
    end

    subgraph Backend["Spring Boot — localhost:8080"]
        AC["AutocompleteController\nGET /api/v1/suggest"]
        TS["TrieService\ninsert · search · DFS"]
        DL["DataLoader\n@PostConstruct"]
        
        subgraph Trie["Trie — in memory"]
            TN["TrieNode\nchildren · endOfWord · word"]
        end

        subgraph Data["Resources"]
            QJ["queries.json"]
        end
    end

    DB -->|"GET /api/v1/suggest?prefix=car"| VP
    DB -->|"GET /api/v1/suggest?prefix=car"| NX
    VP -->|"proxy"| AC
    NX -->|"proxy_pass"| AC
    AC --> TS
    TS --> TN
    DL -->|"reads on startup"| QJ
    DL -->|"inserts words"| TS
```

## Request Flow

```mermaid
sequenceDiagram
    actor User
    participant UI as SearchBox
    participant Debounce as Debounce (300ms)
    participant Backend as Spring Boot
    participant Trie as TrieService

    User->>UI: types "car"
    UI->>Debounce: schedules fetch
    User->>UI: types "d" (cancels previous)
    UI->>Debounce: reschedules fetch
    Note over Debounce: 300ms passes
    Debounce->>Backend: GET /api/v1/suggest?prefix=card
    Backend->>Trie: search("card", 10)
    Trie-->>Backend: ["cardigan", "cards against humanity"]
    Backend-->>UI: 200 OK — JSON array
    UI-->>User: renders dropdown
```

## Data Structures

```mermaid
flowchart TD
    root((root)) -->|d| D((d))
    D -->|o| O((o ✓\nword=do))
    O -->|g| G((g ✓\nword=dog))
    O -->|t| T((t ✓\nword=dot))
```

## Docker Setup

```mermaid
flowchart LR
    subgraph Compose["Docker Compose"]
        subgraph FE["typeah-frontend\nNginx:alpine — port 80"]
            Static["Static files\n(Vite build output)"]
        end

        subgraph BE["typeah-backend\neclipse-temurin:17-jre — port 8080"]
            JAR["app.jar"]
        end
    end

    Browser["Browser"] -->|"http://localhost"| FE
    FE -->|"proxy_pass /api → backend:8080"| BE

    Network["typeah-network\nbridge"]
    FE --- Network
    BE --- Network
```

## Component Responsibilities

| Component | Responsibility |
|---|---|
| `TrieNode` | Holds children map, endOfWord flag, and full word string |
| `TrieService` | Insert, DFS search with limit, blank prefix guard |
| `AutocompleteController` | HTTP layer — validates input, delegates to TrieService |
| `DataLoader` | Reads `queries.json` at startup, populates trie |
| `SearchBox.jsx` | Debounced fetch, renders suggestion dropdown |
| `nginx.conf` | Serves static files, proxies `/api` to backend |