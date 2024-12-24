import { useApi } from '@/api/api';
import type { Comment } from '@/api/types';
import { defineStore } from 'pinia';

export const useCommentStore = defineStore('comment', () => {
    const api = useApi();

    // текущие отображаемые комментарии
    const currentComments = ref<Comment[]>();

    const newComment = ref<string>('');

    async function createComment(comment: Comment) {
        const { data, error } = await api.value.POST('/posts/{postId}/comments', {
            params: {
                path: { postId: comment.postId ?? '' },
            },
            body: {
                ...comment,
            },
        });
    }

    async function fetchComments(postId: string) {
        const { data, error } = await api.value.GET('/posts/{postId}/comments', {
            params: {
                path: { postId },
            },
        });
        if (data) {
            currentComments.value = data;
        }
    }

    async function updateComment(comment: Comment) {
        const { data, error } = await api.value.PATCH('/comments/{commentId}', {
            params: {
                path: { commentId: comment.id ?? '' },
            },
            body: comment
        });
    }

    async function deleteComment(commentId: string) {
        const { data, error } = await api.value.DELETE('/comments/{commentId}', {
            params: {
                path: { commentId },
            },
        });
    }

    
    async function rateComment(commentId: string) {
        const { data, error } = await api.value.POST('/comments/{commentId}/rates', {
            params: {
                path: { commentId },
            },
        });
    }
    
    async function unrateComment(commentId: string) {
        const { data, error } = await api.value.DELETE('/comments/{commentId}/rates', {
            params: {
                path: { commentId },
            },
        });
    }

    return {
        currentComments,
        newComment,
        
        createComment,
        fetchComments,
        updateComment,
        deleteComment,
        rateComment,
        unrateComment,
    };
});
