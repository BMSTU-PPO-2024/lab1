import axios from "axios";
import * as dto from "@/api/dto/feedDto";
import {handleError, handleSuccess} from "@/api/util/response";
import {SERVER_PREFIX} from "@/constants/constants";
import type {Findable, Pagination, WithId} from "@/api/dto/basicDto";
import type {WithFeedId, WithUserId} from "@/api/dto/basicDto";


class FeedApi {
    selfFeeds = async (data: Pagination = {}) => {
        throw Error("лампа сказал не будет этого эндпоинта");
        const url = `${SERVER_PREFIX}/user/feeds`;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };
    listFeeds = async (data: WithUserId & Pagination) => {
        const url = `${SERVER_PREFIX}/user/${data.id}/feeds`;
        delete (data as any).id;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };
    getFeed = async (data: WithFeedId) => {
        const url = `${SERVER_PREFIX}/feed/${data.id}`;
        return await axios.get(url).then(handleSuccess).catch(handleError);
    };
    findFeed = async (data: Pagination & Findable) => {
        const url = `${SERVER_PREFIX}/feed`;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };

    // {
    //     "id": "8a5796e9-ea7c-47d6-bfea-a026a534c612",
    //     "name": "123",
    //     "visible": false,
    //     "channelIds": [],
    //     "tagIds": [],
    //     "created": "Jun 2, 2024, 8:25:45 PM",
    //     "updated": "Jun 2, 2024, 8:25:45 PM",
    //     "ownerId": "2e0d58ad-0388-4fea-b620-bdf8db7d4fff"
    // }
    putFeed = async (data: dto.PutFeedData) => {
        const url = `${SERVER_PREFIX}/feed`;
        return await axios.put(url, data).then(handleSuccess).catch(handleError);
    };

    updFeed = async (data: dto.UpdFeedData & WithFeedId) => {
        const url = `${SERVER_PREFIX}/feed/${data.id}`;
        delete (data as any).id;
        return await axios.patch(url, data).then(handleSuccess).catch(handleError);
    };
    delFeed = async (data: WithFeedId) => {
        const url = `${SERVER_PREFIX}/feed/${data.id}`;
        return await axios.delete(url).then(handleSuccess).catch(handleError);
    };

}

export const feedApi = new FeedApi();
