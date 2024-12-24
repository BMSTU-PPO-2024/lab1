<template>
<v-container class="post-container">
    <v-row class="pb-4 pt-4">
        <v-btn 
            text="Назад"
            variant="plain"
            prepend-icon="mdi-arrow-left"
            @click="router.back()"
        />
    </v-row>
    <!-- Верхняя часть: Автор, Дата и Кнопка редактировать -->
    <v-row
        align="center"
        justify="space-between"
    >
        <v-col
            cols="auto"
            direction="row"
        >
            <div class="d-flex align-center ga-4">
                <div class="author-name text-h6">
                    {{ postOwner?.nickname }}
                </div>
                <div class="post-date text-caption grey--text pt-1">
                    {{ postStore.currentPost?.created }}
                </div>
            </div>
        </v-col>
        <v-btn
            v-if="owner"
            class="button-primary"
            color="orange"
            variant="outlined"
            @click="openEditModal"
        >
            Редактировать
        </v-btn>
    </v-row>
  
    <!-- Заголовок поста -->
    <v-row>
        <v-col>
            <h1 class="post-title">
                {{ postStore.currentPost?.title }}
            </h1>
        </v-col>
    </v-row>
  
    <!-- Теги -->
    <v-row class="mb-4">
        <v-col>
            <!-- Теги -->
            <div 
                class="d-flex gap-2"
            >
                <ds-tag
                    v-for="tag in tags"
                    :key="tag.id"    
                    :name="tag.name ?? ''"
                />
            </div>
        </v-col>
    </v-row>
  
    <!-- Контент поста -->
    <v-row>
        <v-col>
            <div
                class="post-content"
                v-html="renderMarkdown(postStore.currentPost?.text ?? '')"
            />
        </v-col>
    </v-row>
  
    <!-- Лайки и дизлайки -->
    <v-row class="like-dislike-section mb-8">
        <ds-rating 
            :scores="postStore.currentPost?.scores"
            @rate="ratePost"
            @unrate="unratePost"
        />
    </v-row>
  
    <!-- Раздел комментариев -->
    <v-row>
        <v-col>
            <h2>Комментарии</h2>
            <!-- Текстовое поле для ввода комментария -->
            <v-textarea
                v-model="commentStore.newComment"
                label="Оставьте отзыв"
                outlined
                rows="3"
            />
            <v-btn
                color="orange"
                @click="sendComment"
            >
                Отправить
            </v-btn>
        </v-col>
    </v-row>
  
    <!-- Список комментариев -->
    <v-row
        v-for="(comment, index) in comments"
        :key="index"
        class="mb-4"
    >
        <v-col>
            <ds-comment 
                :comment="comment" 
                :author="commentOwners[index]?.nickname"
            />
        </v-col>
    </v-row>
</v-container>
</template>
  
<script setup lang="ts">
import type { Comment } from '@/api/types';
import DsComment from '@/components/DsComment.vue';
import { useCommentStore } from '@/stores/comment-store';
import { usePostStore } from '@/stores/post-store';
import { useTagStore } from '@/stores/tag-store';
import { useUserStore } from '@/stores/user-store';
import { isOwner } from '@/util/owner';
import { marked } from 'marked';
import { useRouter, useRoute } from 'vue-router';
import { scorePositive, scoreNegative } from '@/util/score';

const postStore = usePostStore();
const commentStore = useCommentStore();
const userStore = useUserStore();
const tagStore = useTagStore();
const router = useRouter();
const route = useRoute();

watchEffect(() => {
    const id = route.params.id;
    if (!Array.isArray(id)) {
        postStore.fetchPost(id);
    }
});

watch(() => postStore.currentPost, () => {
    userStore.fetchUser(postStore.currentPost?.ownerId ?? '');
    tagStore.fetchTagsOf(postStore.currentPost ?? {});
    commentStore.fetchComments(postStore.currentPost?.id ?? '');
});

watch(() => commentStore.currentComments, () => {
    userStore.fetchUsers(commentStore.currentComments?.map(p => p.ownerId ?? '') ?? []);
});


const postOwner = computed(() => postStore.currentPost?.ownerId 
                                ? userStore.getUser(postStore.currentPost.ownerId)
                                : null);

const owner = computed(() => isOwner(postStore.currentPost ?? {}));

const tags = computed(() => tagStore.tagsByIds(postStore.currentPost?.tagIds ?? []));

const commentOwners = computed(() => userStore.getUsers(commentStore.currentComments?.map(p => p.ownerId ?? '') ?? []))

const comments = computed(() => commentStore.currentComments);

const ratePost = async () => {
    await postStore.ratePost(postStore.currentPost?.id ?? '');
    postStore.fetchPost(postStore.currentPost?.id ?? '');
}

const unratePost = async () => {
    await postStore.unratePost(postStore.currentPost?.id ?? '');
    postStore.fetchPost(postStore.currentPost?.id ?? '');
}

// **Введение**

// DHCP (Dynamic Host Configuration Protocol) — это протокол, позволяющий автоматически назначать IP-адреса и другие параметры сети (например, шлюз по умолчанию и DNS-сервер) конечным устройствам. В Cisco Packet Tracer настройка DHCP позволяет эмулировать этот процесс, чтобы понять, как происходит автоматизация адресации в локальной сети.

// В этом руководстве мы рассмотрим процесс настройки DHCP на маршрутизаторе и коммутаторе в Cisco Packet Tracer, включая проверку правильности работы настроенного сервиса.

// ---

// ### **1. Понимание DHCP**

// Протокол DHCP состоит из четырех основных шагов:
// 1. **Discover**: Клиент отправляет широковещательный запрос, чтобы найти DHCP-сервер.
// 2. **Offer**: Сервер предлагает клиенту IP-адрес и другие параметры.
// 3. **Request**: Клиент запрашивает предложенные параметры.
// 4. **ACK**: Сервер подтверждает запрос клиента, завершая процесс настройки.

// ---

// ### **2. Сценарий**

// Мы настроим локальную сеть, в которой маршрутизатор будет выполнять роль DHCP-сервера. Коммутатор и конечные устройства (ПК) будут получать IP-адреса динамически.

// **Топология:**
// - Один маршрутизатор (Router0)
// - Один коммутатор (Switch0)
// - Три ПК (PC0, PC1, PC2)

// **IP-схема:**
// - Сеть: 192.168.1.0/24
// - Шлюз по умолчанию: 192.168.1.1
// - Диапазон DHCP: 192.168.1.2 – 192.168.1.254

// ---

// ### **3. Настройка топологии**

// 1. Откройте Cisco Packet Tracer и добавьте следующие устройства из вкладки **Network Devices**:
//    - Маршрутизатор (Router0)
//    - Коммутатор (Switch0)
//    - Три ПК (PC0, PC1, PC2)
// 2. Соедините устройства:
//    - Кабель **copper straight-through** для подключения коммутатора к ПК.
//    - Кабель **copper straight-through** для подключения коммутатора к маршрутизатору.
const renderer = new marked.Renderer();
renderer.image = function({href, title, text}) {
  const url = href.startsWith('http') ? href : `images/${href}`; // for external links
  return `<img class="preview-image" src="${url}" alt="${text}" title="${title}" />`;
};

function renderMarkdown(content: string) {
    return marked(content, { renderer });
}

function sendComment() {
    if (commentStore.newComment.trim()) {
        commentStore.createComment({
            text: commentStore.newComment.trim(),
            postId: postStore.currentPost?.id ?? '',
        });
        commentStore.newComment = '';
    }
    commentStore.fetchComments(postStore.currentPost?.id ?? '');
}

function openEditModal() {
    router.push({ 
        name: 'post/:id/edit',
        params: {
            id: postStore.currentPost?.id ?? '',
        },
    });
}

</script>
  
<style scoped>
.post-content {
    list-style-position: inside;
}
.post-container {
    max-width: 1000px;
    margin: 0 auto;
}

.post-content >>> .preview-image {
    max-width: 100%;
}


.like-dislike-section span {
    font-size: 14px;
}
</style>
  