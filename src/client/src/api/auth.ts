import axios from "axios";
import * as dto from "@/api/dto/authDto";
import {handleError, handleSuccess} from "@/api/util/response";
import {SERVER_PREFIX} from "@/constants/constants";


class AuthApi {
    register = async (data: dto.RegisterData) => {
        const url = `${SERVER_PREFIX}/register`;
        return await axios.post(url, data).then(handleSuccess).catch(handleError);
    };
    login = async (data: dto.LoginData) => {
        const url = `${SERVER_PREFIX}/login`;
        return await axios.post(url, data).then(handleSuccess).catch(handleError);
    }
}

export default new AuthApi();

// fetch("http://localhost:8080/register", {
//     method: "POST",
//     body: JSON.stringify({email: "1@ds.sru", password: "eqwdeqw"}),
// }).then((resp) => resp.json()).then(j => console.log(j));