from typing import List

from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(Get())
    commands.append(Find())
    commands.append(ListPosts())
    commands.append(Put())
    commands.append(PublishPost())
    commands.append(Update())
    commands.append(Delete())


class Get(ICommand):
    pass


class Find(ICommand):
    pass


class ListPosts(ICommand):
    pass


class Put(ICommand):
    pass


class PublishPost(ICommand):
    pass


class Update(ICommand):
    pass


class Delete(ICommand):
    pass
