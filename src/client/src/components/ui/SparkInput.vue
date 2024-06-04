<template>
    <div
        class="spark-input-wrapper"
    >
        <p
            v-if="upperText"
            class="spark-input_upper-text"
        >
            {{ upperText }}
        </p>
        <input
            class="spark-input"
            :placeholder="placeholder"
            :value="modelValue"
            :type="type"
            @input="onInput"
        >
        <p
            v-if="lowerText"
            class="spark-input_lower-text"
        >
            {{ lowerText }}
        </p>
    </div>

</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
    name: "sk-input",
    props: {
        modelValue: {
            type: [String, Number],
            default: ""
        },
        upperText: {
            type: String,
            default: "",
        },
        lowerText: {
            type: String,
            default: "",
        },
        placeholder: {
            type: String,
            default: "xalup",
        },
        type: {
            type: String,
            default: "text",
        }
    },
    methods: {
        onInput(event: Event) {
            // console.log(event);
            this.$emit('update:modelValue', ((event as InputEvent).target as HTMLInputElement).value);
        }
    }
});
</script>

<style scoped>
.spark-input {
    border: 2px solid var(--color-border);
    padding: 1em;
    border-radius: 0.8em;
    width: 100%;
}

.spark-input_upper-text, .spark-input_lower-text {
    font-size: 0.8em;
    position: relative;
    left: 0.5em;
}
</style>