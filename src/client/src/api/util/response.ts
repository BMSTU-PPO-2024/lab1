import type {AxiosError, AxiosResponse} from "axios";
import router from "@/router/router";
import {TOKEN_KEY} from "@/constants/constants";


export const handleSuccess = (response: AxiosResponse<any>): AxiosResponse<any> | any => {
    if (response.data) {
        return response.data;
    }
    return response;
}

export const handleError = (error: AxiosError): AxiosError => {
    console.log("request error:", error.message, "(", error.response?.data, ")", error);
    if (error.response?.status == 401) {
        localStorage.removeItem(TOKEN_KEY);
        router.push("/login");
    }
    throw error;
}