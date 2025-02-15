import { useState, useEffect } from "react";

function SearchBox() {
  const [query, setQuery] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const timer = setTimeout(async () => {
      if (query.trim() === "") {
        setSuggestions([]);
        return;
      }

      setLoading(true);
      try {
        const res = await fetch(
          `/api/v1/suggest?prefix=${encodeURIComponent(query)}&limit=10`
        );
        const data = await res.json();
        setSuggestions(data);
      } catch (err) {
        console.error("Failed to fetch suggestions:", err);
        setSuggestions([]);
      } finally {
        setLoading(false);
      }
    }, 300);

    return () => clearTimeout(timer);
  }, [query]);

  return (
    <div className="search-container">
      <input
        type="text"
        className="search-input"
        placeholder="Search..."
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />
      {loading && <div className="loading">Loading...</div>}
      {suggestions.length > 0 && (
        <ul className="suggestions-list">
          {suggestions.map((s, i) => (
            <li
              key={i}
              className="suggestion-item"
              onClick={() => {
                setQuery(s);
                setSuggestions([]);
              }}
            >
              {s}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default SearchBox;
