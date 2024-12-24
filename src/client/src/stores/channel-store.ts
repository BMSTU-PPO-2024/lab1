import { useApi } from '@/api/api';
import type { Channel, GetChannelsParams } from '@/api/types';
import { defineStore } from 'pinia';
import { useUserStore } from './user-store';

export const useChannelStore = defineStore('channel', () => {
    const api = useApi();
    const userStore = useUserStore();

    const selfChannels = ref<Channel[]>();
    // текущие отображаемые каналы, везде кроме профиля (то есть только на главной)
    const currentChannels = ref<Channel[]>();
    // текущий отображаемый канал
    const currentChannel = ref<Channel>();

    async function fetchSelfChannels() {
        const id = userStore.self?.id;
        if (!id) {
            console.log('no user fetched');
            return;
        }
        const { data } = await api.value.GET("/users/{userId}/channels", { params: {
            path: {
                userId: id
            },
        }});
        selfChannels.value = data;
    }

    async function fetchChannel(id: string) {
        const { data, error } = await api.value.GET('/channels/{channelId}', {
            params: {
                path: { channelId: id }
            }
        });
        if (data) {
            currentChannel.value = data;
        }
    }

    async function deleteChannel(id: string) {
        const { data, error } = await api.value.DELETE('/channels/{channelId}', {
            params: {
                path: { channelId: id }
            }
        });
        if (!error) {
            fetchSelfChannels();
        }
    }

    async function updateChannel(channel: Channel) {
        const { data, error } = await api.value.PATCH('/channels/{channelId}', {
            params: { path: {
                channelId: channel.id ?? '',
            }},
            body: channel,
        });
        if (!error) {
            fetchSelfChannels();
        }
    }


    async function getChannels(params: GetChannelsParams) {
        const { data, error } = await api.value.GET('/channels', {
            params: {
                query: params,
            }
        });
        if (!error) {
            currentChannels.value = data;
        }
    }
    

    return {
        selfChannels,
        currentChannel,
        currentChannels,

        fetchSelfChannels,
        fetchChannel,
        deleteChannel,
        updateChannel,
        getChannels,
    };
});
