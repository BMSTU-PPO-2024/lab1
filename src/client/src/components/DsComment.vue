<template>
<div
    class="comment-item pa-3"
    style="background-color: #fff7f5; border-radius: 8px;"
>
    <div class="comment-header mb-2">
        <strong>{{ author ?? comment.ownerId }}</strong>
        <span class="grey--text text-caption ml-2">{{ comment.created }}</span>
    </div>
    <div class="comment-text mb-2">
        {{ comment.text }}
    </div>
    <div>
        <ds-rating 
            :scores="comment.scores"
            @rate="rate" 
            @unrate="unrate" 
        />
    </div>
</div>
</template>

<script setup lang="ts">
import type { Comment } from '@/api/types';
import { useCommentStore } from '@/stores/comment-store';
import { usePostStore } from '@/stores/post-store';

const commentStore = useCommentStore();
const postStore = usePostStore();

const props = defineProps<{
    comment: Comment;
    author?: string;
}>();

const rate = () => {
    commentStore.rateComment(props.comment.id ?? '');
    commentStore.fetchComments(postStore.currentPost?.id ?? '');
}

const unrate = () => {
    commentStore.unrateComment(props.comment.id ?? '')
    commentStore.fetchComments(postStore.currentPost?.id ?? '');
}

</script>

<style scoped>
.comment-item {
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}
.comment-header {
    display: flex;
    align-items: center;
}
</style>