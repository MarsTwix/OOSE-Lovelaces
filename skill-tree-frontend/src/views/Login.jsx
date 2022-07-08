import React, { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { ChevronRight } from "react-feather";
import toastr from "toastr";

import LocalStorage from "../services/LocalStorage";
import UserRepository from "../repositories/UserRepository";
import SkillTreeRepository from "../repositories/SkillTreeRepository";
import { Roles } from "../enum";

export default function Login() {
  const navigate = useNavigate();

  const [formEmail, setFormEmail] = useState("");
  const [formPassword, setFormPassword] = useState("");
  const [disabled, setDisabled] = useState(false);

  const postLogin = useCallback(() => UserRepository.login({ email: formEmail, password: formPassword }), [formEmail, formPassword]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setDisabled(true);

    postLogin()
      .then(({ data }) => {
        const {
          id,
          firstname,
          lastname,
          email,
          roleId,
          token,
        } = data;

        LocalStorage.set("token", token);
        LocalStorage.set("user", JSON.stringify({
          id,
          firstname,
          lastname,
          email,
          roleId,
        }));

        SkillTreeRepository.setToken(token);

        if (roleId === Roles.COORDINATOR) {
          navigate("/admin/overview");
          return;
        }

        navigate("/overview");
      })
      .catch((error) => {
        if (error.response.status === 403) {
          toastr.error("Verkeerde email en/of wachtwoord combinatie, probeer het opniew.");
        }
      })
      .finally(() => {
        setDisabled(false);
      });
  };

  useEffect(() => {
    if (formEmail.length > 0 && formPassword.length > 0) {
      setDisabled(false);
    }
  }, [formEmail, formPassword]);

  return (
    <div className="min-h-full flex place-items-center h-screen w-screen justify-center">
      <div className="max-w-md w-full bg-white rounded-md p-8 shadow-md">
        <h1 className="font-medium leading-tight text-3xl mt-0 mb-4 text-center">Inloggen</h1>

        <form onSubmit={handleSubmit}>
          <input type="hidden" name="remember" value="true" />

          <div className="mt-2 flex flex-col">
            <label htmlFor="base-input" className="block font-medium text-gray-7">Email</label>
            <input
              onChange={(e) => setFormEmail(e.target.value)}
              type="email"
              autoComplete="email"
              id="email-input"
              required
              placeholder="Email"
              className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
              px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue
              focus:ring-pink-500 focus:border-pink-500 focus:z-10 transition duration-150
              ease-in-out sm:text-sm sm:leading-5"
            />
          </div>
          <div className="mt-2 flex flex-col">
            <label htmlFor="base-input" className="block font-medium text-gray-7">Wachtwoord</label>
            <input
              onChange={(e) => setFormPassword(e.target.value)}
              type="password"
              autoComplete="current-password"
              id="password-input"
              required
              placeholder="********"
              className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
              px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue
              focus:ring-pink-500 focus:border-pink-500 focus:z-10 transition duration-150
              ease-in-out sm:text-sm sm:leading-5"
            />
          </div>

          <div className="float-right mt-3">
            <button
              id="save"
              className="bg-pink-500  active:bg-pink-600 disabled:bg-pink-50 hover:bg-pink-600 flex
              items-center gap-2 text-white font-bold uppercase text-sm px-6 py-3 rounded shadow
              disabled:transform-none disabled:transition-none disabled:cursor-not-allowed outline-none
              focus:outline-none ease-linear transition-all duration-150"
              type="submit"
              disabled={disabled || formEmail === "" || formPassword === ""}
            >
              Inloggen
              <ChevronRight />
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
