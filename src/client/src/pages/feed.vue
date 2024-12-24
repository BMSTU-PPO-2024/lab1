<template>
<ds-edit-feed-modal />
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
                {{ feedOwner?.nickname }}
            </div>
            <div class="text-h4 font-weight-bold mb-4">
                {{ feedStore.currentFeed?.name }}
            </div>  
          
            <!-- Теги -->
            <div 
                class="mb-4 d-flex gap-2"
            >
                <ds-tag
                    v-for="tag in tags"
                    :key="tag.name"    
                    :name="tag.name ?? ''"
                />
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
import DsTag from '@/components/DsTag.vue';
import DsPostPreview from '@/components/DsPostPreview.vue';
import { useFeedStore } from '@/stores/feed-store';
import { useUserStore } from '@/stores/user-store';
import { useTagStore } from '@/stores/tag-store';
import { usePostStore } from '@/stores/post-store';
import { isOwner } from '@/util/owner';
import { useEditFeedDialogStore } from '@/stores/edit-feed-dialog-store';

const router = useRouter();
const route = useRoute();
const feedStore = useFeedStore();
const userStore = useUserStore();
const tagStore = useTagStore();
const postStore = usePostStore();
const editFeedStore = useEditFeedDialogStore();

watchEffect(() => {
    const id = route.params.id;
    feedStore.fetchFeed(id);
});

watch(() => feedStore.currentFeed, () => {
    userStore.fetchUser(feedStore.currentFeed?.ownerId ?? '');
    postStore.fetchFeedPosts(feedStore.currentFeed?.id ?? '');
    tagStore.fetchTagsOf(feedStore.currentFeed ?? {});
});

const feedOwner = computed(() => feedStore.currentFeed?.ownerId 
                                    ? userStore.getUser(feedStore.currentFeed?.ownerId)
                                    : null);

const owner = computed(() => isOwner(feedStore.currentFeed ?? {}));

const tags = computed(() => tagStore.tagsByIds(feedStore.currentFeed?.tagIds ?? []));


function openSettings() {
    editFeedStore.shown = true;
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
  