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
        <v-btn
            variant="plain"
        >
            {{ scorePositive(comment.scores ?? {}) }}
            <v-icon
                class="pl-2"
                color="green"
            >
                mdi-arrow-up
            </v-icon>
        </v-btn>
        <v-btn
            variant="plain"
        >
            {{ scoreNegative(comment.scores ?? {}) }}
            <v-icon
                class="pl-2"
                color="red"
            >
                mdi-arrow-down
            </v-icon>
        </v-btn>
    </div>
</div>
</template>

<script setup lang="ts">
import type { Comment } from '@/api/types';
import { scoreNegative, scorePositive } from '@/util/score'

const props = defineProps<{
    comment: Comment;
    author?: string;
}>();
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