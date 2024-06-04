import axios from "axios";
import * as dto from "@/api/dto/postDto";
import {handleError, handleSuccess} from "@/api/util/response";
import {SERVER_PREFIX} from "@/constants/constants";
import type {Pagination, WithId} from "@/api/dto/basicDto";
import type {WithChannelId, WithFeedId, WithPostId} from "@/api/dto/basicDto";


class PostApi {
    listChannelPosts = async (data: WithChannelId & Pagination) => {
        const url = `${SERVER_PREFIX}/channel/${data.id}/posts`;
        delete (data as any).id;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };

    listFeedPosts = async (data: WithFeedId & Pagination) => {
        const url = `${SERVER_PREFIX}/feed/${data.id}/posts`;
        delete (data as any).id;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };

    // {
    //     "id": "433a5517-86fa-48da-8e46-35ddbc25b72d",
    //     "title": "123",
    //     "text": "text",
    //     "channelId": "15d8c548-62f9-4f33-b6ec-ae0bbc4e4d7f",
    //     "tagIds": [],
    //     "visible": true,
    //     "scores": {},
    //     "created": "Jun 2, 2024, 8:42:45 PM",
    //     "updated": "Jun 2, 2024, 8:42:45 PM",
    //     "ownerId": "2e0d58ad-0388-4fea-b620-bdf8db7d4fff"
    // }
    publishPost = async (data: dto.PublishPostDto & WithChannelId) => {
        const url = `${SERVER_PREFIX}/channel/${data.id}/post`;
        delete (data as any).id;
        return await axios.put(url, data).then(handleSuccess).catch(handleError);
    };

    getPost = async (data: WithPostId) => {
        const url = `${SERVER_PREFIX}/post/${data.id}`;
        return await axios.get(url).then(handleSuccess).catch(handleError);
    };

    updPost = async (data: dto.UpdPostData & WithPostId) => {
        const url = `${SERVER_PREFIX}/post/${data.id}`;
        delete (data as any).id;
        return await axios.patch(url, data).then(handleSuccess).catch(handleError);
    };

    delPost = async (data: WithPostId) => {
        const url = `${SERVER_PREFIX}/post/${data.id}`;
        return await axios.delete(url).then(handleSuccess).catch(handleError);
    };

    ratePost = async (data: WithPostId) => {
        const url = `${SERVER_PREFIX}/post/${data.id}/rate`;
        return await axios.put(url).then(handleSuccess).catch(handleError);
    };

    unratePost = async (data: WithPostId) => {
        const url = `${SERVER_PREFIX}/post/${data.id}/rate`;
        return await axios.delete(url).then(handleSuccess).catch(handleError);
    };


}

export const postApi = new PostApi();
