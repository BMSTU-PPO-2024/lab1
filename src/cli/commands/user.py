from typing import List

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
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class Get(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class UpdateSelf(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class Update(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class ListSelfChs(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class ListChs(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class ListSelfFds(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class ListFds(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass
