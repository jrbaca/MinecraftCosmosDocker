package com.frozenapuri.minecraftcosmos.docker

import mu.KotlinLogging

private val log = KotlinLogging.logger {}
/**
 * Main entry point for the Automatic Minecraft wrapper.
 */
fun main(args: Array<String>) {
    log.info { "Starting automatic minecraft wrapper..." }
    MinecraftCosmosCLI.parseCommandLine(args).execute()
    log.info { "Exiting automatic minecraft wrapper..." }
}
