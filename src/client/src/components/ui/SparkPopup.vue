<template>
    <div
        class="spark-popup"
        v-if="show"
    >
        <div class="spark-popup_content">
            <slot></slot>
        </div>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
    name: "sk-popup",
    props: {
        show: {
            type: Boolean,
            default: false,
        },
    },
    methods: {
        hide() {
            this.$emit("update:show", false);
        }
    }
})
</script>

<style scoped>
.spark-popup {
    position: relative;
    z-index: 999;
    display: inline-block;
    /*min-width: fit-content;*/
    filter: drop-shadow(2px 2px 2px rgba(0,0,0,0.1));
}
.spark-popup_content {
    border-radius:  0.8em;
    border: 2px solid var(--color-border);
    position: absolute;
    background: var(--color-background);
}
</style>