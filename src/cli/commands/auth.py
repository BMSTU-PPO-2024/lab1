from typing import List, Set

from client.HttpRequest import HttpMethod
from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Login())
    commands.append(Register())


class Login(ICommand):

    def get_name(self) -> str:
        return 'login'

    def get_url(self) -> str:
        return '/login'

    def get_method(self) -> HttpMethod:
        return HttpMethod.POST

    def get_body_params(self) -> Set[str]:
        return {'email', 'password'}


class Register(ICommand):
    def get_name(self) -> str:
        return 'register'

    def get_url(self) -> str:
        return '/register'

    def get_method(self) -> HttpMethod:
        return HttpMethod.POST

    def get_body_params(self) -> Set[str]:
        return {'email', 'password'}
