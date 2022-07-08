import { StrictMode, Suspense } from "react";
import React, { render } from "react-dom";

import App from "./containers/App";

import "./css/index.css";

render(
  <StrictMode>
    <Suspense fallback={<div>Loading...</div>}>
      <App />
    </Suspense>
  </StrictMode>,
  document.getElementById("root"),
);
