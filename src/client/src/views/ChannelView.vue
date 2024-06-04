<template>
    <div class="channel">
        <div class="channel_header">
            <div class="channel_author">
                Автор: {{ channelStore.currentAuthor?.nickname }}
            </div>
            <div class="channel_name">
                {{ channelStore.currentChannel.name }}
            </div>
        </div>
        <div class="channel_divider">

        </div>
        <div class="channel_buttons">
            <sk-button
                v-if="isSelf"
                @click="showEditor = true"
            >
                Создать пост
            </sk-button>
        </div>
        <div class="channel_posts">
            <post-preview
                v-for="post in postStore.currentPosts"
                :post="post"
                @click="$router.push('/post/'+post.id)"
            />
        </div>

        <sk-modal
            class="channel_create-post-modal"
            v-model:show="showEditor"
        >
            <div class="channel_editor">
                <sk-input
                    class="channel_editor-title-input"
                    :placeholder="'Введите заголовок'"
                    v-model="postStore.currentPostTitle"
                ></sk-input>
                <div class="channel_editor-content">
                    <textarea
                        class="channel_editor-textarea"
                        v-model="postStore.currentPostText"
                    >

                    </textarea>
                    <div
                        class="channel_editor-preview"
                        v-html="previewHtml"
                    >

                    </div>

                </div>
                <div class="channel_editor-buttons">
                    <sk-button
                        @click="createPost"
                    >
                        Создать
                    </sk-button>
                </div>
            </div>
        </sk-modal>
    </div>
</template>

<script lang="ts">
import {useChannelStore} from "@/stores/channelStore";
import {usePostStore} from "@/stores/postStore";
import {defineComponent} from "vue";
import PostPreview from "@/components/PostPreview.vue";
import {useUsersStore} from "@/stores/usersStore";
import {useUserStore} from "@/stores/userStore";
import {marked} from "marked";

export default defineComponent({
    name: "ChannelView",
    components: {PostPreview},

    props: {
        channelId: {
            type: String,
            required: true,
        }
    },

    data() {
        return {
            showEditor: false,
        }
    },

    setup() {
        const channelStore = useChannelStore();
        const postStore = usePostStore();
        const userStore = useUserStore();
        return {channelStore, postStore, userStore};
    },
    created() {
        this.channelStore.setCurrentChannel(this.channelId);
        this.postStore.setCurrentChannel(this.channelId);
    },
    computed: {
        isSelf() {
            return this.channelStore.currentChannel.ownerId == this.userStore.id;
        },
        previewHtml() {
            return marked(this.postStore.currentPostText);
        }
    },

    methods: {
        createPost() {
            this.showEditor = false;
            this.postStore.createPost();
        }
    }

})
</script>

<style scoped>
.channel {
    width: 100%;
    min-height: 80vh;
}
.channel_author {
    font-weight: bold;
    font-size: 1.5em;
}
.channel_name {
    font-weight: bold;
    font-size: 2.5em;
}
.channel_header {
    margin-left: 2em;
}
.channel_divider {
    border-bottom: 2px solid black;
}
.channel_posts {
    display: flex;
    gap: 2em;
    /*padding-top: 1em;*/
    padding: 1em;
}
.channel_buttons {
    margin: 1em 2em 0;
}
.channel_editor-content {
    padding: 1em;
    min-width: 600px;
    min-height: 20em;
    width: 80%;
    display: flex;
}
.channel_editor-textarea {
    overflow: auto;
    width: 50%;
    min-height: 100%;
    box-sizing: border-box;
    border: none;
    border-right: 1px solid #ccc;
    resize: none;
    outline: none;
    background-color: #f6f6f6;
    font-size: 14px;
    font-family: 'Monaco', courier, monospace;
    padding: 20px;
}
.channel_editor-preview {
    overflow: auto;
    width: 50%;
    height: 100%;
    box-sizing: border-box;
    padding: 0 20px;
}
.channel_editor-title-input {
    /*border-radius: 3em;*/
    --color-border: transparent;
    border-bottom: 1px solid black;
}
</style>