import { useApi } from '@/api/api';
import type { Feed, GetFeedsParams, Tag, User } from '@/api/types';
import { defineStore } from 'pinia';
import { useUserStore } from './user-store';
import { useTagStore } from './tag-store';

export const useMainStore = defineStore('main', () => {
    const api = useApi();

    const selectedTab = ref<'channels' | 'feeds'>('channels');
    const searchQuery = ref<string>('');

    return {
        selectedTab,
        searchQuery,
    }
});