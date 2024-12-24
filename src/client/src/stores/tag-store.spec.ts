import { setActivePinia } from 'pinia';
import { createTestingPinia } from '@pinia/testing';
import { useTagStore } from '@/stores/tag-store';
import { vi, describe, it, beforeEach, expect } from 'vitest';
import { mockedStore, type MockedStore } from './mocked-store';

describe('Tag Store', () => {
    const mockApi = vi.hoisted(() => ({
        GET: vi.fn(),
        POST: vi.fn(),
    }));

    vi.mock('@/api/api', () => ({ useApi: () => ({ value: mockApi }) }));

    let tagStore: ReturnType<typeof useTagStore>;

    beforeEach(() => {
        vi.clearAllMocks();
        setActivePinia(createTestingPinia({
            createSpy: vi.fn,
            stubActions: false,
        }));

        tagStore = useTagStore();
    });



    it('fetches tags by IDs and updates store', async () => {
        const mockTags = [{ id: '1', name: 'Tag 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockTags[0] });
        mockApi.GET.mockResolvedValueOnce({ data: mockTags[0] });

        await tagStore.fetchTags(['1', '2']);

        expect(mockApi.GET).toHaveBeenCalledWith('/tags/{tagId}', {
            params: { path: { tagId: '1' } },
        });
        expect(tagStore.tags['1']).toEqual(mockTags[0]);
    });

    it('fetches all tags and updates store', async () => {
        const mockTags = [{ id: '1', name: 'Tag 1' }, { id: '2', name: 'Tag 2' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockTags });

        await tagStore.fetchAllTags();

        expect(mockApi.GET).toHaveBeenCalledWith('/tags', {
            params: { query: { batch: 1000 } },
        });
        expect(tagStore.tags['1']).toEqual(mockTags[0]);
        expect(tagStore.tags['2']).toEqual(mockTags[1]);
    });

    it('fetches tags of a value containing tag IDs', async () => {
        const mockTags = [{ id: 'id123', name: 'Tag 1' }];
        mockApi.GET.mockResolvedValueOnce({ data: mockTags[0] });

        await tagStore.fetchTagsOf({ tagIds: ['id123'] });

        expect(mockApi.GET).toHaveBeenCalledWith('/tags/{tagId}', {
            params: { path: { tagId: 'id123' } },
        });
        expect(tagStore.tags).toEqual({ 'id123': mockTags[0] });
    });

    it('creates new tags and updates store', async () => {
        const mockTag = { id: '1', name: 'New Tag' };
        mockApi.POST.mockResolvedValueOnce({ data: mockTag });

        await tagStore.createTags(['New Tag']);

        expect(mockApi.POST).toHaveBeenCalledWith('/tags', { body: { name: 'New Tag' } });
        expect(tagStore.tags['1']).toEqual(mockTag);
    });

    it('does not create tags if they already exist', async () => {
        tagStore.tags['1'] = { id: '1', name: 'Existing Tag' };

        await tagStore.createTags(['Existing Tag']);

        expect(mockApi.POST).not.toHaveBeenCalled();
    });

    it('returns tags by names', () => {
        tagStore.tags['1'] = { id: '1', name: 'Tag 1' };
        tagStore.tags['2'] = { id: '2', name: 'Tag 2' };

        const result = tagStore.tagsByNames(['Tag 1', 'Tag 3']);

        expect(result).toEqual([{ id: '1', name: 'Tag 1' }]);
    });
});
