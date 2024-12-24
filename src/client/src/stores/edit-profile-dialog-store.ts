import { useApi } from "@/api/api";
import { defineStore } from "pinia";
import { useUserStore } from "./user-store";

export const useEditProfileDialogStore = defineStore('edit-profile-dialog-store', () => {
    const api = useApi();
    const userStore = useUserStore();

    // State
    const shown = ref(false);
    const nickname = ref<string>('');
    const email = ref<string>('');
    const about = ref<string>('');

    // Function to open the modal and prefill data
    function openDialog() {
        nickname.value = userStore.self?.nickname || '';
        email.value = userStore.self?.email || '';
        about.value = userStore.self?.about || '';
        shown.value = true;
    }

    // Function to close the modal
    function closeDialog() {
        shown.value = false;
    }

    // Function to update profile
    async function updateProfile() {
        const { data, error } = await api.value.PATCH('/users', {
            body: {
                nickname: nickname.value,
                email: email.value,
                about: about.value,
            }
        });


        if (!error) {
            userStore.fetchSelf();
            closeDialog(); // Close modal and reset fields on success
        } else {
            console.error('Failed to update profile:', error);
        }
    }

    return {
        // State
        shown,
        nickname,
        email,
        about,

        // Methods
        openDialog,
        closeDialog,
        updateProfile,
    };
});
