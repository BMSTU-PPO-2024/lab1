import { useApi } from "@/api/api";
import { defineStore } from "pinia";
import { useChannelStore } from "./channel-store";
import { useRouter } from 'vue-router'

export const useEditChannelDialogStore = defineStore('edit-channel-modal-store', () => {
    const api = useApi();
    const channelStore = useChannelStore();
    
    const shown = ref(false);
    const channelName = ref('');
    const visibility = ref(true);

    const channel = computed(() => channelStore.currentChannel);

    watch(shown, () => {
        if (shown.value && channel.value) {
            channelName.value = channel.value.name ?? '';
            visibility.value = !!channel.value.visible;
        }
    })

    async function saveChannel() {
        await channelStore.updateChannel({
            ...channel.value,
            name: channelName.value,
            visible: visibility.value,
        })
        shown.value = false;
        channelStore.fetchChannel(channel.value?.id ?? '');
    }

    async function deleteChannel() {
        await channelStore.deleteChannel(channel.value?.id ?? '');
        shown.value = false;
    }
    
    return {
        shown,
        channelName,
        visibility,

        channel,

        saveChannel,
        deleteChannel,
    }
})