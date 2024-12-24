import { useApi } from '@/api/api';
import type { Feed, GetFeedsParams, Tag, User } from '@/api/types';
import { defineStore } from 'pinia';
import { useUserStore } from './user-store';
import { useTagStore } from './tag-store';

export const useFeedStore = defineStore('feed', () => {
    const api = useApi();
    const userStore = useUserStore();
    const tagStore = useTagStore();

    const selfFeeds = ref<Feed[]>();
    // текущие отображаемые ленты, везде кроме профиля (то есть только на главной)
    const currentFeeds = ref<Feed[]>();
    const currentFeed = ref<Feed>();

    async function fetchSelfFeeds() {
        const id = userStore.self?.id;
        if (!id) {
            console.log('no user fetched');
            return;
        }
        const { data } = await api.value.GET("/users/{userId}/feeds", { params: {
            path: {
                userId: id
            }
        }});
        selfFeeds.value = data;
    }

    
    async function fetchFeed(id: string) {
        const { data } = await api.value.GET("/feeds/{feedId}", { params: {
            path: {
                feedId: id
            }
        }});
        if (data) {
            currentFeed.value = data;
        }
    }
    
    async function deleteFeed(id: string) {
        const { data, error } = await api.value.DELETE('/feeds/{feedId}', {
            params: {
                path: { feedId: id }
            }
        });
        if (!error) {
            fetchSelfFeeds();
        }
    }

    async function updateFeed(feed: Feed, tagNames?: string[]) {
        let tagObjects: Tag[] = [];
        if (tagNames) {
            await tagStore.createTags(tagNames);
            await tagStore.fetchAllTags();
            tagObjects = tagStore.tagsByNames(tagNames);
        }
        const { data, error } = await api.value.PATCH('/feeds/{feedId}', {
            params: { path: {
                feedId: feed.id ?? '',
            }},
            body: {
                ...feed,
                tagIds: [
                    ...(feed.tagIds ?? []), 
                    ...(tagObjects.map(tag => tag?.id ?? ''))
                ],
            },
        });
        if (!error) {
            fetchSelfFeeds();
        }
    }

    async function getFeeds(params: GetFeedsParams) {
        const { data, error } = await api.value.GET('/feeds', {
            params: {
                query: params,
            }
        });
        if (!error) {
            currentFeeds.value = data;
        }
    }

    return {
        selfFeeds,
        currentFeeds,
        currentFeed,

        fetchSelfFeeds,
        fetchFeed,
        deleteFeed,
        updateFeed,
        getFeeds,
    };
});
