import { useUserStore } from "@/stores/user-store";


export function scorePositive(scores: Record<string, number>) {
    return Object.values(scores).reduce((acc, val) => val < 0 ? acc : acc + val, 0);
}

export function scoreNegative(scores: Record<string, number>) {
    return Object.values(scores).reduce((acc, val) => val > 0 ? acc : acc + val, 0);
}

export function scoreTotal(scores: Record<string, number>) {
    return Object.values(scores).reduce((acc, val) => acc + val, 0);
}

export function isLiked(scores: Record<string, number>, selfId?: string) {
    if (!selfId) {
        const userStore = useUserStore();
        selfId = userStore.self?.id;
    }
    return Object.keys(scores).some(k => k === selfId);
}