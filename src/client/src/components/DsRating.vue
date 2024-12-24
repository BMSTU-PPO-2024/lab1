<template>
 <v-btn
    variant="plain"
    :class="{
        'liked': isLiked(scores ?? {})
    }"
    :disabled="disabled"
    @click="click"
>
    {{ scorePositive(scores ?? {}) }}
    <v-icon
        class="pl-2"
        color="green"
    >
        mdi-arrow-up
    </v-icon>
</v-btn>
</template>

<script setup lang="ts">
import { scorePositive, isLiked } from '@/util/score';
import { emit } from 'process';
const props = defineProps<{
    scores?: Record<string, number>;
    disabled?: boolean;
}>();

const emits = defineEmits<{
    (e: 'rate'): void
    (e: 'unrate'): void
}>();

function click() {
    if (isLiked(props.scores ?? {})) {
        emits('unrate')
    } else {
        emits('rate');
    }
}
</script>

<style scoped>
.liked {
    background-color: greenyellow;
    font-size: 1.1em;
}
</style>