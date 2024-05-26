from client.HttpRequest import HttpRequest
from client.HttpResponse import HttpResponse


class IHttpClient:
    def send(self, request: HttpRequest) -> HttpResponse:
        raise NotImplementedError
