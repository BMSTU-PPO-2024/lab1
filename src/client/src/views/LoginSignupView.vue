<template>
    <div
        class="login-view"
    >
        <div class="login-view_logo-header-wrap">
            <img
                class="login-view_logo-img"
                src="https://media.tenor.com/t3dLLNaI50oAAAAM/cat-cats.gif"
                alt="logo"
            >
            <h2 class="login-view_header">Добро пожаловать в DevSpark!</h2>
        </div>

        <div
            v-if="errorMessage"
            class="login-view_error-message">
            {{ errorMessage }}
<!--            bebraaaaaa22-->
        </div>

        <div class="login-view_credential">
            <div class="login-view_credential-inputs">

<!--                <sk-input-->
<!--                    v-if="!isLogin"-->
<!--                    :upperText="'Имя пользователя'"-->
<!--                    :placeholder="'Имя пользователя'"-->
<!--                    v-model="userStore.username"-->
<!--                />-->
                <sk-input
                    :upperText="'Адрес электронной почты'"
                    :placeholder="'Адрес электронной почты'"
                    :modelValue="userStore.email"
                    @update:modelValue="updateEmail($event)"
                />
                <sk-input
                    :upperText="'Пароль'"
                    :placeholder="'Пароль'"
                    :type="'password'"
                    v-model="userStore.password"
                />
            </div>
            <div class="login-view_credential-buttons">
                <!--                <div class="login-view_submit-button-wrap">-->
                <sk-button
                    class="login-view_submit-button"
                    @click="submit"
                >
                    {{ submitButtonText }}
                </sk-button>
                <!--                </div>-->
            </div>
        </div>

        <div class="login-view_help-text">
            <template v-if="!isLogin">
                <span>Зарегестрированы?</span>
                <router-link to="/login">
                    Войти.
                </router-link>
            </template>
            <template v-else>
                <span>Нет аккаунта?</span>
                <router-link to="/signup">
                    Зарегестрироваться
                </router-link>
            </template>
        </div>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useUserStore} from "@/stores/userStore";

export default defineComponent({
    name: "login-view",
    setup() {
        const userStore = useUserStore();
        return {userStore};
    },
    props: {
        isLogin: {
            type: Boolean,
            default: true,
        }
    },
    data() {
        return {
            email: "Адрес электронной почты",
            password: "Пароль",
            errorMessage: "",
        };
    },
    methods: {
        submit() {
            if (this.isLogin) {
                this.userStore.login();
            } else {
                this.userStore.signup()
                    .catch((err) => {
                        // console.log("in login signup:", err);
                        if (err.response.data?.message) {
                            this.errorMessage = err.response.data.message;
                        } else {
                            this.errorMessage = "Что то пошло не так...";
                        }
                    });
            }
        },
        updateEmail(value: string) {
            // console.log("value", value);
            // console.log("store", this.userStore.email);
            this.userStore.email = value;
        }
    },
    computed: {
        submitButtonText() {
            return this.isLogin ? "Войти" : "Зарегестрироваться";
        },
        helpText() {

        }
    }

});
</script>

<style scoped>
.login-view {
    /*display: flex;*/
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
    min-height: 80vh;
    /*gap: 2em;*/
}

.login-view_logo-header-wrap {
    margin-bottom: 50px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.login-view_credential-buttons {
    display: flex;
    width: 50%;
}

.login-view_credential {
    display: flex;
    flex-direction: column;
    gap: 2em;
    align-items: center;
}

.login-view_submit-button-wrap {

}

.login-view_logo-img {
    width: 50px;
}

.login-view_header {
    color: var(--color-heading);
    font-size: 2em;
    font-weight: 500;
    /*letter-spacing: -1px;*/
}

.login-view_submit-button {
    width: max(10em, fit-content);
    height: 2em;
}

.login-view_credential-inputs {
    display: flex;
    flex-direction: column;
    gap: 1em;
    width: 50%;
}

.login-view_help-text {
    margin: 2em auto;
    display: flex;
    justify-content: center;
    gap: 0.5em;
}

.login-view_error-message {
    display: flex;
    place-items: center;
    flex-direction: column;
    margin-bottom: 1em;
    color: var(--color-error);
    font-weight: bold;
}
</style>