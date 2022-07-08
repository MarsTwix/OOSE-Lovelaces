import React from "react";
import { Navigate, useLocation } from "react-router-dom";

import LocalStorage from "../services/LocalStorage";

export default function RouteGuard({ children }) {
  const location = useLocation();

  const token = LocalStorage.get("token");

  if (!token) {
    return <Navigate to={`/login?return=${location.pathname}`} state={{ from: location }} replace />;
  }

  return children;
}
