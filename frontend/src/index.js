import React from "react"
import ReactDOM from "react-dom"

import { BrowserRouter, Route, Routes } from "react-router-dom"
import Homepage from "./pages/Homepage"

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Homepage />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById("root")
)
