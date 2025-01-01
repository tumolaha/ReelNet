import { createApi } from "@reduxjs/toolkit/query/react";
import axiosBaseQuery from "./AxiosCustomBaseQuery";

// Define your base API configuration
export const baseApi = createApi({
  reducerPath: "api",
  baseQuery: axiosBaseQuery(),
  endpoints: () => ({}), // We'll extend this in other files
  tagTypes: ["Posts", "Users"], // Add your cache tag types here
});
