<template>
<v-app>
    <!-- <v-app-bar title="Devspark" /> -->
    <v-app-bar>
        <v-toolbar-title class="heading-h3-base">
            <router-link
                style="text-decoration: none; color: inherit;"

                :to="{ path: '/main' }"
            >
                DevSpark
            </router-link>
        </v-toolbar-title>

        <v-spacer />

        <template v-if="authStore.isAuthorized">
            <v-btn
                :to="'/profile'"
                :style="{ color: 'var(--color-primary)' }"
            >
                Профиль
            </v-btn>
            <v-btn
                @click="authStore.logout()"
            >
                Выйти
            </v-btn>
        </template>
        <template v-else>
            <v-btn
                to="/login"
            >
                Войти
            </v-btn>
        </template>
    </v-app-bar>

    <v-main>
        <router-view />
    </v-main>
</v-app>
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth-store';
import { ref } from 'vue';

const authStore = useAuthStore();

</script>
