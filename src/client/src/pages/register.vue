<template>
<v-container class="fill-height">
    <v-row
        justify="center"
        align="center"
    >
        <v-col
            cols="5"
            class="d-flex ga-12 flex-column"
        >
            <h1 class="text-center">
                Регистрация в <span style="color:var(--color-primary)">DevSpark</span>
            </h1>

            <v-form
                ref="form"
                v-model="valid"
                @submit.prevent
            >
                <div class="d-flex justify-space-between align-center">
                    <span class="heading-h5-base w-25 pb-5">Почта</span>
                    <v-text-field 
                        v-model="email" 
                        label="Почта" 
                        :rules="emailRules"
                        required
                    />
                </div>
                <div class="d-flex justify-space-between align-center">
                    <span class="heading-h5-base w-25 pb-5">Пароль</span>
                    <v-text-field
                        v-model="password"
                        label="Пароль"
                        :rules="passwordRules"
                        required
                    />
                </div>
            </v-form>

            <div class="d-flex ga-4">
                <v-btn
                    color="var(--color-primary)"
                    class="text-white"
                    @click="submit"
                >
                    Зарегестрироваться
                </v-btn>
    
                <v-btn
                    variant="text"
                    to="/login"
                >
                    Вход
                </v-btn>
            </div>
        </v-col>
    </v-row>
</v-container>
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth-store';
import { ref } from 'vue';
import type { VForm, VTextField } from 'vuetify/components';

const authStore = useAuthStore();
const router = useRouter();

watchEffect(() => {
    if (authStore.isAuthorized) {
        router.push({ 
            path: '/main'
        });
    }
})

const form = ref<VForm>();
const valid = ref(false);
const password = ref('');
const email = ref('');

const emailRules: VTextField['rules'] = [
    (v) => !!v || 'Почта обязательна',
    (v) => (v && v.length >= 3) || 'Минимум 3 символа',
];

const passwordRules: VTextField['rules'] = [
    (v) => !!v || 'Пароль обязателен',
    (v) => (v && v.length >= 3) || 'Минимум 3 символа',
];

const submit = async () => {
    const valid = await form.value?.validate();
    if (valid?.valid) {
        authStore.register({
            email: email.value,
            password: password.value,
        })
    }
};
</script>
