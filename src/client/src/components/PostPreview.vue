<template>
    <div class="post-preview">

        <span class="post-preview_title">
            {{ post.title }}
        </span>
        <span class="post-preview_created">
            {{ post.created }}
        </span>
        <span class="post-preview_text">

        </span>
    </div>
</template>

<script lang="ts">
import {defineComponent, PropType} from "vue";
import {Post} from "@/interfaces/models";

export default defineComponent({
    name: "PostPreview",
    props: {
        post: {
            type: Object as PropType<Post>,
            required: true,
        },
    }
});
</script>

<style scoped>
.post-preview {
    width: 10em;
    height: 10em;
    transition: 0.2s;
    border: 1px solid var(--color-border);
    border-radius: 1.5em;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    text-overflow: ellipsis;
    word-wrap: break-word;
    overflow: hidden;
    cursor: pointer;
}

.post-preview_text {
    max-width: 5em;
    max-height: 2em;
    text-overflow: ellipsis;
    /*word-wrap: break-word;*/
    /*overflow: hidden;*/
}
.post-preview_text, .post-preview_title, .post-preview_created {
    margin-left: 10%;
}
.post-preview_title {
    font-weight: bold;
    font-size: 1.5em;
}
.post-preview_created {
    font-style: italic;
    font-weight: lighter;
}

.post-preview:hover {
    background-color: var(--color-background-button);
}

</style>