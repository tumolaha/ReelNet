// services/api.ts
import axios from "axios";
const baseURL = import.meta.env.VITE_API_URL as string;
const instance = axios.create({
  baseURL: baseURL + "/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

export default instance;
