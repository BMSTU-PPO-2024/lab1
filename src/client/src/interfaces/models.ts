
export interface Channel {
    id: string,
    name: string,
    visible?: boolean,
    created?: string,
    updated?: string,
    ownerId: string,
}

export interface Comment {
    id: string,
    postId: string,
    text: string,
    // scores: Date,
    created?: string,
    updated?: string,
    ownerId: string,
}

export interface Post {
    id: string,
    title: string,
    text: string,
    channelId: string,
    tagIds?: string[],
    visible?: boolean,
    // scores:
    created?: string,
    updated?: string,
    ownerId: string,
}

export interface User {
    id: string,
    nickname: string,
    email: string,
    banned?: boolean,
    permissions?: number,
    created?: string,
    updated?: string,
    about?: string,
    avatarUrl?: string,
}

export interface Tag {
    id: string,
    name: string,
    created?: string,
    updated?: string,
}

export interface Feed {
    id: string,
    name: string,
    visible?: boolean,
    channelIds?: string[],
    tagIds?: string[],
    created?: string,
    updated?: string,
    ownerId: string,
}