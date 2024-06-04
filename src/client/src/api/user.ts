import axios from "axios";
import * as dto from "@/api/dto/userDto";
import {handleError, handleSuccess} from "@/api/util/response";
import {SERVER_PREFIX} from "@/constants/constants";


class UserApi {
    getSelf = async () => {
        const url = `${SERVER_PREFIX}/user`;
        return await axios.get(url).then(handleSuccess).catch(handleError);
    };
    getUser = async (data: dto.GetUserData) => {
        const url = `${SERVER_PREFIX}/user/${data.id}`;
        return await axios.get(url).then(handleSuccess).catch(handleError);
    };
    updateSelf = async (data: dto.UpdateSelfData) => {
        const url = `${SERVER_PREFIX}/user`;
        return await axios.patch(url, data).then(handleSuccess).catch(handleError);
    };
    updateUser = async (data: dto.UpdateUserData) => {
        const url = `${SERVER_PREFIX}/user/${data.id}`;
        // в дате лишнее айди но мне все равно
        return await axios.patch(url, data).then(handleSuccess).catch(handleError);
    };
}

export default new UserApi();
