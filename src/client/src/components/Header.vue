<template>
    <div class="header">
        <div class="header_content">
            <div class="header_logo">
                DevSpark
            </div>

            <div class="header_buttons">
                <router-link
                    v-if="userStore.authorized && $route.name !== 'profile'"
                    to="/profile">Профиль</router-link>
                <router-link
                    v-if="!userStore.authorized"
                    to="/login">Войти</router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import {useUserStore} from "@/stores/userStore";

export default {
    name: "Header",
    setup() {
        const userStore = useUserStore();
        return {userStore};
    },

}
</script>

<style scoped>
.header {
    border-bottom: 2px solid black;
    position: fixed;
    height: 3em;
    top: 0;
    width: 100%;
    padding-bottom: 3em;
    /*padding-right: 2em;*/
    background-color: var(--color-background);
}
.header_content {
    position: relative;
}
.header_logo {
    font-size: 2em;
}
.header_buttons {
    position: absolute;
    right: 1em;
    top: 0;
}
</style>