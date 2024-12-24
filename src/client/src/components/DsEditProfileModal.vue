<template>
<v-dialog
    v-model="store.shown"
    max-width="600"
>
    <v-card class="pa-8">
        <v-card-title class="d-flex justify-space-between align-center">
            <h3>Редактировать профиль</h3>
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
                        <span class="text-base-bold">Никнейм</span>
                    </v-col>
                    <v-col cols="8">
                        <v-text-field
                            v-model="store.nickname"
                            variant="underlined"
                            :rules="nameRules"
                        />
                    </v-col>
                </v-row>
  
                <v-row class="d-flex align-center">
                    <v-col cols="4">
                        <span class="text-base-bold">Почта</span>
                    </v-col>
                    <v-col cols="8">
                        <v-text-field
                            v-model="store.email"
                            variant="underlined"
                            type="email"
                            :rules="emailRules"
                        />
                    </v-col>
                </v-row>
  
                <v-row class="align-center">
                    <v-col cols="4">
                        <span class="text-base-bold">О себе</span>
                    </v-col>
                    <v-col cols="8">
                        <v-textarea
                            v-model="store.about"
                            variant="underlined"
                            rows="8"
                            no-resize
                            hide-details
                            :rules="aboutRules"
                            style="height: 250px"
                        />
                    </v-col>
                </v-row>
            </v-form>
        </v-card-text>
  
        <v-card-actions>
            <v-btn
                class="button-primary"
                @click="saveProfile"
            >
                Сохранить
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
import { useEditProfileDialogStore } from '@/stores/edit-profile-dialog-store';
import type { VForm, VTextField, VTextarea } from 'vuetify/components';

const store = useEditProfileDialogStore();
const form = ref<VForm>();

// Validation rules
const nameRules: VTextField['rules'] = [
    (v) => !!v || 'Никнейм обязателен',
    (v) => (v && v.length >= 3) || 'Минимум 3 символа',
];

const emailRules: VTextField['rules'] = [
    (v) => !!v || 'Почта обязательна',
    // (v) => /.+@.+\..+/.test(v) || 'Введите корректный email',
];

const aboutRules: VTextarea['rules'] = [
    // (v) => (v && v.length <= 500) || 'Максимум 500 символов',
];

// Save profile function
const saveProfile = async () => {
    const valid = await form.value?.validate();
    if (valid?.valid) {
        store.updateProfile();
    }
};
</script>
  