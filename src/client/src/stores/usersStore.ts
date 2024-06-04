import {defineStore} from "pinia";
import router from "@/router/router";
import authApi from "@/api/auth"
import userApi from "@/api/user"
import type {AxiosResponse} from "axios";
import {TOKEN_KEY} from "@/constants/constants";
import axios from "axios";
import type {User} from "@/interfaces/models";

interface UserIdMap {
    [id: string]: User;
}

export const useUsersStore = defineStore("users", {
    state: () => ({
        idMap: {} as UserIdMap,
        currentUser: { // тот чью страницу сейчас просматривают
            id: "",
            nickname: "",
            email: "",
            banned: false,
            permissions: 0,
            created: "",
            updated: "",
            about: "",
            avatarUrl: "",
        } as User,
    }),
    getters: {},
    actions: {
        setCurrentUser(id: string) {
            return userApi.getUser({id})
                .then((data: AxiosResponse<any> | any) => {
                    this.currentUser.banned = data.banned;
                    this.currentUser.created = data.created;
                    this.currentUser.email = data.email;
                    this.currentUser.id = data.id;
                    this.currentUser.nickname = data.nickname;
                    this.currentUser.permissions = data.permissions;
                    this.currentUser.updated = data.updated;
                    this.currentUser.avatarUrl = data.avatarUrl ? data.avatarUrl : "https://i.ytimg.com/vi/av4sEcTS8QA/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLCTKhwL0z9c_jJqF9-1q__3y1zMEw";
                    this.currentUser.about = data.about ? data.about : "";
                });
        }
    }
});