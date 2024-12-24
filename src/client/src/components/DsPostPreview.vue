<template>
<v-card
    class="pa-8 d-flex align-center justify-center base-hover cursor-pointer"
    color="#ffe4e1"
    height="200"
    variant="flat"
>
    <div class="d-flex flex-column w-100 h-100">
        <!-- Заголовок -->
        <div 
            class="heading-h5-base overflow-hidden"
            style="text-overflow: ellipsis; white-space: nowrap;"
        >
            {{ post.title }}
        </div>
        <!-- Автор и дата -->
        <div class="text-caption mb-4">
            {{ userStore.getUser(post.ownerId ?? '')?.nickname ?? post.ownerId }} <br>
            {{ post.created }}
        </div>
        <!-- Лайки и дизлайки -->
        <v-row
            align="center"
            justify="space-between"
            class="mt-auto"
        >
            <div>
                <v-btn
                    variant="plain"
                >
                    {{ likes }}
                    <v-icon
                        class="pl-2"
                        color="green"
                    >
                        mdi-arrow-up
                    </v-icon>
                </v-btn>
                <v-btn
                    variant="plain"
                >
                    {{ dislikes }}
                    <v-icon
                        class="pl-2"
                        color="red"
                    >
                        mdi-arrow-down
                    </v-icon>
                </v-btn>
            </div>
        </v-row>
    </div>
</v-card>
</template>

<script setup lang="ts">
import type { Post } from '@/api/types';
import { useUserStore } from '@/stores/user-store';
import { scorePositive } from '@/util/score';

const userStore = useUserStore();

const props = defineProps<{
    post: Post;
}>();

const likes = computed(() => {
    return scorePositive(props.post.scores ?? {});
});

const dislikes = computed(() => {
    return scorePositive(props.post.scores ?? {});
});
</script>