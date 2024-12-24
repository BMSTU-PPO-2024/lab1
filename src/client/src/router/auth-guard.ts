import { useAuthStore } from '@/stores/auth-store';
import { type RouteRecordRaw } from 'vue-router';
import { fa } from 'vuetify/locale';

export const authGuard = (() => {
    const authStore = useAuthStore();
    if (!authStore.isAuthorized) {
        return { path: '/login' };
    }
    return true;
}) satisfies RouteRecordRaw['beforeEnter'];
