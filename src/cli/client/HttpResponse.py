from typing import Dict


class HttpResponse:
    def __init__(self, status: int, body: Dict):
        self._status = status
        self._body = body

    def get_status(self):
        return self._status

    def get_body(self):
        return self._body
