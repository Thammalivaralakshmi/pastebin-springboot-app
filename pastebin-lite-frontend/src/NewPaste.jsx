import { useState } from "react";

export default function NewPaste() {
  const [content, setContent] = useState("");
  const [ttl, setTtl] = useState("");
  const [maxViews, setMaxViews] = useState("");
  const [url, setUrl] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      const res = await fetch("https://pastebin-springboot-app.onrender.com/api/pastes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          content,
          ttl_seconds: ttl ? parseInt(ttl) : undefined,
          max_views: maxViews ? parseInt(maxViews) : undefined,
        }),
      });

      const data = await res.json();
      if (res.ok) {
        setUrl(data.url);
      } else {
        alert(data.error || "Failed to create paste");
      }
    } catch (err) {
      alert("Network error: " + err.message);
    }
  }

  return (
    <div style={{ padding: "2rem" }}>
      <h1>Create a Paste</h1>
      <form onSubmit={handleSubmit}>
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Enter your text"
          required
          rows={6}
          cols={50}
        />
        <br />
        <input
          type="number"
          value={ttl}
          onChange={(e) => setTtl(e.target.value)}
          placeholder="TTL (seconds)"
        />
        <br />
        <input
          type="number"
          value={maxViews}
          onChange={(e) => setMaxViews(e.target.value)}
          placeholder="Max views"
        />
        <br />
        <button type="submit">Create Paste</button>
      </form>
      {url && (
        <p>
          Paste created! <a href={url}>{url}</a>
        </p>
      )}
    </div>
  );
}
