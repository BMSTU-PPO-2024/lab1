<template>
<ds-edit-channel-modal />

<v-container
    class="mt-5"
    style="max-width: 1000px;"
>
    <v-row class="pb-4 pt-4">
        <v-btn 
            text="Назад"
            variant="plain"
            prepend-icon="mdi-arrow-left"
            @click="router.back()"
        />
    </v-row>
    <v-row justify="space-between">
        <div class="d-flex flex-column"> 
            <!-- Заголовок страницы -->
            <div class="text-h6 font-weight-bold mb-2">
                {{ channelOwner?.nickname }}
            </div>
            <div class="text-h4 font-weight-bold mb-4">
                {{ channelStore.currentChannel?.name }}
            </div>  
        </div>

        <!-- Кнопка настроек -->
        <v-btn
            v-if="owner"
            icon
            variant="plain"
            @click="openSettings"
        >
            <v-icon>mdi-cog</v-icon>
        </v-btn>
    </v-row>
  
    <!-- Сетка карточек -->
    <v-row v-if="postStore.currentPosts?.length">
        <!-- Карточка "Создать пост" -->
        <v-col
            v-if="owner"
            cols="12"
            sm="6"
            md="4"
        >
            <v-card
                class="pa-4 d-flex justify-center align-center cursor-pointer"
                color="#ffe4e1"
                height="200"
                variant="flat"
                @click="navigateToCreatePost"
            >
                <div>
                    <v-icon>mdi-plus</v-icon>
                    <span class="ml-2">Создать пост</span>
                </div>
            </v-card>
        </v-col>
  
        <!-- Карточки постов -->
        <template
            v-for="post in postStore.currentPosts"
            :key="post.id"
        >
            <v-col
                v-if="post.visible"
                cols="12"
                sm="6"
                md="4"
            >
                <ds-post-preview
                    :post="post"
                    @click="openPost(post.id ?? '')"
                />
            </v-col>
        </template>
    </v-row>
    <v-row v-else>
        Постов нет
    </v-row>
</v-container>
</template>
  
<script lang="ts" setup>
import type { Post } from '@/api/types';
import DsTag from '@/components/DsTag.vue';
import DsPostPreview from '@/components/DsPostPreview.vue';
import { useChannelStore } from '@/stores/channel-store';
import { useUserStore } from '@/stores/user-store';
import { isOwner } from '@/util/owner';
import { usePostStore } from '@/stores/post-store';
import { useEditChannelDialogStore } from '@/stores/edit-channel-dialog-store';

const router = useRouter();
const route = useRoute();
const channelStore = useChannelStore();
const userStore = useUserStore();
const postStore  = usePostStore();
const editChannelStore  = useEditChannelDialogStore();

watchEffect(() => {
    const id = route.params.id;
    channelStore.fetchChannel(id);
});

watch(() => channelStore.currentChannel, () => {
    userStore.fetchUser(channelStore.currentChannel?.ownerId ?? '');
    postStore.fetchChannelPosts(channelStore.currentChannel?.id ?? '')
});


const channelOwner = computed(() => channelStore.currentChannel?.ownerId 
                                    ? userStore.getUser(channelStore.currentChannel?.ownerId)
                                    : null);

const owner = computed(() => isOwner(channelStore.currentChannel ?? {}));


function navigateToCreatePost() {
    router.push({
        name: 'post/create',
    })
}
function openSettings() {
    editChannelStore.shown = true;
}

function openPost(id: string) {
    router.push({
        name: 'post/:id',
        params: { id },
    })
}
</script>
  
<style scoped>
.v-card {
    display: flex;
    flex-direction: column;
}
</style>
  