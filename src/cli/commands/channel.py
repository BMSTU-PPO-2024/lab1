from typing import List, Set

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
        return 'get-channel'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/channel/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}


class Find(ICommand):

    def get_name(self) -> str:
        return 'find-channel'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/channel'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_queries(self) -> Set[str]:
        return {'name', 'pattern', 'page', 'batch'}


class ListPosts(ICommand):

    def get_name(self) -> str:
        return 'list-channel-posts'

    def get_method(self) -> HttpMethod:
        return HttpMethod.GET

    def get_url(self) -> str:
        return '/channel/{id}/posts'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_queries(self) -> Set[str]:
        return {'page', 'batch'}


class Put(ICommand):

    def get_name(self) -> str:
        return 'put-channel'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PUT

    def get_url(self) -> str:
        return '/channel'

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'name', 'visible'}


class PublishPost(ICommand):

    def get_name(self) -> str:
        return 'publish-post'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PUT

    def get_url(self) -> str:
        return '/channel/{id}/post'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'title', 'text', 'visible', 'tagIds'}


class Update(ICommand):

    def get_name(self) -> str:
        return 'upd-channel'

    def get_method(self) -> HttpMethod:
        return HttpMethod.PATCH

    def get_url(self) -> str:
        return '/channel/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}

    def get_body_params(self) -> Set[str]:
        return {'name', 'visible'}


class Delete(ICommand):

    def get_name(self) -> str:
        return 'del-channel'

    def get_method(self) -> HttpMethod:
        return HttpMethod.DELETE

    def get_url(self) -> str:
        return '/channel/{id}'

    def get_path_params(self) -> Set[str]:
        return {'id'}

    def get_headers(self) -> Set[str]:
        return {'Authorization'}
