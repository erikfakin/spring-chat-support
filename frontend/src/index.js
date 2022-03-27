import React from "react"
import ReactDOM from "react-dom"

import { BrowserRouter, HashRouter, Route, Routes } from "react-router-dom"
import Chatroom from "./pages/Chatroom"
import Dashboard from "./pages/Dashboard"
import Home from "./pages/Home"

import "./index.scss"
import Layout from "./components/layout/Layout/Layout"

ReactDOM.render(
  <React.StrictMode>
    <HashRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/chatroom" element={<Chatroom />} />
        </Routes>
      </Layout>
    </HashRouter>
  </React.StrictMode>,
  document.getElementById("root")
)
