<template>
    <div class="comment-list">
        <sk-comment
            v-for="comm in comments"
            :comment="comm"
        />
    </div>
</template>

<script lang="ts">
import {defineComponent, type PropType} from "vue";
import type {Comment} from "@/interfaces/models";
import SkComment from "@/components/Comment.vue"

export default defineComponent({
    name: "sk-comment-list",
    components: {SkComment},

    props: {
        comments: {
            type: Object as PropType<Comment[]>,
            default: [],
        }
    }
});
</script>

<style scoped>

</style>