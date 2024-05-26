from manager.Manager import Manager


class ManagerBuilder:

    def __init__(self):
        self._host = None
        self._commands = None
        self._client = None
        self._source = None
        self._printer = None

    def reset(self):
        self._host = None
        self._commands = None
        self._client = None
        self._source = None
        self._printer = None

    def set_host(self, host):
        self._host = host

    def set_commands(self, commands):
        self._commands = commands

    def set_client(self, client):
        self._client = client

    def set_source(self, source):
        self._source = source

    def set_printer(self, printer):
        self._printer = printer

    def build(self):
        if (self._commands is None
                or self._client is None
                or self._source is None
                or self._printer is None
                or self._host is None):
            self.reset()
            raise ValueError('Invalid params')
        ret = Manager(self._host, self._client, self._source, self._printer, self._commands)
        self.reset()
        return ret
