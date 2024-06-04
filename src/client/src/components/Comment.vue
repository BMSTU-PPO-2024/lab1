<template>
    <div class="comment">
        <div class="comment_head">
            <div class="comment_author-name">
                {{comment.ownerId}}
            </div>
            <div class="comment_date">
                {{ comment.created }}
            </div>
        </div>
        <div class="comment_content">
            {{ comment.text }}
        </div>
        <div class="comment_ratings">
            1 +
        </div>
    </div>
</template>

<script lang="ts">
import {defineComponent, type PropType} from "vue";
import type {Comment} from "@/interfaces/models";

export default defineComponent({
    name: "sk-comment",
    props: {
        comment: {
            type: Object as PropType<Comment>,
            required: true,
        }
    }
})
</script>

<style scoped>
.comment {
    display: flex;
    flex-direction: column;
    gap: 0.5em;
    /*border: 2px solid var(--color-border);*/
    padding: 0.5em;
}

.comment_author-name {
    font-weight: bold;
}

.comment_head {
    display: flex;
    flex-direction: row;
    gap: 1em;
}

.comment_content {
    text-wrap: wrap;
}

.comment_ratings {
    margin-left: 2em;
}
</style>