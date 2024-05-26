from typing import List, Set

from client.HttpRequest import HttpMethod
from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Get())
    commands.append(Update())
    commands.append(Delete())
    commands.append(Rate())
    commands.append(Unrate())


class Get(ICommand):

    def get_name(self) -> str:
        return 'get-comment'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/comment/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}


class Update(ICommand):

    def get_name(self) -> str:
        return 'upd-comment'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/comment/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'text'}


class Delete(ICommand):

    def get_name(self) -> str:
        return 'del-comment'

    def get_method(self) -> HttpMethod:
        return HttpMethod.DELETE

    def get_url(self) -> str:
        return '/comment/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class Rate(ICommand):

    def get_name(self) -> str:
        return 'rate-comment'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PUT

    def get_url(self) -> str:
        return '/comment/{id}/rate'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class Unrate(ICommand):

    def get_name(self) -> str:
        return 'unrate-comment'

    def get_method(self) -> HttpMethod:
        return HttpMethod.DELETE

    def get_url(self) -> str:
        return '/comment/{id}/rate'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}
