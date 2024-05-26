from typing import Set

from client.HttpRequest import HttpMethod


class ICommand:
    def get_name(self) -> str:
        raise NotImplementedError

    def get_body_params(self) -> Set[str]:
        return set()

    def get_path_params(self) -> Set[str]:
        return set()

    def get_queries(self) -> Set[str]:
        return set()

    def get_headers(self) -> Set[str]:
        return set()

    def get_method(self) -> HttpMethod:
        raise NotImplementedError

    def get_url(self) -> str:
        raise NotImplementedError
