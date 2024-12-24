import { setActivePinia } from 'pinia';
import { createTestingPinia } from '@pinia/testing';
import { usePostStore } from '@/stores/post-store';
import { useTagStore } from '@/stores/tag-store';
import { vi, describe, it, beforeEach, expect } from 'vitest';
import { mockedStore, type MockedStore } from './mocked-store';
import type { Post } from '@/api/types';

describe('Post Store', () => {
    const mockApi = vi.hoisted(() => ({
        GET: vi.fn(),
        POST: vi.fn(),
        PATCH: vi.fn(),
        DELETE: vi.fn(),
    }));

    vi.mock('@/api/api', () => ({ useApi: () => ({ value: mockApi }) }));

    let postStore: ReturnType<typeof usePostStore>;
    let tagStore: MockedStore<typeof useTagStore>;

    beforeEach(() => {
        vi.clearAllMocks();
        setActivePinia(createTestingPinia({
            createSpy: vi.fn,
            stubActions: false,
        }));

        postStore = mockedStore(usePostStore);
        tagStore = mockedStore(useTagStore);
    });

    // it('fetches channel posts on successful API call', async () => {
    //     const mockPosts = [{ id: '1', title: 'Post 1' }];
    //     mockApi.GET.mockResolvedValueOnce({ data: mockPosts });

    //     await postStore.fetchChannelPosts('channel123');

    //     expect(mockApi.GET).toHaveBeenCalledWith('/channels/{channelId}/posts', {
    //         params: { path: { channelId: 'channel123' } },
    //     });
    //     expect(postStore.currentPosts).toEqual(mockPosts);
    // });

    it('fetches feed posts on successful API call', async () => {
        const mockPosts = [{ id: '1', title: 'Post 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockPosts });

        await postStore.fetchFeedPosts('feed123');

        expect(mockApi.GET).toHaveBeenCalledWith('/feeds/{feedId}/posts', {
            params: { path: { feedId: 'feed123' } },
        });
        expect(postStore.currentPosts).toEqual(mockPosts);
    });

    it('fetches a single post by ID on successful API call', async () => {
        const mockPost = { id: 'post123', title: 'Post 123' };
        mockApi.GET.mockResolvedValueOnce({ data: mockPost });

        await postStore.fetchPost('post123');

        expect(mockApi.GET).toHaveBeenCalledWith('/posts/{postId}', {
            params: { path: { postId: 'post123' } },
        });
        expect(postStore.currentPost).toEqual(mockPost);
    });

    it('creates a post with tags and handles API call', async () => {
        tagStore.createTags = vi.fn().mockResolvedValueOnce(undefined);
        tagStore.fetchAllTags = vi.fn().mockResolvedValueOnce(undefined);
        tagStore.tagsByNames = vi.fn().mockReturnValue([{ id: 'tag123' }]);

        const mockPost: Post = { id: 'post123', title: 'New Post', tagIds: [] };
        mockApi.POST.mockResolvedValueOnce({});

        await postStore.createPost(mockPost, ['New Tag']);

        expect(tagStore.createTags).toHaveBeenCalledWith(['New Tag']);
        expect(tagStore.fetchAllTags).toHaveBeenCalled();
        expect(tagStore.tagsByNames).toHaveBeenCalledWith(['New Tag']);
        expect(mockApi.POST).toHaveBeenCalledWith('/channels/{channelId}/posts', {
            params: { path: { channelId: mockPost.channelId ?? '' } },
            body: { ...mockPost, tagIds: ['tag123'] },
        });
    });

    it('updates a post with tags and handles API call', async () => {
        tagStore.createTags = vi.fn().mockResolvedValueOnce(undefined);
        tagStore.fetchAllTags = vi.fn().mockResolvedValueOnce(undefined);
        tagStore.tagsByNames = vi.fn().mockReturnValue([{ id: 'tag123' }]);

        const mockPost = { id: 'post123', title: 'Updated Post', tagIds: [] };
        mockApi.PATCH.mockResolvedValueOnce({});

        await postStore.updatePost(mockPost, ['Updated Tag']);

        expect(tagStore.createTags).toHaveBeenCalledWith(['Updated Tag']);
        expect(tagStore.fetchAllTags).toHaveBeenCalled();
        expect(tagStore.tagsByNames).toHaveBeenCalledWith(['Updated Tag']);
        expect(mockApi.PATCH).toHaveBeenCalledWith('/posts/{postId}', {
            params: { path: { postId: 'post123' } },
            body: { ...mockPost, tagIds: ['tag123'] },
        });
    });

    it('deletes a post and handles API call', async () => {
        mockApi.DELETE.mockResolvedValueOnce({});

        await postStore.deletePost('post123');

        expect(mockApi.DELETE).toHaveBeenCalledWith('/posts/{postId}', {
            params: { path: { postId: 'post123' } },
        });
    });

    it('rates a post and handles API call', async () => {
        mockApi.POST.mockResolvedValueOnce({});

        await postStore.ratePost('post123');

        expect(mockApi.POST).toHaveBeenCalledWith('/posts/{postId}/rates', {
            params: { path: { postId: 'post123' } },
        });
    });

    it('unrates a post and handles API call', async () => {
        mockApi.DELETE.mockResolvedValueOnce({});

        await postStore.unratePost('post123');

        expect(mockApi.DELETE).toHaveBeenCalledWith('/posts/{postId}/rates', {
            params: { path: { postId: 'post123' } },
        });
    });
});
