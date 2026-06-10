package ru.gorbunov.vocabulary.common.exceptions

import ru.gorbunov.vocabulary.common.models.VcblCommand

class UnknownVcblCommand(command: VcblCommand) : Throwable("Wrong command $command at mapping toTransport stage")