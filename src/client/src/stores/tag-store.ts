import { useApi } from '@/api/api';
import type { Tag } from '@/api/types';
import { defineStore } from 'pinia';

export const useTagStore = defineStore('tag', () => {
    const api = useApi();
    
    const tags = ref<Record<string, Tag>>({});

    const tagsByNames = computed(() => (names: string[]) => {
        return names
                .map(name => Object.values(tags.value).find(tag => tag.name === name))
                .filter(Boolean) as Tag[];
    });

    const tagsByIds = computed(() => (ids: string[]) => {
        return ids
                .map(id => Object.values(tags.value).find(tag => tag.id === id))
                .filter(Boolean) as Tag[];
    });

    async function fetchTags(tagIds: string[]) {
        await Promise.all(tagIds.filter(id => !tags.value?.[id]).map(async id => {
            const { data, error } = await  api.value.GET('/tags/{tagId}', { params: { path: {
                tagId: id
            }}});
            if (!data?.id || error) {
                console.log('failed to fetch tag ', id);
            } else {
                tags.value[data.id] = data;
            }
        }));
    }

    async function fetchAllTags() {
        const { data, error } = await api.value.GET('/tags', {
            params: {
                query: {
                    batch: 1000,
                }
            }
        });
        if (data) {
            for (const tag of data ?? []) {
                tags.value[tag.id ?? ''] = tag;
            }
        }
    }

    async function fetchTagsOf(value: { tagIds?: string[] }) {
        if (value.tagIds) {
            await fetchTags(value.tagIds);
        }
    }

    async function createTags(names: string[]) {
        const notExistsNames = names.filter(name => {
            return !Object.values(tags.value).some(t => t.name === name);
        })

        await Promise.all(notExistsNames.map(async name => {
            const { data, error } = await api.value.POST('/tags', { body: { name } });
            if (!data?.id || error) {
                console.log('failed to add tag ', name);
            } else {
                tags.value[data.id] = data;
            }
        }));
    }

    return {
        tags,

        tagsByNames,
        tagsByIds,
        
        fetchTags,
        fetchAllTags,
        fetchTagsOf,
        createTags,
    };
});
