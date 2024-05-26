from typing import List

from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Get())
    commands.append(ListComments())
    commands.append(PublishComment())
    commands.append(Update())
    commands.append(Delete())
    commands.append(Rate())
    commands.append(Unrate())


class Get(ICommand):
    pass


class ListComments(ICommand):
    pass


class PublishComment(ICommand):
    pass


class Update(ICommand):
    pass


class Delete(ICommand):
    pass


class Rate(ICommand):
    pass


class Unrate(ICommand):
    pass
