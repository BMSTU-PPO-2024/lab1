<template>
    <div class="radio-group">
        <div
            v-for="variant in variants"
            key="variant"
            class="radio-group_button"
            :class="{'radio-group_button__selected': selected === variant}"
            @click="clickButton($event, variant)"
        >
            {{ variant }}
        </div>
    </div>
</template>

<script lang="ts">
import type {PropType} from "vue";
import {defineComponent} from "vue";

export default defineComponent({
    name: "sk-radio-group",
    props: {
        variants: {
            type: Object as PropType<string[]>,
            required: true
        },
        selected: {
            type: String,
            required: true,
        }
    },
    methods: {
        clickButton(event: MouseEvent, variant: string) {
            // console.log(event);
            // console.log(this.$el)
            // for (const child of Array.from(this.$el.children)) {
            //     (child as HTMLElement).classList.remove("radio-group_button__selected");
            // }
            // (event.target as HTMLElement).classList.add("radio-group_button__selected");
            // console.log("clicked:" + variant);
            this.$emit("update:selected", variant);
        }
    }
})
</script>

<style scoped>
.radio-group {
    display: flex;
    gap: 1em;
}
.radio-group_button {
    cursor: pointer;
    transition: 0.2s ease;
    padding: 0.3em;
}
.radio-group_button__selected {
    border-bottom: 1px solid #2c3e50;
}
.radio-group_button:hover {
    /*filter: brightness(0.4);*/
    background-color: var(--color-background-button);
    border-radius: 0.5em;
}

</style>