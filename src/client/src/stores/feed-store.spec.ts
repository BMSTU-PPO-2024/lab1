import { setActivePinia } from 'pinia';
import { createTestingPinia } from '@pinia/testing';
import { useFeedStore } from '@/stores/feed-store';
import { useUserStore } from '@/stores/user-store';
import { useTagStore } from '@/stores/tag-store';
import { vi, describe, it, beforeEach, expect } from 'vitest';
import { mockedStore, type MockedStore } from './mocked-store';

describe('Feed Store', () => {
    const mockApi = vi.hoisted(() => ({
        GET: vi.fn(),
        DELETE: vi.fn(),
        PATCH: vi.fn(),
    }));

    vi.mock('@/api/api', () => ({ useApi: () => ({ value: mockApi }) }));

    let feedStore: ReturnType<typeof useFeedStore>;
    let userStore: MockedStore<typeof useUserStore>;
    let tagStore: MockedStore<typeof useTagStore>;

    beforeEach(() => {
        vi.clearAllMocks();
        setActivePinia(createTestingPinia({
            createSpy: vi.fn,
            stubActions: false,
        }));
        
        mockApi.GET.mockResolvedValueOnce({ data: { id: 'user123' }});

        feedStore = mockedStore(useFeedStore);
        userStore = mockedStore(useUserStore);
        tagStore = mockedStore(useTagStore);
    });


    it('fetches self feeds on successful API call', async () => {
        userStore.self = { id: 'user123' };
        const mockFeeds = [{ id: '1', title: 'Feed 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockFeeds });

        await feedStore.fetchSelfFeeds();


        expect(mockApi.GET).toHaveBeenCalledWith('/users/{userId}/feeds', {
            params: { path: { userId: 'user123' } },
        });
        expect(feedStore.selfFeeds).toEqual(mockFeeds);
    });

    it('does not fetch self feeds if no user', async () => {
        userStore.self = undefined;

        await feedStore.fetchSelfFeeds();

        expect(mockApi.GET).toHaveBeenCalledTimes(1);
    });

    it('deletes a feed and refreshes self feeds on success', async () => {
        userStore.self = { id: 'user123' };
        mockApi.DELETE.mockResolvedValueOnce({});
        const mockFeeds = [{ id: '1', title: 'Feed 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockFeeds });

        await feedStore.deleteFeed('feed123');

        expect(mockApi.DELETE).toHaveBeenCalledWith('/feeds/{feedId}', {
            params: { path: { feedId: 'feed123' } },
        });
        expect(feedStore.selfFeeds).toEqual(mockFeeds);
    });

    it('updates a feed with tags and refreshes self feeds on success', async () => {
        userStore.self = { id: 'user123' };
        tagStore.createTags = vi.fn().mockResolvedValueOnce(undefined);
        tagStore.fetchAllTags = vi.fn().mockResolvedValueOnce(undefined);
        tagStore.tagsByNames = vi.fn().mockReturnValue([{ id: 'tag123' }]);

        const mockFeed = { id: 'feed123', title: 'Updated Feed', tagIds: [] };
        mockApi.PATCH.mockResolvedValueOnce({});
        const mockFeeds = [{ id: '1', title: 'Feed 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockFeeds });

        await feedStore.updateFeed(mockFeed, ['New Tag']);

        expect(tagStore.createTags).toHaveBeenCalledWith(['New Tag']);
        expect(tagStore.fetchAllTags).toHaveBeenCalled();
        expect(tagStore.tagsByNames).toHaveBeenCalledWith(['New Tag']);
        expect(mockApi.PATCH).toHaveBeenCalledWith('/feeds/{feedId}', {
            params: { path: { feedId: 'feed123' } },
            body: { ...mockFeed, tagIds: ['tag123'] },
        });
        expect(feedStore.selfFeeds).toEqual(mockFeeds);
    });

    it('fetches current feeds with correct params', async () => {
        const mockParams = { page: 1, batch: 10 };
        const mockFeeds = [{ id: '1', title: 'Main Feed' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockFeeds });

        await feedStore.getFeeds(mockParams);

        expect(mockApi.GET).toHaveBeenCalledWith('/feeds', {
            params: { query: mockParams },
        });
        expect(feedStore.currentFeeds).toEqual(mockFeeds);
    });
});
