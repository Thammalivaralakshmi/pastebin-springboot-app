import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";

export default function ViewPaste() {
  const { id } = useParams();
  const [paste, setPaste] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!id) return;
    fetch(`https://pastebin-springboot-app.onrender.com/p/${id}`)
      .then((res) => {
        if (res.status === 404) {
          setError("Paste not found or expired.");
          return null;
        }
        return res.json();
      })
      .then((data) => {
        if (data) setPaste(data);
      })
      .catch(() => setError("Error fetching paste."));
  }, [id]);

  if (error) return <h1>{error}</h1>;
  if (!paste) return <h1>Loading...</h1>;

  return (
    <div style={{ padding: "2rem" }}>
  <h1>Paste</h1>
  <pre>{paste.content}</pre>
  {paste.view_count !== undefined && (
    <p>View count: {paste.view_count}</p>
  )}
  {paste.remaining_views !== null && (
    <p>Remaining views: {paste.remaining_views}</p>
  )}
  {paste.expires_at && <p>Expires at: {paste.expires_at}</p>}
</div>
  );
}
