<template>
<v-app>
    <v-container>
        <v-form
            ref="form"
            v-model="valid"
            @submit.prevent="submit"
        >
            <v-text-field
                v-model="name"
                label="Имя"
                :rules="nameRules"
                required
            />

            <v-text-field
                v-model="email"
                label="Электронная почта"
                :rules="emailRules"
                required
            />

            <v-btn
                type="submit"
                :disabled="!valid"
                color="primary"
            >
                Отправить
            </v-btn>
        </v-form>
        <p>
            id: {{ (route.params as any).id }}
        </p>
        <p>
            {{ authStore.tokenData?.email }}
            {{ authStore.tokenData?.id }}
            {{ authStore.tokenData?.nickname }}
        </p>
    </v-container>
</v-app>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth-store';
import { ref } from 'vue';
import type { VForm, VTextField } from 'vuetify/components';

const form = ref<VForm>();
const valid = ref(false);
const name = ref('');
const email = ref('');

const route = useRoute();
const authStore = useAuthStore();

const nameRules: VTextField['rules'] = [
    (v) => !!v || 'Имя обязательно',
    (v) => (v && v.length >= 3) || 'Минимум 3 символа',
];

const emailRules: VTextField['rules'] = [
    (v) => !!v || 'Электронная почта обязательна',
    // v => /^[^\\s@]+@[^\s@]+\\.[^\s@]+$/.test(v) || 'Введите корректный email',
];

const submit = () => {
    if (form.value?.validate()) {
        alert(`Форма отправлена! Имя: ${name.value}, Email: ${email.value}`);
    }
};
</script>
