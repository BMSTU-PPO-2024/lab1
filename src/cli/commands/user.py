from typing import List

from manager.ICommand import ICommand


def register(commands: List[ICommand]):
    commands.append(GetSelf())
    commands.append(Get())
    commands.append(UpdateSelf())
    commands.append(Update())
    commands.append(ListSelfChs())
    commands.append(ListChs())
    commands.append(ListSelfFds())
    commands.append(ListFds())


class GetSelf(ICommand):
    pass


class Get(ICommand):
    pass


class UpdateSelf(ICommand):
    pass


class Update(ICommand):
    pass


class ListSelfChs(ICommand):
    pass


class ListChs(ICommand):
    pass


class ListSelfFds(ICommand):
    pass


class ListFds(ICommand):
    pass
