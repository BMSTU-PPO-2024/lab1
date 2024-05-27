from typing import List

from client.HttpRequest import HttpRequest
from client.IHttpClient import IHttpClient
from manager.ICommand import ICommand


class Manager:

    def __init__(self, host, client: IHttpClient, source, printer, commands: List[ICommand]):
        self._commands = dict()
        self._host = host
        for command in commands:
            self._commands[command.get_name()] = command
        self._client = client
        self._source = source
        self._printer = printer

    def help(self):
        command_name = self._source('command')
        if command_name is None:
            print("Available commands:")
            for command in self._commands.keys():
                print(command)
            return
        command = self._commands.get(command_name)
        if command is None:
            print('Unknown command')
            return
        print('Command: ' + command.get_name())
        print('Method: ' + command.get_method().name)
        print('Url: ' + command.get_url())
        print('Headers: ' + str(list(command.get_headers())))
        print('Path parameters: ' + str(list(command.get_path_params())))
        print('Query parameters: ' + str(list(command.get_queries())))
        print('Body values: ' + str(list(command.get_body_params())))

    def execute(self, command_name: str) -> bool:
        if command_name == 'help':
            self.help()
            return True
        command = self._commands.get(command_name)
        if command is None:
            print('Command not found')
            return False
        # Prepare request
        request = HttpRequest()
        request.set_method(command.get_method())
        request.set_url(self._host + command.get_url())
        # Prepare headers
        headers = request.get_headers()
        for header in command.get_headers():
            to_set = self._source(header)
            if to_set is None:
                continue
            headers[header] = to_set
        # Prepare path params
        paths = request.get_paths()
        for param in command.get_path_params():
            to_set = self._source(param)
            if to_set is None:
                continue
            paths[param] = to_set
        # Prepare queries
        queries = request.get_queries()
        for query in command.get_queries():
            to_set = self._source(query)
            if to_set is None:
                continue
            queries[query] = to_set
        # Prepare body
        params = command.get_body_params()
        if len(params) != 0:
            body = dict()
            for param in params:
                body[param] = self._source(param)
            request.set_body(body)
        # Do request
        response = self._client.send(request)
        self._printer(response)
        return response.get_status() == 200
