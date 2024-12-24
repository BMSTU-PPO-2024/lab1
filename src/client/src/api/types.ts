import type { paths, components } from './schema';

export type PostArgs<Path extends keyof paths> = paths[Path]['post'] extends {
    requestBody: unknown;
}
    ? paths[Path]['post']['requestBody'] extends { content: unknown }
        ? paths[Path]['post']['requestBody']['content']['application/json']
        : never
    : never;

export type PatchArgs<Path extends keyof paths> = paths[Path]['patch'] extends {
    requestBody: unknown;
}
    ? paths[Path]['patch']['requestBody'] extends { content: unknown }
        ? paths[Path]['patch']['requestBody']['content']['application/json']
        : never
    : never;

export type DeleteArgs<Path extends keyof paths> = paths[Path]['delete'] extends {
    parameters: unknown;
}
    ? paths[Path]['delete']['parameters']['path']
    : never;

export type GetPathArgs<Path extends keyof paths> = paths[Path]['get'] extends {
    parameters: unknown;
}
    ? paths[Path]['get']['parameters']['path']
    : never;

export type GetQueryArgs<Path extends keyof paths> = paths[Path]['get'] extends {
    parameters: unknown;
}
    ? paths[Path]['get']['parameters']['query']
    : never;

export type PostRet<Path extends keyof paths> = paths[Path]['post'] extends {
    responses: unknown;
}
    ? paths[Path]['post']['responses'][200]['content'] extends {
          'application/json': unknown;
      }
        ? paths[Path]['post']['responses'][200]['content']['application/json']
        : never
    : never;

export type PatchRet<Path extends keyof paths> = paths[Path]['patch'] extends {
    responses: unknown;
}
    ? paths[Path]['patch']['responses'][200]['content'] extends {
          'application/json': unknown;
      }
        ? paths[Path]['patch']['responses'][200]['content']['application/json']
        : never
    : never;

export type DeleteRet<Path extends keyof paths> = paths[Path]['delete'] extends {
    responses: unknown;
}
    ? paths[Path]['delete']['responses'][200]['content'] extends {
          'application/json': unknown;
      }
        ? paths[Path]['delete']['responses'][200]['content']['application/json']
        : never
    : never;

export type GetRet<Path extends keyof paths> = paths[Path]['get'] extends {
    responses: unknown;
}
    ? paths[Path]['get']['responses'][200]['content'] extends {
          'application/json': unknown;
      }
        ? paths[Path]['get']['responses'][200]['content']['application/json']
        : never
    : never;

export type LoginData = PostArgs<'/login'>;
export type RegisterData = PostArgs<'/register'>;
export type User = GetRet<'/users/{userId}'>;
export type Post = GetRet<'/posts/{postId}'>;
export type Channel = GetRet<'/channels/{channelId}'>;
export type Feed = GetRet<'/feeds/{feedId}'>;
export type Comment = GetRet<'/comments/{commentId}'>;
export type Tag = GetRet<'/tags/{tagId}'>;

export type GetFeedsParams = GetQueryArgs<'/feeds'>;
export type GetChannelsParams = GetQueryArgs<'/channels'>;