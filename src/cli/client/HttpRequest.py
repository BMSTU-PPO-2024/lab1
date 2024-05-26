from enum import Enum
from typing import Dict


class HttpMethod(Enum):
    GET = 'GET'
    POST = 'POST'
    PATCH = 'PATCH'
    PUT = 'PUT'
    DELETE = 'DELETE'


class HttpRequest:
    def __init__(self):
        self._method = None
        self._url = None
        self._paths = dict[str, str]()
        self._headers = dict[str, str]()
        self._queries = dict[str, str]()
        self._body = None

    def get_method(self) -> HttpMethod:
        return self._method

    def set_method(self, method: HttpMethod):
        if method is None:
            raise ValueError('Method is none')
        self._method = method

    def get_url(self) -> str:
        return self._url

    def set_url(self, url: str):
        if url is None:
            raise ValueError('Url is none')
        self._url = url

    def get_paths(self) -> Dict[str, str]:
        return self._paths

    def get_queries(self) -> Dict[str, str]:
        return self._queries

    def get_headers(self) -> Dict[str, str]:
        return self._headers

    def get_body(self) -> Dict:
        return self._body

    def set_body(self, body: Dict):
        self._body = body
