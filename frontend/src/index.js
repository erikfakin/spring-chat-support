import React from "react"
import ReactDOM from "react-dom"

import { BrowserRouter, Route, Routes } from "react-router-dom"
import Chatroom from "./pages/Chatroom"
import Dashboard from "./pages/Dashboard"
import Home from "./pages/Home"


ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/chatroom" element={<Chatroom />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById("root")
)
