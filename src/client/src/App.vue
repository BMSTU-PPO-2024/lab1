<template>
    <div class="app-inner">
        <Header
            v-if="$route.name !== 'login' && $route.name !== 'signup'"
        >

        </Header>
        <template v-if="userDataLoaded">
            <router-view></router-view>
        </template>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useUserStore} from "@/stores/userStore";
import Header from "@/components/Header.vue"
// import stores from "@/stores/stores"
// import {mapMutations, mapState} from "vuex";

export default defineComponent({
    components: {Header},

    setup() {
        const userStore = useUserStore();

        return {userStore};
    },
    data: () => ({
        userDataLoaded: false,
        userPromise: null as any
    }),
    created() {
        console.log("start user data load");
        this.userStore.tryGetSelfData()
            .finally(() => {
                this.userDataLoaded = true;  // капец костыль????
                console.log("end user data load");
            });
    },
    async mounted() {

    }
});
</script>

<style scoped>
.app-inner {
    min-height: 100vh;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center
}
</style>