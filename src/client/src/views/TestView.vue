<template>
    <div class="test-view">
        <h3>smartfon vivo</h3>
        <sk-button
            @click="clickRating"
        >
            vivo
        </sk-button>
        <p>
            rating: {{ mainStore.rating }}
        </p>
        <div>
            <sk-input v-model="name" />
        </div>
        <h3>
            {{name}}
        </h3>

        <sk-button
            @click="$router.push('/login')"
        >
            to login
        </sk-button>

        <sk-modal
            v-model:show="showModal"
        >
            Hola!!
        </sk-modal>

        <sk-button
            @click="showModal = true"
        >
            open modal
        </sk-button>



        <sk-button
            @click="showPopup = !showPopup"
        >
            open popup
        </sk-button>
        <sk-popup
            v-model:show="showPopup"
        >
            <div style="padding: 1em">
                dark fantasies
            </div>
        </sk-popup>

        <div style="text-wrap: normal; word-break: break-word; width: 50%; overflow: auto; max-width: 600px"> your token is: {{ userStore.token ? userStore.token : "-" }}</div>

        <sk-post
            style="border: 1px solid black"
            :post="{
                id: 'dasdas',
                channelId: '123',
                title: 'joji',
                text: '# Marked in the browser\\n\\nRendered by **marked**.',
            }"
        />

        <sk-comment-list
            :comments="[{
                postId: '123',
                id: '1',
                ownerId: 'bebr',
                text: 'lorem20qfwedfwdgwrd hbh bj v vv gvhg v bkb kj bkj bbkgwrdgwrdfwsdnfwefo oi2in fwjbff njf j rnkjrn gkergk erkgejrkg jeng rn',
                created: '123',
            },
            {
                postId: '123',
                id: '2',
                ownerId: 'bebr',
                text: 'lorem20qfwedfwdgwrd hbh bj v vv gvhg v bkb kj bkj bbkgwrdgwrdfwsdnfwefo oi2in fwjbff njf j rnkjrn gkergk erkgejrkg jeng rn',
                created: '123',
            },
            {
                postId: '123',
                id: '3',
                ownerId: 'bebr',
                text: 'lorem20qfwedfwdgwrd hbh bj v vv gvhg v bkb kj bkj bbkgwrdgwrdfwsdnfwefo oi2in fwjbff njf j rnkjrn gkergk erkgejrkg jeng rn',
                created: '123',
            },
            ]"
        />

        <sk-button
            @click="testRequest"

        >
            test request
        </sk-button>

        <div
            ref="test"
        >

        </div>
    </div>
</template>

<script lang="ts">
import {useMainStore} from "@/stores/store";
import SkPost from "@/components/Post.vue";
import SkComment from "@/components/Comment.vue";
import SkCommentList from "@/components/CommentList.vue";
import axios from "axios";
import {useUserStore} from "@/stores/userStore";
import SkModal from "@/components/ui/SparkModal.vue";

export default {
    name: "test-view",
    components: {SkModal, SkCommentList, SkComment, SkPost},
    setup() {
        const mainStore = useMainStore();
        const userStore = useUserStore()

        return { mainStore, userStore };
    },
    data() {
        return {
            name: "",
            showModal: false,
            showPopup: false,
        };
    },
    computed: {

    },
    methods: {
        clickRating(e: MouseEvent) {
            console.log(e);
            this.mainStore.rating++;
            this.mainStore.increaseRating(10);
        },
        async testRequest() {
            const resp = await axios.get("https://jsonplaceholder.typicode.com/todos/1");
            console.log(resp);
            console.log(this.$refs);
            console.log(this.$refs["test"]);
            // this.$refs["test"].focus() ;
            (this.$refs["test"] as HTMLDivElement).textContent = JSON.stringify(resp.data);
        }
    }
}
</script>

<style scoped>

</style>