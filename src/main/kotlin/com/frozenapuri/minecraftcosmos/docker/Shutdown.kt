package com.frozenapuri.minecraftcosmos.docker

import mu.KotlinLogging
import java.util.*

/**
 * Main execution branch of AutomaticMinecraft. Performs all necessary cleanup for the server
 * before exiting.
 */
class Shutdown internal constructor(private val serverUUID: UUID) : Executable {

    private val log = KotlinLogging.logger {}

    override fun execute() {
        log.info("Executing shutdown")
        zipDataFolderAndUploadToS3()
        log.info("Shutdown complete")
    }

    private fun zipDataFolderAndUploadToS3() {
        log.info("Will upload data to S3")
        S3Storage.writeByteArrayOutputStream(
                MinecraftInstallationAdapter.zipOfEntireInstallation,
                "$serverUUID.zip")
    }
}
