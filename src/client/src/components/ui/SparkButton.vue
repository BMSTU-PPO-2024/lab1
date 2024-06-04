<template>
    <button
        class="spark-button"
    >
        <span><slot>Жми меня</slot></span>
    </button>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
    name: "sk-button",
});
</script>

<style scoped>
.spark-button {
    flex: 1;
    /*line-height: 0;*/
    padding: 0.5em 0.5em;
    border: 2px solid var(--color-border);
    border-radius: 0.6em;
    transition: 0.2s ease;
    cursor: pointer;
}
.spark-button:hover {
    filter: brightness(0.95);
}
</style>