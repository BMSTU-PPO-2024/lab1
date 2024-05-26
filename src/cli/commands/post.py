from typing import List, Set

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
        return 'get-post'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/post/{postId}'

    def get_path_params(self) -> Set[str]:
        return {'postId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class ListComments(ICommand):

    def get_name(self) -> str:
        return 'list-post-cs'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/post/{postId}/comments'

    def get_path_params(self) -> Set[str]:
        return {'postId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_queries(self) -> Set[str]:
        return {'page', 'batch'}


class PublishComment(ICommand):

    def get_name(self) -> str:
        return 'add-comment'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PUT

    def get_url(self) -> str:
        return '/post/{postId}/comment'

    def get_path_params(self) -> Set[str]:
        return {'postId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'text'}


class Update(ICommand):

    def get_name(self) -> str:
        return 'upd-post'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PATCH

    def get_url(self) -> str:
        return '/post/{postId}'

    def get_path_params(self) -> Set[str]:
        return {'postId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'title', 'text', 'visible', 'tagIds'}


class Delete(ICommand):

    def get_name(self) -> str:
        return 'del-post'

    def get_method(self) -> HttpMethod:
        return HttpMethod.DELETE

    def get_url(self) -> str:
        return '/post/{postId}'

    def get_path_params(self) -> Set[str]:
        return {'postId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class Rate(ICommand):

    def get_name(self) -> str:
        return 'rate-post'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PUT

    def get_url(self) -> str:
        return '/post/{postId}/rate'

    def get_path_params(self) -> Set[str]:
        return {'postId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class Unrate(ICommand):

    def get_name(self) -> str:
        return 'unrate-post'

    def get_method(self) -> HttpMethod:
        return HttpMethod.DELETE

    def get_url(self) -> str:
        return '/post/{postId}/rate'

    def get_path_params(self) -> Set[str]:
        return {'postId'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}
