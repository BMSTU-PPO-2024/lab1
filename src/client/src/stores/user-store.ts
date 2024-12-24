import { useApi } from '@/api/api';
import type { User } from '@/api/types';
import { defineStore } from 'pinia';
import { useAuthStore } from './auth-store';

export const useUserStore = defineStore('user', () => {
    const api = useApi();
    const authStore = useAuthStore();
    
    const self = ref<User>();

    const users = ref<User[]>([]);

    const getUser = computed(() => {
        return (id: string) => users.value.find(u => u.id == id);
    });

    const getUsers = computed(() => {
        return (ids: string[]) => ids
                                    .map(id => users.value.find(u => u.id == id))
                                    .filter(Boolean);
    });

    async function fetchSelf() {
        const { data } = await api.value.GET('/users');
        console.log('fetched self', data);
        self.value = data;
    }

    async function fetchUser(id: string) {
        const { data } = await api.value.GET('/users/{userId}', { params: { path: {
            userId: id,
        }}});
        if (data) {
            users.value = [
                ...users.value.filter(u => u.id !== data.id),
                data,
            ];
        }
    }

    async function fetchUsers(ids: string[]) {
        await Promise.all(ids.map(async id => {
            const { data } = await api.value.GET('/users/{userId}', { params: { path: {
                userId: id,
            }}});
            if (data) {
                users.value = [
                    ...users.value.filter(u => u.id !== data.id),
                    data
                ];
            }
        }));
    }

    watch(() => authStore.token, () => {
        console.log('authStore.token changed and fetch self')
        fetchSelf();
    });
    console.log('setup user store and fetch self')
    fetchSelf();

    return {
        self,

        getUser,
        getUsers,

        fetchSelf,
        fetchUser,
        fetchUsers,
    };
});
