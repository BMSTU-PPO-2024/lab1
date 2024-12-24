<template>
<v-container class="py-8">
    <v-row class="pb-4 pt-4">
        <v-btn 
            text="Назад"
            variant="plain"
            prepend-icon="mdi-arrow-left"
            @click="router.back()"
        />
    </v-row>
    <!-- Page Title -->
    <v-row>
        <v-col>
            <h1 class="text-h4 font-weight-bold">
                Редактировать пост
            </h1>
        </v-col>
    </v-row>
  
    <!-- Input for Post Title and Channel Selection -->
    <v-row class="mt-4">
        <v-col
            cols="8"
            class="d-flex align-center ga-4"
        >
            <h3 class="pb-6">
                Заголовок
            </h3>
            <v-text-field
                v-model="postStore.editingPost.title"
                label="Заголовок"
                variant="outlined"
            />
        </v-col>
        <v-col cols="4">
            <v-select
                v-model="postStore.editingPost.channel"
                item-title="name"
                return-object
                label="Выберите канал"
                variant="outlined"
                disabled
            />
        </v-col>
    </v-row>
  
    <v-row class="main-content d-flex flex-wrap md-flex-column">
        <!-- Markdown Input -->
        <v-col cols="6">
            <v-textarea
                v-model="postStore.editingPost.text"
                label="Содержание поста"
                rows="20"
                no-resize
                variant="outlined"
                :hide-details="true"
                class="h-100 input-area"
            />
        </v-col>
        <!-- Markdown Preview -->
        <v-col cols="6">
            <div
                class="preview-container pa-8"
                v-html="renderedMarkdown"
            />
        </v-col>
    </v-row>
  
    <!-- Tags and Actions -->
    <v-row class="mt-4 align-center flex-wrap">
        <v-col cols="6">
            <v-row class="d-flex ga-4 align-center">
                <span class="text-base-bold">Теги</span>

                <div class="d-flex ga-2">
                    <ds-tag
                        v-for="tag in postStore.editingPost.tags"
                        :key="tag"
                        :name="tag"
                        closable
                        @close="removeTag(tag)"
                    />
                </div>
            </v-row>
            <v-row class="mt-4 d-flex ga-4 align-center">
                <span class="text-base-bold">Введите тег</span>
                <!-- Add New Tag Input -->
                <v-text-field
                    v-model="newTag"
                    label="Введите тег"
                    variant="underlined"
                    density="comfortable"
                    class="pt-2"
                    max-width="200"
                />
                <v-btn
                    class="button-primary"
                    @click="addTag"
                >
                    Добавить
                </v-btn>
            </v-row>
        </v-col>
  
        <!-- Create Post Button -->
        <v-col
            cols="6"
            class="text-right d-flex ga-2 justify-end flex-wrap"
        >
            <v-btn
                class="button-primary"
                @click="savePost"
            >
                Сохранить
            </v-btn>
            <v-btn
                variant="text"
                @click="cancel"
            >
                Отмена
            </v-btn>
            <v-btn
                class="button-error"
                @click="deletePost"
            >
                Удалить пост
            </v-btn>
        </v-col>
    </v-row>
</v-container>
</template>
  
<script setup lang="ts">
import { ref, computed } from 'vue';
import { marked } from 'marked';
import DsTag from '@/components/DsTag.vue';
import { usePostStore } from '@/stores/post-store';
import { useChannelStore } from '@/stores/channel-store';
import { useUserStore } from '@/stores/user-store';
import router from '@/router';
import { useTagStore } from '@/stores/tag-store';

const route = useRoute();
const postStore = usePostStore();
const channelStore = useChannelStore();
const tagStore = useTagStore();

const renderer = new marked.Renderer();
renderer.image = function({href, title, text}) {
  const url = href.startsWith('http') ? href : `images/${href}`; // for external links
  return `<img class="preview-image" src="${url}" alt="${text}" title="${title}" />`;
};

// Markdown content
const renderedMarkdown = computed(() => marked(postStore.editingPost.text, { renderer }));
const newTag = ref('');


watchEffect(async () => {
    const id = route.params.id;
    await postStore.fetchPost(id);
    await channelStore.fetchChannel(postStore.currentPost?.channelId ?? '');
    await tagStore.fetchTagsOf(postStore.currentPost ?? {});
    postStore.startEditing(
        postStore.currentPost ?? {},
        tagStore.tagsByIds(postStore.currentPost?.tagIds ?? [])
                .map(t => t.name)
                .filter(Boolean) as string[],
        channelStore.currentChannel ?? {}, 
    );
    console.log('current channel', channelStore.currentChannel)
});


const addTag = () => {
    if (newTag.value.trim() && !postStore.editingPost.tags.includes(newTag.value)) {
        postStore.editingPost.tags.push(newTag.value);
        newTag.value = '';
    }
};
const removeTag = (tag: string) => {
    postStore.editingPost.tags = postStore.editingPost.tags.filter(t => t !== tag);
}

const savePost = async () => {
    await postStore.saveEditing();
    router.back();
};

const deletePost = async () => {
    await postStore.deletePost(postStore.editingPost.id ?? '');
    router.go(-2);
}

const cancel =  () => {
    router.back();
}
</script>
  
<style scoped>
.preview-container {
    height: 60vh;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background-color: #fafafa;
    overflow: auto;
    max-width: 100%;
    list-style-position: inside;
}

.preview-container >>> .preview-image {
    max-width: 100%;
}

.main-content {
    height: 600px;
}

.md-flex-column {
    @media screen and (max-width: 1024px) {
        flex-direction: column;
    }
}


/* .input-area >>> .v-input__details {
    display: none;
} */
</style>
  