<template>
<v-chip
    class="ma-1"
    color="var(--color-primary)"
    outlined
    :closable="closable"
    @click:close="$emit('close')"
>
    #{{ name }}
</v-chip>
</template>

<script setup lang="ts">
const props = defineProps<{
    name: string;
    closable?: boolean;
}>();

const emit = defineEmits<{
  (e: 'close'): void
}>()
</script>