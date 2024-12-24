import { useUserStore } from "@/stores/user-store";

export function isOwner(owned: { ownerId?: string }, selfId?: string) {
    if (!selfId) {
        const userStore = useUserStore();
        selfId = userStore.self?.id;
    }
    return owned.ownerId === selfId;
}