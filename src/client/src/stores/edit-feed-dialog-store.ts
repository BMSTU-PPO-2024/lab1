import { useApi } from "@/api/api";
import { defineStore } from "pinia";
import { useFeedStore } from "./feed-store";
import { useTagStore } from "./tag-store";

export const useEditFeedDialogStore = defineStore('create-feed-modal-store', () => {
    const api = useApi();
    const feedStore = useFeedStore();
    const tagStore = useTagStore();

    const shown = ref(false);
    const feedName = ref('');
    const visibility = ref(true);
    const tags = ref<string[]>([]);

    const feed = computed(() => feedStore.currentFeed);

    watch(() => [shown, feed.value], async () => {
        console.log('watch open edit feed dialog', shown.value, feed.value);
        if (shown.value && feed.value) {
            feedName.value = feed.value.name ?? '';
            visibility.value = feed.value.visible ?? true;
            tags.value = tagStore.tagsByIds(feed.value.tagIds ?? []).map(tag => tag.name ?? '')
        }
    })

    async function saveFeed() {
        await feedStore.updateFeed({
            ...feed.value,
            name: feedName.value,
            visible: visibility.value,
            tagIds: [],
        }, tags.value);
        shown.value = false;
        feedStore.fetchFeed(feed.value?.id ?? '');
    }

    async function deleteFeed() {
        await feedStore.deleteFeed(feed.value?.id ?? '');
        shown.value = false;
    }

    return {
        shown,
        feedName,
        visibility,
        tags,

        saveFeed,
        deleteFeed,
    };
});
