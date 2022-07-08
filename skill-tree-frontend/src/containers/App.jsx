import React from "react";
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import toastr from "toastr";

import RouteGuard from "../components/RouteGuard";

import routes from "../routes/Routes";

toastr.options = {
  progressBar: true,
  closeButton: true,
  maxOpened: 5,
  positionClass: "toast-top-center",
  hideMethod: "slideUp",
  closeMethod: "slideUp",
};

function App() {
  return (
    <div className="flex flex-row min-h-screen h-full overflow-y-auto bg-gray-100">
      <BrowserRouter>
        <Routes>
          {routes.map((route) => (
            route.redirectTo
              ? (
                <Route
                  key={route.path}
                  path={route.path}
                  element={<Navigate to={route.redirectTo} replace />}
                />
              )
              : (
                <Route
                  key={route.path}
                  path={route.path}
                  element={route.useGuard
                    ? (
                      <RouteGuard>
                        {route.useNavigation ? <route.navigation /> : null}
                        <route.component />
                      </RouteGuard>
                    ) : <route.component />}
                />
              )
          ))}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
