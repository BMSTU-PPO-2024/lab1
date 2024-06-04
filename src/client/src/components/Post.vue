<template>
    <div class="post">
        <div class="post_head">
            <div class="post_author-name">
                {{ owner?.nickname }}
            </div>
            <div class="post_date">
                {{post.created}}
            </div>
        </div>
        <div class="post_title">
            {{ post.title }}
        </div>
        <div class="post_tags">
            <div
                class="post_tag"
            >

            </div>
        </div>
        <div class="post_content" v-html="postHTML">

        </div>
        <div class="post_rating">
           1+
        </div>
    </div>
</template>

<script lang="ts">
import {defineComponent, type PropType} from "vue";
import type {Post, User} from "@/interfaces/models";
import {marked} from "marked";

export default defineComponent({
    name: "sk-post",
    props: {
        post: {
            type: Object as PropType<Post>,
            required: true,
        },
        owner: {
            type: Object as PropType<User>,
            required: false
        }
    },
    computed: {
        postHTML() {
            return marked.parse(this.post.text);
        }
    }
});
</script>

<style scoped>
.post {
    width: 100%;
    min-height: 100%;
}
.post_date {
    font-style: italic;
    font-weight: lighter;
}
.post_title {
    font-weight: bold;
    font-size: 2em;
}
.post_content {
    margin: 2em 0;
}
.post_author-name {
    font-size: 1.5em;
}
</style>