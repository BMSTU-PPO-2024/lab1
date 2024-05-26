import sys

from client.HttpResponse import HttpResponse
from commands import auth, channel, comment, feed, post, tag, user
from manager.Manager import Manager
from manager.ManagerBuilder import ManagerBuilder
from req.RequestsHttpClient import RequestsHttpClient


def output(response: HttpResponse):
    print('Status: ' + str(response.get_status()))
    for k, v in response.get_body().items():
        print(k + ': ' + v)


def build(builder: ManagerBuilder, args) -> Manager:
    # Set host
    host = args.get('host')
    if host is None:
        host = 'http://127.0.0.1:8080'
    builder.set_host(host)
    # Set http client
    client = RequestsHttpClient()
    builder.set_client(client)
    # Set printer
    builder.set_printer(output)

    # Set source
    def source(name):
        return args.get(name)

    builder.set_source(source)

    # Register commands
    commands = []
    auth.register(commands)
    channel.register(commands)
    comment.register(commands)
    feed.register(commands)
    post.register(commands)
    tag.register(commands)
    user.register(commands)
    builder.set_commands(commands)
    return builder.build()


def parse_named(args):
    ret = dict()
    for arg in args:
        if not arg.startswith('--'):
            continue
        index = arg.find('=')
        if index < 0:
            ret[arg[2:]] = None
        else:
            ret[arg[2:index]] = arg[index + 1:]
    return ret


def main(args):
    if len(args) == 0:
        print('Missing command')
        return 1
    builder = ManagerBuilder()
    manager = build(builder, parse_named(args[1:]))
    return 0 if manager.execute(args[0]) else 1


if __name__ == '__main__':
    sys.exit(main(sys.argv[1:]))
