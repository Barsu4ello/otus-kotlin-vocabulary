package exceptions

import models.VcblCommand

class UnknownVcblCommand(command: VcblCommand) : Throwable("Wrong command $command at mapping toTransport stage")