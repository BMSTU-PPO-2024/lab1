<template>
<v-dialog
    v-model="store.shown"
    max-width="500"
>
    <v-card class="pa-8">
        <v-card-title class="d-flex justify-space-between align-center">
            <h3>Создать ленту</h3>
            <v-btn
                icon
                variant="plain"
                @click="store.shown = false"
            >
                <v-icon>mdi-close</v-icon>
            </v-btn>
        </v-card-title>
  
        <v-card-text>
            <v-form ref="form">
                <v-row class="d-flex align-center">
                    <v-col cols="4">
                        <span class="text-base-bold">Название</span>
                    </v-col>
                    <v-col cols="8">
                        <v-text-field
                            v-model="store.feedName"
                            variant="underlined"
                            :rules="nameRules"
                        />
                    </v-col>
                </v-row>
  
                <v-row class="d-flex align-center">
                    <v-col cols="4">
                        <span class="text-base-bold">Видимость</span>
                    </v-col>
                    <v-col cols="8">
                        <v-checkbox
                            v-model="store.visibility"
                            :hide-details="true"
                        />
                    </v-col>
                </v-row>
  
                <v-row class="d-flex align-center">
                    <v-col cols="4">
                        <span class="text-base-bold">Теги</span>
                    </v-col>
                    <v-col cols="8">
                        <v-chip
                            v-for="(tag, index) in store.tags"
                            :key="index"
                            class="ma-1"
                            color="var(--color-primary)"
                            closable
                            @click:close="removeTag(tag)"
                        >
                            #{{ tag }}
                        </v-chip>
                    </v-col>
                </v-row>
                
                <v-row class="d-flex align-center flex-nowrap">
                    <v-col cols="8">
                        <v-text-field
                            v-model="tagInput"
                            variant="underlined"
                            placeholder="Введите тег"
                            @keyup.enter="addTag"
                        />
                    </v-col>

                    <v-col cols="8">
                        <v-btn
                            class="button-primary"
                            @click="addTag"
                        >
                            Добавить
                        </v-btn>
                    </v-col>
                </v-row>
            </v-form>
        </v-card-text>
  
        <v-card-actions>
            <v-btn
                class="button-primary"
                @click="createFeed"
            >
                Создать
            </v-btn>
            <v-btn
                variant="plain"
                @click="store.shown = false"
            >
                Отмена
            </v-btn>
            <v-spacer />
        </v-card-actions>
    </v-card>
</v-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useCreateFeedDialogStore } from '@/stores/create-feed-dialog-store';
import type { VForm, VTextField } from 'vuetify/components';

const store = useCreateFeedDialogStore();

const form = ref<VForm>();
const tagInput = ref('');

const nameRules: VTextField['rules'] = [
    (v) => !!v || 'Название обязательно',
    (v) => (v && v.length >= 3) || 'Минимум 3 символа',
];

const addTag = () => {
    const trimmedTag = tagInput.value.trim();
    if (trimmedTag && !store.tags.includes(trimmedTag)) {
        store.tags.push(trimmedTag);
    }
    tagInput.value = '';
};

const removeTag = (tag: string) => {
    store.tags = store.tags.filter((t) => t !== tag);
    console.log(store.tags)
};

const createFeed = async () => {
    const valid = await form.value?.validate();
    if (valid?.valid) {
        store.createFeed();
    }
};
</script>
