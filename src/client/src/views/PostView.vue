<template>
    <div class="post-view">
        <div class="post-view_post-container">
            <sk-post
                :post="postStore.currentPost"
                :owner="postStore.currentPostOwner"
            />
        </div>
        <div class="post-view_divider">

        </div>
        <div class="post-view_comments-container">
            <h3>Комментарии</h3>
            <sk-comment-list
                :comments=""
            />
        </div>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import SkPost from "@/components/Post.vue";
import SkComment from "@/components/Comment.vue";
import SkCommentList from "@/components/CommentList.vue";
import {usePostStore} from "@/stores/postStore";
import {useCommentStore} from "@/stores/commentStore";

export default defineComponent({
    name: "PostView",
    components: {SkCommentList, SkPost},
    props: {
        postId: {
            type: String,
            required: true,
        },
    },
    setup() {
        const postStore = usePostStore();
        const commentStore = useCommentStore();

        return {postStore, commentStore};
    },
    created() {
        this.postStore.setCurrentPost(this.postId);
        this.commentStore.setCurrentPost(this.postId);
    }
});
</script>

<style scoped>
.post-view {
    width: 100%;
    min-height: 80vh;
}
.post-view_divider {
    border: 2px solid black;
}
</style>