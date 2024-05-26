from typing import List, Set

from client.HttpRequest import HttpMethod
from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Get())
    commands.append(ListPosts())
    commands.append(Find())
    commands.append(Put())
    commands.append(Update())
    commands.append(Delete())


class Get(ICommand):

    def get_name(self) -> str:
        return 'get-feed'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/feed/{feedId}'

    def get_path_params(self) -> Set[str]:
        return {'feedId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class ListPosts(ICommand):

    def get_name(self) -> str:
        return 'see-feed'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/feed/{feedId}/posts'

    def get_path_params(self) -> Set[str]:
        return {'feedId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_queries(self) -> Set[str]:
        return {'page', 'batch'}


class Find(ICommand):

    def get_name(self) -> str:
        return 'find-feed'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/feed'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_queries(self) -> Set[str]:
        return {'name', 'pattern', 'page', 'batch'}


class Put(ICommand):

    def get_name(self) -> str:
        return 'put-feed'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PUT

    def get_url(self) -> str:
        return '/feed'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'name', 'visible', 'channelIds', 'ids'}


class Update(ICommand):

    def get_name(self) -> str:
        return 'upd-feed'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PATCH

    def get_url(self) -> str:
        return '/feed/{feedId}'

    def get_path_params(self) -> Set[str]:
        return {'feedId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'name', 'visible', 'channelIds', 'ids'}


class Delete(ICommand):

    def get_name(self) -> str:
        return 'del-feed'

    def get_method(self) -> HttpMethod:
        return HttpMethod.DELETE

    def get_url(self) -> str:
        return '/feed/{feedId}'

    def get_path_params(self) -> Set[str]:
        return {'feedId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}
