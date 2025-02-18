# type-ah 💬

A production-inspired search autocomplete system, built phase by phase — from a basic trie to a distributed, ranked, real-time suggestion engine.

> Built as a learning project to implement the concepts behind large-scale autocomplete systems (Google, YouTube, Amazon) from the ground up.

---

## Current Version — v0.1.0

Core autocomplete with a trie data structure, REST API, debounced React UI, and Docker setup.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5 |
| Data Structure | Trie (in-memory) |
| Frontend | React, Vite |
| Containerisation | Docker, Docker Compose |
| Linting / Formatting | ESLint, Prettier |

---

## Project Structure

```
type-ah/
├── backend/                  # Spring Boot service
│   └── src/main/java/com/ayush/typeah/
│       ├── controller/       # AutocompleteController
│       ├── service/          # TrieService
│       ├── model/            # TrieNode
│       └── loader/           # DataLoader
├── frontend/                 # React + Vite
│   └── src/
│       ├── components/       # SearchBox
│       └── App.jsx
└── docs/                     # Architecture diagrams, ADRs
```

At startup, `DataLoader` reads `queries.json` and inserts every query into an in-memory trie. Each `GET /api/v1/suggest?prefix=car` request walks the trie to the prefix node and returns completions via DFS. The React frontend debounces keystrokes (300ms) before firing requests, keeping traffic minimal.

See [`docs/architecture-v0.1.0.md`](docs/architecture-v0.1.0.md) for full system and sequence diagrams.

---

## Running Locally

### Dev mode (recommended during development)

**Backend** — terminal 1:
```bash
cd backend
./mvnw spring-boot:run
```

**Frontend** — terminal 2:
```bash
cd frontend
npm run dev
```

Open [http://localhost:5173](http://localhost:5173)

### Docker (full stack)

```bash
docker compose up --build
```

Open [http://localhost](http://localhost)

```bash
docker compose down   # to stop
```

---

## API

### `GET /api/v1/suggest`

Returns top-K autocomplete suggestions for a given prefix.

| Parameter | Type | Required | Default | Description |
|---|---|---|---|---|
| `prefix` | string | ✅ | — | The search prefix |
| `limit` | int | ❌ | 10 | Max suggestions to return |

**Example:**
```bash
curl "http://localhost:8080/api/v1/suggest?prefix=car&limit=5"
```

**Response:**
```json
["car rental", "car wash near me", "car insurance", "cardigan", "cards against humanity"]
```

---

## Running Tests

```bash
cd backend
./mvnw test
```

```
Tests run: 7, Failures: 0, Errors: 0
```

---

## Roadmap

| Version | Phase | Status |
|---|---|---|
| `v0.1.0` | Core Autocomplete | ✅ Done |
| `v0.2.0` | Intelligent Ranking | 🔜 Next |
| `v0.5.0` | Production Foundation | ⬜ Planned |
| `v0.7.0` | Data Pipeline | ⬜ Planned |
| `v0.8.0` | Distribution Layer | ⬜ Planned |
| `v1.0.0` | Production-Ready | ⬜ Planned |

---

## Docs

- [`docs/architecture-v0.1.0.md`](docs/architecture-v0.1.0.md) — system diagram, sequence diagram, Docker topology