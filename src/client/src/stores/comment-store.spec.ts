import { setActivePinia } from 'pinia';
import { createTestingPinia } from '@pinia/testing';
import { useCommentStore } from '@/stores/comment-store';
import { vi, describe, it, beforeEach, expect } from 'vitest';

describe('Comment Store', () => {
  const mockApi = vi.hoisted(() => ({
    POST: vi.fn(),
    GET: vi.fn(),
    PATCH: vi.fn(),
    DELETE: vi.fn(),
  }));
  vi.mock('@/api/api', () => ({ useApi: () => ({ value: mockApi }) }));

  let commentStore: ReturnType<typeof useCommentStore>;

  beforeEach(() => {
    vi.clearAllMocks();
    setActivePinia(createTestingPinia({
      createSpy: vi.fn,
      stubActions: false,
    }));

    commentStore = useCommentStore();
  });

  it('creates a comment with correct API call', async () => {
    const mockComment = { postId: 'post123', content: 'New comment' };
    mockApi.POST.mockResolvedValueOnce({});

    await commentStore.createComment(mockComment);

    expect(mockApi.POST).toHaveBeenCalledWith('/posts/{postId}/comment', {
      params: { path: { postId: 'post123' } },
      body: mockComment,
    });
  });

  it('fetches comments for a specific post', async () => {
    const postId = 'post123';
    const mockComments = [{ id: '1', content: 'First comment' }];
    mockApi.GET.mockResolvedValueOnce({ data: mockComments });

    await commentStore.fetchComments(postId);

    expect(mockApi.GET).toHaveBeenCalledWith('/posts/{postId}/comments', {
      params: { path: { postId } },
    });
    expect(commentStore.currentComments).toEqual(mockComments);
  });

  it('updates a comment with correct API call', async () => {
    const mockComment = { id: 'comment123', content: 'Updated content' };
    mockApi.PATCH.mockResolvedValueOnce({});

    await commentStore.updateComment(mockComment);

    expect(mockApi.PATCH).toHaveBeenCalledWith('/comments/{commentId}', {
      params: { path: { commentId: 'comment123' } },
      body: mockComment,
    });
  });

  it('deletes a comment with correct API call', async () => {
    const commentId = 'comment123';
    mockApi.DELETE.mockResolvedValueOnce({});

    await commentStore.deleteComment(commentId);

    expect(mockApi.DELETE).toHaveBeenCalledWith('/comments/{commentId}', {
      params: { path: { commentId } },
    });
  });

  it('rates a comment with correct API call', async () => {
    const commentId = 'comment123';
    mockApi.POST.mockResolvedValueOnce({});

    await commentStore.rateComment(commentId);

    expect(mockApi.POST).toHaveBeenCalledWith('/comments/{commentId}/rates', {
      params: { path: { commentId } },
    });
  });

  it('unrates a comment with correct API call', async () => {
    const commentId = 'comment123';
    mockApi.DELETE.mockResolvedValueOnce({});

    await commentStore.unrateComment(commentId);

    expect(mockApi.DELETE).toHaveBeenCalledWith('/comments/{commentId}/rates', {
      params: { path: { commentId } },
    });
  });
});
