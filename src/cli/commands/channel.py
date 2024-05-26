from typing import List

from client.HttpRequest import HttpMethod
from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Get())
    commands.append(Find())
    commands.append(ListPosts())
    commands.append(Put())
    commands.append(PublishPost())
    commands.append(Update())
    commands.append(Delete())


class Get(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class Find(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class ListPosts(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class Put(ICommand):

    def get_name(self) -> str:
        pass

    def get_method(self) -> HttpMethod:
        pass

    def get_url(self) -> str:
        pass


class PublishPost(ICommand):

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