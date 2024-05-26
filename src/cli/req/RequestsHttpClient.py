import json

import requests

from client.HttpRequest import HttpRequest, HttpMethod
from client.HttpResponse import HttpResponse
from client.IHttpClient import IHttpClient


def queries_to_str(queries):
    if len(queries) == 0:
        return ''
    ret = '?'
    for k, v in queries.items():
        ret += k + '=' + str(v) + '&'
    return ret[:-1]


class RequestsHttpClient(IHttpClient):

    def send(self, request: HttpRequest) -> HttpResponse:
        method = request.get_method().name
        # Prepare url
        url = request.get_url() + queries_to_str(request.get_queries())
        for k, v in request.get_paths().items():
            url = url.replace('{%s}' % k, v)
        # Prepare body
        if method == HttpMethod.GET:
            body = None
        else:
            body = json.dumps(request.get_body())
        raw_response = requests.request(
            method=method,
            url=url,
            data=body,
            headers=request.get_headers()
        )
        text = raw_response.text.strip()
        if len(text) == 0:
            body = {}
        elif '{' not in text and '[' not in text:
            body = text
        else:
            body = json.loads(text)
        ret = HttpResponse(raw_response.status_code, body)
        return ret
