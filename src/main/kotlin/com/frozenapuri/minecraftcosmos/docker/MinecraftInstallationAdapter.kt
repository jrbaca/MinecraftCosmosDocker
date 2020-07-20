package com.frozenapuri.minecraftcosmos.docker

import mu.KotlinLogging
import java.util.ArrayList
import java.util.zip.ZipInputStream
import java.nio.channels.ReadableByteChannel
import java.io.*

/**
 * Adapter for performing operations on the local minecraft installation.
 */
internal object MinecraftInstallationAdapter {

    private val log = KotlinLogging.logger {}

    private val MINECRAFT_ROOT = "/opt/minecraft/"
    private val MINECRAFT_SERVER_FILENAME = "minecraft_server.jar"

    val zipOfEntireInstallation: ByteArrayOutputStream
        get() {
            val ignoreList = ArrayList<String>()
            ignoreList.add(MINECRAFT_ROOT + MINECRAFT_SERVER_FILENAME)
            ignoreList.add(MINECRAFT_ROOT + "eula.txt")
            return ZipUtils.createZipFromDirectory(MINECRAFT_ROOT, ignoreList)
        }

    @Throws(IOException::class)
    fun writeMinecraftServerFile(readableByteChannel: ReadableByteChannel) {
        val fos = FileOutputStream(MINECRAFT_ROOT + MINECRAFT_SERVER_FILENAME)
        fos.channel.transferFrom(readableByteChannel, 0, java.lang.Long.MAX_VALUE)
    }

    @Throws(IOException::class)
    fun writeEulaTrue() {
        log.info("Writing true to eula")
        val writer = BufferedWriter(FileWriter(MINECRAFT_ROOT + "eula.txt", true))
        writer.append("eula=true")
        writer.close()
    }

    @Throws(IOException::class)
    fun extractZipOverEntireInstallation(zipInputStream: ZipInputStream) {
        ZipUtils.extractZipToDirectory(zipInputStream, MINECRAFT_ROOT)
    }
}
