from client.HttpResponse import HttpResponse
from client.HttpRequest import HttpRequest


class IHttpClient:
    def send(self, request: HttpRequest) -> HttpResponse:
        raise NotImplementedError
