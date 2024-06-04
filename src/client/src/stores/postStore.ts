import {defineStore} from "pinia";
import router from "@/router/router";
import authApi from "@/api/auth"
import userApi from "@/api/user"
import type {AxiosResponse} from "axios";
import {TOKEN_KEY} from "@/constants/constants";
import axios from "axios";
import type {Post, User} from "@/interfaces/models";
import {postApi} from "@/api/post";

export const usePostStore = defineStore("post", {
    state: () => ({
        currentPosts: [] as Post[],
        currentChannelId: "",
        currentPostText: "",
        currentPostTitle: "",
        currentPost: {
            id: "",
            title: "",
            text: "",
            channelId: "",
        } as Post,
        currentPostOwner: null as User,
    }),
    getters: {

    },
    actions: {
        setCurrentChannel(id: string) {
            this.currentChannelId = id;
            postApi.listChannelPosts({
                id,
                batch: 500,
                page: 1,
            }).then((data: any) => {
                const posts = [] as Post[];
                for (const post of data) {
                    posts.push(post as any);
                }
                this.currentPosts = posts as any;
            });
        },
        createPost() {
            postApi.publishPost({
                id: this.currentChannelId,
                text: this.currentPostText,
                title: this.currentPostTitle,
                tagIds: [],
            }).then((data: any) => {
               this.currentPosts.push(data as any);
            });
        },
        setCurrentPost(id: string) {
            postApi.getPost({id})
                .then((data: any) => {
                    this.currentPost = data;
                    return userApi.getUser({id: this.currentPost.ownerId });
                })
                .then((data: any) => {
                    console.log("fetch owner: ", data);
                    this.currentPostOwner = data;
                })
        }
    }
});