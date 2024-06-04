export interface Pagination {
    page?: number,
    batch?: number
}

export interface WithId {
    id: string,
}

export type WithChannelId = WithId;
export type WithFeedId = WithId;
export type WithUserId = WithId;
export type WithPostId = WithId;
export type WithTagId = WithId;
export type WithCommentId = WithId;


//
// export interface WithChannelId {
//     channelId: string,
// }
//
// export interface WithFeedId {
//     feedId: string,
// }
//
// export interface WithUserId {
//     userId: string,
// }
//
// export interface WithPostId {
//     postId: string,
// }
//
// export interface WithTagId {
//     tagId: string,
// }
//
// export interface WithCommentId {
//     commentId: string,
// }



export interface Findable {
    name?: string,  // не будет
    pattern?: string,
}