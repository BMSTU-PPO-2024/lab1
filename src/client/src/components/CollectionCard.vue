<template>
    <div
        class="collection-card"
        :class="{'collection-card__with-burger': hasBurger}"
    >
        <sk-button
            v-if="hasBurger"
            class="collection-card_burger"
            @click="clickBurger"
        >
            <img
                class="collection-card_burger-img"
                src="https://www.svgrepo.com/show/532944/dots-vertical.svg"
                alt="">
        </sk-button>

        <sk-popup
            v-model:show="showPopup"
        >
            <slot></slot>
        </sk-popup>

        <span
            class="collection-card_name"
            :class="{'collection-card_name__with-burger': hasBurger}"
        >
            {{ name }}
        </span>
    </div>
</template>

<script lang="ts">
import type {PropType} from "vue";
import SkButton from "@/components/ui/SparkButton.vue";

export default {
    name: "sk-collection-card",
    components: {SkButton},
    props: {
        name: {
            type: String,
            required: true
        },
        hasBurger: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            showPopup: false,
        }
    },
    methods: {
        clickBurger() {
            this.showPopup = !this.showPopup;
            this.$emit('burger:click');
        }
    }
}
</script>

<style scoped>
.collection-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 10em;
    height: 10em;
    transition: 0.2s;
    border: 1px solid var(--color-border);
    border-radius: 1.5em;
    cursor: pointer;
}
.collection-card__with-burger {
    justify-content: flex-start;
}
.collection-card:hover {
    background-color: var(--color-background-button);
}
.collection-card_burger {
    /*костыль*/
    --color-border: transparent;
    background: none;
    width: fit-content;
    height: fit-content;
    display: inline;
    justify-self: flex-end;
    align-self: flex-end;
    flex: 0;
}
.collection-card_burger-img {
    width: 1.5em;
}
.collection-card_name {
    justify-self: center;
}
.collection-card_name__with-burger {
    margin-top: 1em;
}
</style>