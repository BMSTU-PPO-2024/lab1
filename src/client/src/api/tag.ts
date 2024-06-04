import axios from "axios";
import * as dto from "@/api/dto/tagDto";
import {handleError, handleSuccess} from "@/api/util/response";
import {SERVER_PREFIX} from "@/constants/constants";
import type {Findable, Pagination, WithId} from "@/api/dto/basicDto";
import type {WithTagId} from "@/api/dto/basicDto";


class TagApi {
    putTag = async (data: dto.PutTagData) => {
        const url = `${SERVER_PREFIX}/tag`;
        return await axios.put(url, data).then(handleSuccess).catch(handleError);
    };

    getTag = async (data: WithTagId) => {
        const url = `${SERVER_PREFIX}/tag/${data.id}`;
        return await axios.get(url).then(handleSuccess).catch(handleError);
    };

    findTag = async (data: Pagination & Findable) => {
        const url = `${SERVER_PREFIX}/tag`;
        return await axios.get(url, { params: data }).then(handleSuccess).catch(handleError);
    };

    updTag = async (data: dto.UpdTagData & WithTagId) => {
        const url = `${SERVER_PREFIX}/tag/${data.id}`;
        delete (data as any).id;
        return await axios.patch(url, data).then(handleSuccess).catch(handleError);
    };

    delTag = async (data: WithTagId) => {
        const url = `${SERVER_PREFIX}/tag/${data.id}`;
        return await axios.delete(url).then(handleSuccess).catch(handleError);
    };
}

export const tagApi = new TagApi();
