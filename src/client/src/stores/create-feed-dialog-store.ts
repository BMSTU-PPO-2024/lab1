import { useApi } from "@/api/api";
import { defineStore } from "pinia";
import { useFeedStore } from "./feed-store";
import { useTagStore } from "./tag-store";

export const useCreateFeedDialogStore = defineStore('create-feed-modal-store', () => {
    const api = useApi();
    const feedStore = useFeedStore();
    const tagStore = useTagStore();

    const shown = ref(false);
    const feedName = ref('');
    const visibility = ref(true);
    const tags = ref<string[]>([]);

    async function createFeed() {
        await tagStore.createTags(tags.value);
        await tagStore.fetchAllTags();
        const tagObjects = tagStore.tagsByNames(tags.value);
        const { data, error } = await api.value.POST('/feeds', {
            body: {
                name: feedName.value,
                visible: visibility.value,
                tagIds: tagObjects.map(tag => tag?.id ?? ''),
            }
        });

        if (data) {
            shown.value = false;
            feedName.value = '';
            visibility.value = true;
            tags.value = [];
            feedStore.fetchSelfFeeds(); // Assuming this fetches the list of feeds
        }
    }

    return {
        shown,
        feedName,
        visibility,
        tags,

        createFeed,
    };
});
