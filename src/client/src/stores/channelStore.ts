import {defineStore} from "pinia";
import router from "@/router/router";
import authApi from "@/api/auth"
import userApi from "@/api/user"
import type {AxiosResponse} from "axios";
import {TOKEN_KEY} from "@/constants/constants";
import axios from "axios";
import type {Channel, User} from "@/interfaces/models";
import {channelApi} from "@/api/channel";
import {feedApi} from "@/api/feed";

export const useChannelStore = defineStore("channel", {
    state: () => ({
        currentUserChannels: [] as Channel[],
        currentChannelName: "",
        currentChannel: {
            id: "",
            name: "",
            visible: true,
            created: "",
            updated: "",
            ownerId: "",
        } as Channel,
        currentAuthor: null as User,
    }),
    getters: {

    },
    actions: {
        setCurrentUser(id: string) {
            return channelApi.listChannels({
                id,
                batch: 500,
                page: 1,
            }).then((data: AxiosResponse<any> | any) => {
                const channels = [] as Channel[];
                for (const chan of data) {
                    channels.push(chan as Channel);  // смартфон vivo
                }
                this.currentUserChannels = channels as any;
            });
        },
        create() {
            return channelApi.putChannel({ name: this.currentChannelName })
                .then((data: any) => {
                    this.currentUserChannels.push(data);
                });
        },
        remove(id: string) {
            this.currentUserChannels = this.currentUserChannels.filter((chan) => chan.id !== id);
            return channelApi.delChannel({id});
        },
        setCurrentChannel(id: string) {
            channelApi.getChannel({id})
                .then((data: any) => {
                    this.currentChannel = data;
                    return userApi.getUser({id: this.currentChannel.ownerId });
                })
                .then((data: any) => {
                    this.currentAuthor = data;
                });
        }
    }
});