import { useApi } from '@/api/api';
import type { LoginData, RegisterData } from '@/api/types';
import { jwtDecode } from 'jwt-decode';
import { defineStore } from 'pinia';

export const TOKEN_KEY = 'auth_token';

export interface TokenData {
    id: string;
    nickname: string;
    email: string;
    exp: number;
}

export const useAuthStore = defineStore('auth', () => {
    const api = useApi().value;
    const router = useRouter();

    const tokenData = ref<TokenData | null>(null);

    const token = ref<string | null>('');
    const tokenValue = localStorage.getItem(TOKEN_KEY);
    if (tokenValue != null) {
        token.value = tokenValue;
    }

    const isAuthorized = computed(() => Boolean(token.value && token.value !== '' && !isExpiredToken.value));

    const isExpiredToken = computed(() => {
        const now = Date.now();
        return Boolean(tokenData.value && (tokenData.value.exp * 1000 < now));
    });

    watchEffect(() => {
        try {
            tokenData.value = jwtDecode<TokenData>(token.value ?? '');
            console.log(tokenData.value);
        } catch {}
        localStorage.setItem(TOKEN_KEY, token.value ?? '');
    });


    async function login(login: LoginData) {
        const {
            data, // only present if 2XX response
            error, // only present if 4XX or 5XX response
        } = await api.POST('/login', {
            body: login,
        });
        if (data?.token) {
            token.value = data.token;
        } else {
            throw Error("Can't get token from login: " + error);
        }
    }

    async function register(register: RegisterData) {
        const { data, error } = await api.POST('/register', {
            body: register,
        });
        if (data?.token) {
            token.value = data.token;
        } else {
            throw Error("Can't get token from register: " + error);
        }
    }

    function logout() {
        token.value = null;
        router.push({ path: '/login' });
    }

    return {
        token,
        isAuthorized,
        tokenData,
        isExpiredToken,
        
        login,
        register,
        logout,
    };
});
