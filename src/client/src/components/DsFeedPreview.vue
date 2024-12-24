<template>
<v-card
    class="pa-8 d-flex justify-center base-hover cursor-pointer"
    color="#ffe4e1"
    height="200"
    variant="flat"
    @click="$emit('open')"
>
    <div class="d-flex flex-column align-start justify-content-start fill-height w-100">
        <h4 class="text-center">
            {{ name }}
        </h4>
        <v-chip
            color="orange"
            class="mt-2"
        >
            <!-- {{ profileStore.selectedTab === 'channels' ? 'Канал' : 'Лента' }} -->
            Лента
        </v-chip>
    </div>
</v-card>
</template>

<script setup lang=ts>
const props = defineProps<{
    name?: string;
}>();

const emits = defineEmits<{
    (e: 'open'): void
}>()
</script>