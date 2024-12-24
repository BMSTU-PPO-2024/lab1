import { defineStore } from 'pinia';

export const useProfileStore = defineStore('profile', () => {
    const selectedTab = ref<'channels' | 'feeds'>('channels');

    return {
        selectedTab
    }
});