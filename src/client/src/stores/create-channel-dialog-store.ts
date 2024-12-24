import { useApi } from "@/api/api";
import { defineStore } from "pinia";
import { useChannelStore } from "./channel-store";

export const useCreateChannelDialogStore = defineStore('create-channel-modal-store', () => {
    const api = useApi();
    const channelStore = useChannelStore();
    
    const shown = ref(false);
    const channelName = ref('');
    const visibility = ref(true);

    async function createChannel() {
        const { data, error } = await api.value.POST('/channels', {
            body: {
                name: channelName.value,
                visible: visibility.value,
            }
        });
        if (data) {
            shown.value = false;
            channelName.value = '';
            visibility.value = true;
            channelStore.fetchSelfChannels();
        }
    }
    
    return {
        shown,
        channelName,
        visibility,

        createChannel,
    }
})