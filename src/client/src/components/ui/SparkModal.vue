<template>
    <Teleport to="#app">
        <div
            class="spark-modal"
            v-if="show"
            @click.self="$emit('update:show', false)"
        >
            <div class="spark-modal_content">
                <slot></slot>
                <div class="spark-modal_buttons">
                    <sk-button
                        class="spark-modal_close-button"
                        v-if="closeButtonText"
                        @click="hide"
                    >
                        {{ closeButtonText }}
                    </sk-button>
                </div>
            </div>
        </div>
    </Teleport>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
    name: "sk-modal",
    components: {},

    props: {
        show: {
            type: Boolean,
            default: false,
        },
        closeButtonText: {
            type: String,
            default: "ок",
        }
    },
    methods: {
        hide() {
            this.$emit("update:show", false);
        }
    }
});
</script>

<style scoped>
.spark-modal {
    z-index: 999;
    position: fixed;
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
}

.spark-modal_content {
    margin: auto;
    background: var(--color-background);
    border-radius: 1em;
    min-height: 50px;
    min-width: 300px;
}

.spark-modal_buttons {
    /*display: flex;*/
    /*justify-content: flex-end;*/
    padding: 0.5em;
}

.spark-modal_close-button {
    /*align-self: end;*/
    margin-left: 85%;
}
</style>