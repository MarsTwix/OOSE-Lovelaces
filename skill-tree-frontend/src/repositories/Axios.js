import axios from "axios";

import LocalStorage from "../services/LocalStorage";

const URL = process.env.REACT_APP_API_URL;

axios.defaults.headers.patch["Access-Control-Allow-Methods"] = "*";
axios.defaults.headers["Access-Control-Allow-Origin"] = "*";
axios.defaults.headers.common.Authorization = `Bearer ${LocalStorage.get("token")}`;
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response.status === 401) {
      LocalStorage.remove("token");
      LocalStorage.remove("user");
      window.location.reload();
    }
    return Promise.reject(error);
  },
);

export default {
  axios,
  url: URL,
};
