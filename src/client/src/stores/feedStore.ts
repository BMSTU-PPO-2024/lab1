import {defineStore} from "pinia";
import router from "@/router/router";
import authApi from "@/api/auth"
import userApi from "@/api/user"
import type {AxiosResponse} from "axios";
import {TOKEN_KEY} from "@/constants/constants";
import axios from "axios";
import {channelApi} from "@/api/channel";
import type {Channel, Feed} from "@/interfaces/models";
import {feedApi} from "@/api/feed";

export const useFeedStore = defineStore("feed", {
    state: () => ({
        currentUserFeeds: [] as Feed[],
        currentUserId: "",
    }),
    getters: {

    },
    actions: {
        setCurrentUser(id: string) {
            this.currentUserId = id;
            return feedApi.listFeeds({
                id,
                batch: 500,
                page: 1,
            }).then((data: AxiosResponse<any> | any) => {
                const feeds = [] as Feed[];
                for (const feed of data) {
                    feeds.push(feed as Feed);  // смартфон vivo
                }
                this.currentUserFeeds = feeds as any;
            });
        },
        create(name: string) {
            return feedApi.putFeed({ name: name })
                .then((data: any) => {
                    this.currentUserFeeds.push(data);
                });
        },
        remove(id: string) {
            this.currentUserFeeds = this.currentUserFeeds.filter((feed) => feed.id !== id);
            return feedApi.delFeed({id});
        }
    },

});