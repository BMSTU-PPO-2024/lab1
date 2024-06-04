import {createRouter, createWebHistory} from "vue-router";
import {LoginView, Profile, TestView} from "@/views";
import {useUserStore} from "@/stores/userStore";
import ChannelView from "@/views/ChannelView.vue";
import PostView from "@/views/PostView.vue";

const routes = [
    {
        path: "/login",
        component: LoginView,
        props: { isLogin: true },
        name: "login",
    },
    {
        path: "/signup",
        component: LoginView,
        props: { isLogin: false },
        name: "signup",
    },
    {
        path: "/test",
        component: TestView,
        name: "test",
    },
    {
        path: "/profile",
        component: Profile,
        name: "profile",
        meta: { requireAuth: true }
    },
    {
        path: "/profile/:userId",
        component: Profile,
        name: "profile-user",
        props: true,
        meta: { requireAuth: true }
    },
    {
        path: "/channel/:channelId",
        component: ChannelView,
        name: "channel",
        props: true,
        meta: { requireAuth: true }
    },
    {
        path: "/post/:postId",
        component: PostView,
        name: "post",
        props: true,
        meta: { requireAuth: true }
    }

];

const router = createRouter({
    routes: routes,
    history: createWebHistory(),
});

router.beforeEach((to, from) => {
    const userStore = useUserStore();

    if (!userStore.authorized && (to.meta as {requireAuth?: boolean}).requireAuth) {
        console.log(`redirect from ${String(to.name)} to login beacause no auth`);
        return { name: "login" }
    }
});

export default router;