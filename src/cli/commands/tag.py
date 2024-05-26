from typing import List, Set

from client.HttpRequest import HttpMethod
from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Get())
    commands.append(Find())
    commands.append(Put())
    commands.append(Update())
    commands.append(Delete())


class Get(ICommand):

    def get_name(self) -> str:
        return 'get-tag'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/tag/{tagId}'

    def get_path_params(self) -> Set[str]:
        return {'tagId'}


class Find(ICommand):

    def get_name(self) -> str:
        return 'find-tag'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/tag'

    def get_queries(self) -> Set[str]:
        return {'name', 'pattern', 'page', 'batch'}


class Put(ICommand):

    def get_name(self) -> str:
        return 'put-tag'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PUT

    def get_url(self) -> str:
        return '/tag'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'name'}


class Update(ICommand):

    def get_name(self) -> str:
        return 'upd-tag'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PATCH

    def get_url(self) -> str:
        return '/tag/{tagId}'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_path_params(self) -> Set[str]:
        return {'tagId'}

    def get_body_params(self) -> Set[str]:
        return {'name'}


class Delete(ICommand):

    def get_name(self) -> str:
        return 'del-tag'

    def get_method(self) -> HttpMethod:
        return HttpMethod.DELETE

    def get_url(self) -> str:
        return '/tag/{tagId}'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_path_params(self) -> Set[str]:
        return {'tagId'}
