import instance from "@/shared/utils/HttpRequest";
import { makeSerializable } from "@/shared/utils/ReduxUtils";
import type { BaseQueryFn } from "@reduxjs/toolkit/query";
import type { AxiosRequestConfig, AxiosError } from "axios";

// Define error response types
interface ValidationErrors {
  [key: string]: string[];
}

interface ErrorResponse {
  message: string;
  errors?: ValidationErrors;
  status?: number;
}

interface ErrorAxiosType {
  response?: {
    data: ErrorResponse;
    status: number;
    statusText: string;
    headers: Record<string, string>;
  };
  message: string;
  code?: string;
  isAxiosError: boolean;
}

const axiosBaseQuery =
  (): BaseQueryFn<
    {
      url: string;
      method?: AxiosRequestConfig["method"];
      body?: AxiosRequestConfig["data"];
      params?: AxiosRequestConfig["params"];
      headers?: AxiosRequestConfig["headers"];
    },
    unknown,
    ErrorAxiosType
  > =>
  async ({ url, method, body, params, headers, ...props }) => {
    try {
      const result = await instance({
        url: url,
        method,
        data: body,
        params,
        headers,
        ...props,
      });
      return result;
    } catch (axiosError) {
      const err = axiosError as AxiosError<ErrorResponse>;

      return {
        error: {
          config: makeSerializable(err.config),
          response: err.response && {
            data: {
              message:
                err.response.data?.message ||
                "Đã xảy ra lỗi, vui lòng thử lại sau",
              errors: makeSerializable(err.response.data?.errors),
              status: err.response.status,
            },
            status: err.response.status,
            statusText: err.response.statusText,
            headers: makeSerializable(err.response.headers) as Record<
              string,
              string
            >,
          },
          message: err.message,
          code: err.code,
          isAxiosError: err.isAxiosError,
        },
      };
    }
  };

export default axiosBaseQuery;
