
export interface UpdateSelfData {
    about?: string,
    avatar?: string,
    nickname?: string,
    password?: string,
}

export interface UpdateUserData extends UpdateSelfData {
    id: string,
    banned?: boolean,
    permissions?: number,
}

export interface GetUserData {
    id: string,
}
