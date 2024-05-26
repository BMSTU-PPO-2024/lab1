from typing import List, Set

from client.HttpRequest import HttpMethod
from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(GetSelf())
    commands.append(Get())
    commands.append(UpdateSelf())
    commands.append(Update())
    commands.append(ListSelfChs())
    commands.append(ListChs())
    commands.append(ListSelfFds())
    commands.append(ListFds())


class GetSelf(ICommand):

    def get_name(self) -> str:
        return 'get-self'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/user'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class Get(ICommand):

    def get_name(self) -> str:
        return 'get-user'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/user/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class UpdateSelf(ICommand):

    def get_name(self) -> str:
        return 'upd-self'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PATCH

    def get_url(self) -> str:
        return '/user'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'nickname', 'password', 'about', 'avatar'}


class Update(ICommand):

    def get_name(self) -> str:
        return 'upd-user'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PATCH

    def get_url(self) -> str:
        return '/user/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'nickname', 'password', 'about', 'avatar', 'permissions', 'banned'}


class ListSelfChs(ICommand):

    def get_name(self) -> str:
        return 'list-self-chs'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/user/channels'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_queries(self) -> Set[str]:
        return {'page', 'batch'}


class ListChs(ICommand):

    def get_name(self) -> str:
        return 'list-chs'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/user/{id}/channels'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_queries(self) -> Set[str]:
        return {'page', 'batch'}


class ListSelfFds(ICommand):

    def get_name(self) -> str:
        return 'list-self-fds'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/user/feeds'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_queries(self) -> Set[str]:
        return {'page', 'batch'}


class ListFds(ICommand):

    def get_name(self) -> str:
        return 'list-fds'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/user/{id}/feeds'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_queries(self) -> Set[str]:
        return {'page', 'batch'}
