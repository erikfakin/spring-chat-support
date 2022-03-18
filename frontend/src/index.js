import React from "react"
import ReactDOM from "react-dom"

import { BrowserRouter, Route, Routes } from "react-router-dom"
import Chatroom from "./pages/Chatroom"
import Homepage from "./pages/Homepage"

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/chatroom" element={<Chatroom />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById("root")
)
