package com.frozenapuri.minecraftcosmos.docker

/**
 * Exception to be thrown whenever AutomaticMinecraft fails to interface with an external service.
 */
class AutomaticMinecraftExternalServiceException(str: String, e: Exception) : RuntimeException(str, e)

/**
 * Exception to be thrown whenever AutomaticMinecraft cannot parse the CLI properly.
 */
class AutomaticMinecraftParseException : RuntimeException {
    internal constructor(str: String) : super(str) {}

    internal constructor(str: String, e: Exception) : super(str, e) {}
}