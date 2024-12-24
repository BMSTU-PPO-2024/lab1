/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
// import { setupLayouts } from 'virtual:generated-layouts'
// import { routes } from 'vue-router/auto-routes'
import Default from '@/layouts/default.vue';
import Index from '@/pages/index.vue';
import Uuu from '@/pages/uuu.vue';
import Auth from '@/layouts/auth.vue';
import Login from '@/pages/login.vue';
import Register from '@/pages/register.vue';
import Profile from '@/pages/profile.vue';
import Post from '@/pages/post.vue';
import CreatePost from '@/pages/create-post.vue';
import EditPost from '@/pages/edit-post.vue';
import Feed from '@/pages/feed.vue';
import Channel from '@/pages/channel.vue';
import Main from '@/pages/main.vue';
import NotFound from '@/pages/not-found.vue';
import { authGuard } from './auth-guard';

// console.log('routes', JSON.stringify(routes, null, 4));
// const layouts = setupLayouts(routes);

// console.log('layouts', layouts);

const myRoutes: RouteRecordRaw[] = [
    {
        path: '/',
        component: Default,
        children: [
            {
                path: '',
                name: '',
                component: Index,
            },
            {
                path: 'test',
                name: 'test',
                component: Uuu,
            },
            {
                path: 'feeds/:id',
                name: 'feeds/:id',
                component: Feed,
            },
            {
                path: 'channels/:id',
                name: 'channels/:id',
                component: Channel,
            },
            {
                path: 'post/create',
                name: 'post/create',
                component: CreatePost,
                // beforeEnter: [authGuard],
            },
            
            {
                path: 'post/:id',
                name: 'post/:id',
                component: Post,
            },
            {
                path: 'post/:id/edit',
                name: 'post/:id/edit',
                component: EditPost,
                // beforeEnter: [authGuard],
            },
            {
                path: 'profile',
                name: 'profile',
                component: Profile,
            },
            {
                path: 'main',
                name: 'main',
                component: Main,
            },
            {
                path: '/:pathName(.*)',
                name: 'fallback',
                component: NotFound,
            },
        ],
        meta: {
            isLayout: true,
        },
    },
    {
        path: '/login',
        component: Auth,
        children: [
            {
                path: '',
                name: '',
                component: Login,
            },
        ],
        meta: {
            isLayout: true,
        },
    },
    {
        path: '/register',
        component: Auth,
        children: [
            {
                path: '',
                name: '',
                component: Register,
            },
        ],
        meta: {
            isLayout: true,
        },
    },
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: myRoutes,
});

// Workaround for https://github.com/vitejs/vite/issues/11804
router.onError((err, to) => {
    if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
        if (!localStorage.getItem('vuetify:dynamic-reload')) {
            console.log('Reloading page to fix dynamic import error');
            localStorage.setItem('vuetify:dynamic-reload', 'true');
            location.assign(to.fullPath);
        } else {
            console.error('Dynamic import error, reloading page did not fix it', err);
        }
    } else {
        console.error(err);
    }
});

router.isReady().then(() => {
    localStorage.removeItem('vuetify:dynamic-reload');
});

export default router;
