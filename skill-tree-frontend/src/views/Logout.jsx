import React, { useCallback } from "react";
import { useNavigate } from "react-router-dom";

import LocalStorage from "../services/LocalStorage";
import { Spinner } from "../components";
import UserRepository from "../repositories/UserRepository";

export default function Logout() {
  const navigate = useNavigate();
  const token = LocalStorage.get("token");
  const postLogout = useCallback(() => UserRepository.logout(token), [token]);

  postLogout().finally(() => {
    LocalStorage.remove("token");
    LocalStorage.remove("user");
    navigate("/login");
  });

  return (
    <div className="flex flex-col space-y-2 w-full justify-center items-center">
      <Spinner />
      <h1>Uitloggen ...</h1>
    </div>
  );
}
