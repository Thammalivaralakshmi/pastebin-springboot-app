import { BrowserRouter, Routes, Route } from "react-router-dom";
import NewPaste from "./NewPaste";
import ViewPaste from "./ViewPaste";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<NewPaste />} />
        <Route path="/p/:id" element={<ViewPaste />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;