import { setActivePinia } from 'pinia';
import { useChannelStore } from '@/stores/channel-store';
import { useUserStore } from '@/stores/user-store';
import { vi, describe, it, beforeEach, expect } from 'vitest';
import { createTestingPinia } from '@pinia/testing'
import { mockedStore, type MockedStore } from './mocked-store';


describe('Channel Store', () => {
    const mockApi = vi.hoisted(() => ({
        GET: vi.fn(),
        DELETE: vi.fn(),
        PATCH: vi.fn(),
    }));
    vi.mock('@/api/api', () => ({ useApi: () => ({ value: mockApi }) }));

    let channelStore: ReturnType<typeof useChannelStore>;
    let userStore: MockedStore<typeof useUserStore>;

    beforeEach(() => {
        vi.clearAllMocks();
        setActivePinia(createTestingPinia({
            createSpy: vi.fn,
            stubActions: false,
        }));

        mockApi.GET.mockResolvedValueOnce({ data: { id: 'user123' }});
        channelStore = mockedStore(useChannelStore);
        userStore = mockedStore(useUserStore);
    });

    it('fetches self channels on successful API call', async () => {
        userStore.self = { id: 'user123' };
        const mockChannels = [{ id: '1', name: 'Channel 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockChannels });

        await channelStore.fetchSelfChannels();

        expect(mockApi.GET).toHaveBeenCalledWith("/users/{userId}/channels", {
            params: { path: { userId: 'user123' } },
        });
        expect(channelStore.fetchSelfChannels).toBeCalledTimes(1);
        expect(channelStore.selfChannels).toEqual(mockChannels);
    });

    it('does not fetch self channels if no user', async () => {
        userStore.self = undefined;
        await channelStore.fetchSelfChannels();
        expect(mockApi.GET).toHaveBeenCalledTimes(1);
    });

    it('deletes a channel and refreshes self channels on success', async () => {
        userStore.self = { id: 'user123' };
        mockApi.DELETE.mockResolvedValueOnce({});
        const mockChannels = [{ id: '1', name: 'Channel 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockChannels });

        await channelStore.deleteChannel('channel123');

        expect(mockApi.DELETE).toHaveBeenCalledWith('/channels/{channelId}', {
            params: { path: { channelId: 'channel123' } },
        });
        expect(channelStore.selfChannels).toEqual(mockChannels);
    });

    it('updates a channel and refreshes self channels on success', async () => {
        userStore.self = { id: 'user123' };
        const mockChannel = { id: 'channel123', name: 'Updated Channel' };
        mockApi.PATCH.mockResolvedValueOnce({});
        const mockChannels = [{ id: '1', name: 'Channel 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockChannels });

        await channelStore.updateChannel(mockChannel);

        expect(mockApi.PATCH).toHaveBeenCalledWith('/channels/{channelId}', {
            params: { path: { channelId: 'channel123' } },
            body: mockChannel,
        });
        expect(channelStore.selfChannels).toEqual(mockChannels);
    });

    it('fetches current channels with correct params', async () => {
        const mockParams = { page: 1, batch: 10 };
        const mockChannels = [{ id: '1', name: 'Main Channel' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockChannels });

        await channelStore.getChannels(mockParams);

        expect(mockApi.GET).toHaveBeenCalledWith('/channels', {
            params: { query: mockParams },
        });
        expect(channelStore.currentChannels).toEqual(mockChannels);
    });
});
