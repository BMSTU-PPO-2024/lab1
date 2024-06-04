
export interface PublishPostDto {
    text: string,
    title: string,
    tagIds: string[],
    visible?: boolean,
}
export interface UpdPostData {
    text?: string,
    title?: string,
    tagIds?: string[],
    visible?: boolean,
}