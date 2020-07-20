package com.frozenapuri.minecraftcosmos.docker


import com.amazonaws.services.s3.model.AmazonS3Exception
import mu.KotlinLogging
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.util.*
import java.util.zip.ZipInputStream

/**
 * Main execution branch of AutomaticMinecraft. Sets up the server so it can be run.
 */
class Setup internal constructor(private val serverUUID: UUID) : Executable {

    private val log = KotlinLogging.logger {}

    override fun execute() {
        log.info("Executing setup")
        installPreviousFiles()
        downloadServer()
        writeEula()
        log.info("Setup complete")
    }

    private fun installPreviousFiles() {
        log.info("Downloading previous files")
        try {
            val previousFileInputStream = S3Storage.readFile("$serverUUID.zip")
            val zipInputStream = ZipInputStream(previousFileInputStream)
            MinecraftInstallationAdapter.extractZipOverEntireInstallation(zipInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: AmazonS3Exception) {
            log.info(String.format("Server %s was not found", serverUUID))
        }

    }

    private fun downloadServer() {
        try {
            log.info("Downloading Minecraft server files")
            val website = URL(
                    "https://launcher.mojang.com/v1/objects/3dc3d84a581f14691199cf6831b71ed1296a9fdf/server.jar")
            val rbc = Channels.newChannel(website.openStream())

            MinecraftInstallationAdapter.writeMinecraftServerFile(rbc)
        } catch (e: IOException) {
            throw AutomaticMinecraftExternalServiceException("Failed to download server files", e)
        }

    }

    private fun writeEula() {
        try {
            MinecraftInstallationAdapter.writeEulaTrue()
        } catch (e: IOException) {
            throw AutomaticMinecraftExternalServiceException("Failed to write EULA", e)
        }

    }
}
