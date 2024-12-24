import { useAuthStore } from '@/stores/auth-store';
import { type Middleware } from 'openapi-fetch';

export const authMiddleware: Middleware = {
    async onRequest({ request }) {
        // set "foo" header
        // request.headers.set("Authorization", "bar");

        const authStore = useAuthStore();
        if (authStore.token) {
            request.headers.set("Authorization", `Bearer ${authStore.token}`);
        }

        // TODO useRouter useAuthStore
        // get token from store
        // if no token - throw error and navigate to login

        return request;
    },
    async onResponse({ response }) {
        // const { body, ...resOptions } = response;
        // change status of response
        // return new Response(body, { ...resOptions, status: 200 });

        if (response.status == 401) {
            const authStore = useAuthStore();
            authStore.logout();
        }

        // TODO useRouter useAuthStore
        // check if status is unauthorized
        // call logout in store
        // navigate login

        return response;
    },
    async onError({ error }) {
        // wrap errors thrown by fetch
        return new Error('Oops, fetch failed ' + error);
    },
};
