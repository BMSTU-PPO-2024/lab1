import type {Pagination, WithId} from "@/api/dto/basicDto";

export interface PutChannelData {
    visible?: boolean,
    name: string
}

export interface UpdChannelData {
    visible?: boolean,
    name?: string
}

