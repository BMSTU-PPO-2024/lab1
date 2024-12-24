import { useApi } from '@/api/api';
import type { Channel, Post, Tag, User } from '@/api/types';
import { defineStore } from 'pinia';
import { useTagStore } from './tag-store';

export type EditingPost = Required<Pick<Post, 'text' | 'title' | 'id'>> & {
    tags: string[];
    channel: Channel;
}

export const usePostStore = defineStore('post', () => {
    const api = useApi();
    const tagStore = useTagStore();

    // отображаемые в данный момент где либо посты
    const currentPosts = ref<(Post & { owner?: User })[]>();
    // отображаемый в данный момент пост
    const currentPost = ref<Post & { owner?: User }>();

    const newPostText = ref<string>('');
    const newPostChannel = ref<Channel>();
    const newPostTitle = ref<string>('');
    const newPostTags = ref<string[]>([]);

    const editingPost = ref<EditingPost>({
        id: '',
        tags: [],
        text: '',
        title: '',
        channel: {}
    });

    watchEffect(() => {
        if (newPostText.value) {
            localStorage.setItem('newPostText', newPostText.value);
        }
        if (newPostTitle.value) {
            localStorage.setItem('newPostTitle', newPostTitle.value);
        }
        if (newPostTags.value) {
            localStorage.setItem('newPostTags', JSON.stringify(newPostTags.value));
        }
        if (newPostChannel.value) {
            localStorage.setItem('newPostChannel', JSON.stringify(newPostChannel.value));
        }
    })

    async function fetchChannelPosts(channelId: string, batch?: number, page?: number ) {
        const { data, error } = await api.value.GET('/channels/{channelId}/posts', {
            params: {
                path: { channelId },
                query: {
                    batch,
                    page,
                }
            },
        });
        if (data) {
            currentPosts.value = data;
        }
    }

    async function fetchFeedPosts(feedId: string) {
        const { data, error } = await api.value.GET('/feeds/{feedId}/posts', {
            params: {
                path: { feedId }
            },
        });
        if (data) {
            currentPosts.value = data;
        }
    }

    async function fetchPost(id: string) {
        const { data, error } = await api.value.GET('/posts/{postId}', {
            params: {
                path: { postId: id }
            },
        });
        if (data) {
            currentPost.value = data;
        }
    }

    async function createPost(post: Post, tagNames?: string[]) {
        let tagObjects: Tag[] = [];
        if (tagNames) {
            await tagStore.createTags(tagNames);
            await tagStore.fetchAllTags();
            tagObjects = tagStore.tagsByNames(tagNames);
        }
        const { data, error } = await api.value.POST('/channels/{channelId}/posts', {
            params: {
                path: { channelId: post.channelId ?? '' }
            },
            body: {
                ...post,
                tagIds: [
                    ...(post.tagIds ?? []), 
                    ...(tagObjects.map(tag => tag?.id ?? ''))
                ],
            },
        });
    }

    
    async function updatePost(post: Post, tagNames?: string[]) {
        let tagObjects: Tag[] = [];
        if (tagNames) {
            await tagStore.createTags(tagNames);
            await tagStore.fetchAllTags();
            tagObjects = tagStore.tagsByNames(tagNames);
        }
        const { data, error } = await api.value.PATCH('/posts/{postId}', {
            params: {
                path: { postId: post.id ?? '' }
            },
            body: {
                ...post,
                tagIds: [
                    ...(post.tagIds ?? []), 
                    ...(tagObjects.map(tag => tag?.id ?? ''))
                ],
            },
            
        });
    }

     
    async function deletePost(id: string) {
        const { data, error } = await api.value.DELETE('/posts/{postId}', {
            params: {
                path: { postId: id }
            },
        });
    }

    async function ratePost(id: string) {
        const { data, error } = await api.value.POST('/posts/{postId}/rates', {
            params: {
                path: { postId: id }
            },
        });
    }

    async function unratePost(id: string) {
        const { data, error } = await api.value.DELETE('/posts/{postId}/rates', {
            params: {
                path: { postId: id }
            },
        });
    }

    function startEditing(post: Post, tags: string[], channel: Channel) {
        editingPost.value.id = post.id ?? '';
        editingPost.value.text = post.text ?? '';
        editingPost.value.title = post.title ?? '';
        editingPost.value.tags = tags;
        editingPost.value.channel = channel;
    }

    async function saveEditing() {
        await updatePost(editingPost.value, editingPost.value.tags)
    }

    function startCreating() {
        const text = localStorage.getItem('newPostText');
        const title = localStorage.getItem('newPostTitle');
        const tags = localStorage.getItem('newPostTags');
        const channel = localStorage.getItem('newPostChannel');
        console.log('start creating ', tags)
        if (text) {
            newPostText.value = text;
        }
        if (title) {
            newPostTitle.value = title;
        }
        if (tags) {
            newPostTags.value = JSON.parse(tags);
        }
        if (channel) {
            newPostChannel.value = JSON.parse(channel);
        }
    }


    return {
        self,
        currentPosts,
        currentPost,
        editingPost,

        newPostChannel,
        newPostText,
        newPostTags,
        newPostTitle,
        
        createPost,
        updatePost,
        deletePost,
        fetchPost,
        fetchChannelPosts,
        fetchFeedPosts,
        ratePost,
        unratePost,
        saveEditing,
        startEditing,
        startCreating,
    };
});
