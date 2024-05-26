from typing import List

from client.HttpRequest import HttpMethod
from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Get())
    commands.append(ListComments())
    commands.append(PublishComment())
    commands.append(Update())
    commands.append(Delete())
    commands.append(Rate())
    commands.append(Unrate())


class Get(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class ListComments(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class PublishComment(ICommand):

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


class Delete(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class Rate(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class Unrate(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass
