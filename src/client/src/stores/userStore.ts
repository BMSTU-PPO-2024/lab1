import {defineStore} from "pinia";
import router from "@/router/router";
import authApi from "@/api/auth"
import userApi from "@/api/user"
import type {AxiosResponse} from "axios";
import {TOKEN_KEY} from "@/constants/constants";
import axios from "axios";

export const useUserStore = defineStore("user", {
    state: () => ({
        id: "",
        nickname: "lox",
        email: "",
        banned: false,
        permissions: 0,
        created: "",
        updated: "",
        password: "",
        token: "",
        avatarUrl: "https://i.ytimg.com/vi/av4sEcTS8QA/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLCTKhwL0z9c_jJqF9-1q__3y1zMEw",
        about: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur ut vestibulum orci, at iaculis arcu. Donec nec enim ut augue."
    }),
    getters: {
        authorized(): boolean {
            return this.token != "";
        }
    },
    actions: {
        setToken(token: string) {
            this.token = token;
            localStorage.setItem(TOKEN_KEY, this.token);
            axios.defaults.headers.common['Authorization'] = this.token;
        },
        invalidateToken() {
            this.token = "";
            localStorage.removeItem(TOKEN_KEY);
            delete axios.defaults.headers.common['Authorization'];
        },
        async login() {
            return await authApi.login({
                email: this.email,
                password: this.password,
            })
                .then((data: AxiosResponse<any> | any) => {
                    this.setToken(`Bearer ${data.token}`);
                    router.push("/test");
                    this.fetchSelf();
                });
        },
        async signup() {
            return await authApi.register({
                email: this.email,
                password: this.password,
            })
                .then((data: AxiosResponse<any> | any) => {
                    this.setToken(`Bearer ${data.token}`);
                    router.push("/test");
                    this.fetchSelf();
                });
        },
        async tryGetSelfData(): Promise<any> {
            this.retrieveToken();
            if (this.token) {
                return this.fetchSelf();
            }
            return null;
        },
        retrieveToken() {
            const tok = localStorage.getItem(TOKEN_KEY);
            if (tok) {
                this.setToken(tok);
            }
        },
        fetchSelf() {
            return userApi.getSelf()
                .then((data: AxiosResponse<any> | any) => {
                    this.banned = data.banned;
                    this.created = data.created;
                    this.email = data.email;
                    this.id = data.id;
                    this.nickname = data.nickname;
                    this.permissions = data.permissions;
                    this.updated = data.updated;
                    this.avatarUrl = "https://i.ytimg.com/vi/av4sEcTS8QA/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLCTKhwL0z9c_jJqF9-1q__3y1zMEw";
                });
        }
    }
});