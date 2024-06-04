import axios from "axios";
import * as dto from "@/api/dto/commentDto";
import {handleError, handleSuccess} from "@/api/util/response";
import {SERVER_PREFIX} from "@/constants/constants";
import type {Pagination, WithId} from "@/api/dto/basicDto";
import type {WithCommentId, WithPostId} from "@/api/dto/basicDto";


class CommentApi {
    listPostComments = async (data: WithPostId & Pagination) => {
        const url = `${SERVER_PREFIX}/post/${data.id}/comments`;
        delete (data as any).id;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };

    addComment = async (data: dto.AddCommentData & WithPostId) => {
        const url = `${SERVER_PREFIX}/post/${data.id}/comment`;
        delete (data as any).id;
        return await axios.put(url, data).then(handleSuccess).catch(handleError);
    };

    getComment = async (data: WithCommentId) => {
        const url = `${SERVER_PREFIX}/comment/${data.id}`;
        return await axios.get(url).then(handleSuccess).catch(handleError);
    };

    updComment = async (data: dto.UpdCommentData & WithCommentId) => {
        const url = `${SERVER_PREFIX}/comment/${data.id}`;
        delete (data as any).id;
        return await axios.patch(url, data).then(handleSuccess).catch(handleError);
    };

    delComment = async (data: WithCommentId) => {
        const url = `${SERVER_PREFIX}/comment/${data.id}`;
        return await axios.delete(url).then(handleSuccess).catch(handleError);
    };

    rateComment = async (data: WithCommentId) => {
        const url = `${SERVER_PREFIX}/comment/${data.id}/rate`;
        return await axios.put(url).then(handleSuccess).catch(handleError);
    };

    unrateComment = async (data: WithCommentId) => {
        const url = `${SERVER_PREFIX}/comment/${data.id}/rate`;
        return await axios.delete(url).then(handleSuccess).catch(handleError);
    };
}

export const commentApi = new CommentApi();
