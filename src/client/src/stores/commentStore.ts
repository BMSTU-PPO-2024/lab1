import {defineStore} from "pinia";
import router from "@/router/router";
import authApi from "@/api/auth"
import userApi from "@/api/user"
import type {AxiosResponse} from "axios";
import {TOKEN_KEY} from "@/constants/constants";
import axios from "axios";

export const useCommentStore = defineStore("comment", {
    state: () => ({

    }),
    getters: {

    },
    actions: {
        setCurrentPost(id: string) {

        }
    }
});