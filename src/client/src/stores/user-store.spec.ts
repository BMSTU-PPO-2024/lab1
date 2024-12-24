import { setActivePinia } from 'pinia';
import { createTestingPinia } from '@pinia/testing';
import { useUserStore } from '@/stores/user-store';
import { vi, describe, it, beforeEach, expect } from 'vitest';

describe('User Store', () => {
    const mockApi = vi.hoisted(() => ({
        GET: vi.fn(),
    }));

    vi.mock('@/api/api', () => ({ useApi: () => ({ value: mockApi }) }));

    let userStore: ReturnType<typeof useUserStore>;

    beforeEach(() => {
        vi.clearAllMocks();
        setActivePinia(createTestingPinia({
            createSpy: vi.fn,
            stubActions: false,
        }));

        userStore = useUserStore();
    });

    it('fetches the current user and updates store', async () => {
        const mockUser = { id: '1', name: 'John Doe' };
        mockApi.GET.mockResolvedValueOnce({ data: mockUser });

        await userStore.fetchSelf();

        expect(mockApi.GET).toHaveBeenCalledWith('/users');
        expect(userStore.self).toEqual(mockUser);
    });

    it('handles fetch failure gracefully', async () => {
        mockApi.GET.mockResolvedValueOnce({ data: null });

        await userStore.fetchSelf();

        expect(mockApi.GET).toHaveBeenCalledWith('/users');
        expect(userStore.self).toBeFalsy();
    });
});
