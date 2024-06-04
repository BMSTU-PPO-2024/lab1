import axios from "axios";
import * as dto from "@/api/dto/channelDto";
import {handleError, handleSuccess} from "@/api/util/response";
import {SERVER_PREFIX} from "@/constants/constants";
import type {Findable, Pagination, WithId} from "@/api/dto/basicDto";
import type {WithChannelId, WithUserId} from "@/api/dto/basicDto";


class ChannelApi {
    selfChannels = async (data: Pagination = {}) => {
        throw Error("лампа сказал не будет этого эндпоинта");
        const url = `${SERVER_PREFIX}/user/channels`;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };
    listChannels = async (data: WithUserId & Pagination) => {
        const url = `${SERVER_PREFIX}/user/${data.id}/channels`;
        delete (data as any).id;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };
    getChannel = async (data: WithChannelId) => {
        const url = `${SERVER_PREFIX}/channel/${data.id}`;
        return await axios.get(url).then(handleSuccess).catch(handleError);
    };
    findChannel = async (data: Pagination & Findable) => {
        const url = `${SERVER_PREFIX}/channel`;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };

    // {
    //     "id": "e129e201-67f0-4aba-852b-0d81939ef105",
    //     "name": "bebra22",
    //     "visible": true,
    //     "created": "Jun 2, 2024, 7:38:23 PM",
    //     "updated": "Jun 2, 2024, 7:38:23 PM",
    //     "ownerId": "2e0d58ad-0388-4fea-b620-bdf8db7d4fff"
    // }
    putChannel = async (data: dto.PutChannelData) => {
        const url = `${SERVER_PREFIX}/channel`;
        return await axios.put(url, data).then(handleSuccess).catch(handleError);
    };

    // ничего
    updChannel = async (data: dto.UpdChannelData & WithChannelId) => {
        const url = `${SERVER_PREFIX}/channel/${data.id}`;
        delete (data as any).id;
        return await axios.patch(url, data).then(handleSuccess).catch(handleError);
    };
    delChannel = async (data: WithChannelId) => {
        const url = `${SERVER_PREFIX}/channel/${data.id}`;
        return await axios.delete(url).then(handleSuccess).catch(handleError);
    };

}

export const channelApi = new ChannelApi();
