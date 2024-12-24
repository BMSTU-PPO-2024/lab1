<template>
<v-dialog
    v-model="store.shown"
    max-width="500"
>
    <v-card class="pa-8">
        <v-card-title class="d-flex justify-space-between align-center">
            <h3>Редактировать канал</h3>
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
                            v-model="store.channelName"
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
                            label="Публичный"
                        />
                    </v-col>
                </v-row>
            </v-form>
        </v-card-text>
    
        <v-card-actions>
            <v-btn
                class="button-primary"
                @click="saveChannel"
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
            <v-btn
                class="button-danger"
                color="error"
                @click="deleteChannel"
            >
                Удалить
            </v-btn>
        </v-card-actions>
    </v-card>
</v-dialog>
</template>
    
<script setup lang="ts">
import { useEditChannelDialogStore } from '@/stores/edit-channel-dialog-store';
import type { VForm, VTextField } from 'vuetify/components';
import { ref } from 'vue';

const store = useEditChannelDialogStore();
const router = useRouter();

const form = ref<VForm>();

const nameRules: VTextField['rules'] = [
    (v) => !!v || 'Название обязательно',
    (v) => (v && v.length >= 3) || 'Минимум 3 символа',
];

const saveChannel = async () => {
    const valid = await form.value?.validate();
    if (valid?.valid) {
        store.saveChannel();
    }
};

const deleteChannel = async () => {
    await store.deleteChannel();
    router.back()
};
</script>
    