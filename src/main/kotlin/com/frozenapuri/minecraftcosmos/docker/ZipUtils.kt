package com.frozenapuri.minecraftcosmos.docker

import mu.KotlinLogging
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

internal object ZipUtils {

    private val log = KotlinLogging.logger {}

    @Throws(IOException::class)
    fun extractZipToDirectory(zipInputStream: ZipInputStream, destinationPath: String) {
        unZip(zipInputStream, File(destinationPath))
    }

    @Throws(IOException::class)
    private fun unZip(zipInputStream: ZipInputStream, outputDirectory: File) {

        var numberOfFiles = 0
        var zipEntry: ZipEntry? = zipInputStream.nextEntry

        while (zipEntry != null) {
            numberOfFiles++
            log.info("{}. Extracting content: {}", numberOfFiles, zipEntry.name)
            val unZippedFile = File(outputDirectory.toString() + File.separator + zipEntry.name)

            //Create output director
            unZippedFile.parentFile.mkdirs()
            log.info("Creating new file : {}", unZippedFile.canonicalPath)

            //Write contents to file
            writeContents(zipInputStream, unZippedFile)
            log.info("Written content to file: {}", unZippedFile.canonicalPath)

            //Close current entry
            zipInputStream.closeEntry()
            zipEntry = zipInputStream.nextEntry
        }
        log.info("Finished execution, UnZipped file count: {}", numberOfFiles)

    }

    @Throws(IOException::class)
    private fun writeContents(zipInputStream: ZipInputStream, outputFile: File) {
        val fileOutputStream = FileOutputStream(outputFile)
        val content = ByteArray(1024)
        var len: Int = zipInputStream.read(content)
        while (len > 0) {
            fileOutputStream.write(content, 0, len)
            len = zipInputStream.read(content)
        }
        fileOutputStream.close()
    }

    fun createZipFromDirectory(directoryPath: String, ignoredFiles: List<String>): ByteArrayOutputStream {
        val baos = ByteArrayOutputStream()
        try {
            ZipOutputStream(baos).use { zos ->
                val directory = File(directoryPath)

                val fileList = getFileList(directory)

                for (filePath in fileList) {

                    if (ignoredFiles.contains(filePath)) {
                        log.info("Skipping: {}", filePath)
                        continue
                    }

                    log.info("Compressing: {}", filePath)

                    // Creates a zip entry.
                    val name = filePath.substring(directory.absolutePath.length + 1)

                    val zipEntry = ZipEntry(name)
                    zos.putNextEntry(zipEntry)

                    // Read file content and write to zip output stream.
                    try {
                        FileInputStream(filePath).use { fis ->
                            val buffer = ByteArray(1024)
                            var length: Int = fis.read(buffer)
                            while (length > 0) {
                                zos.write(buffer, 0, length)
                                length = fis.read(buffer)
                            }

                            // Close the zip entry.
                            zos.closeEntry()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }

        return baos
    }

    /**
     * Get files list from the directory recursive to the sub directory.
     */
    private fun getFileList(directory: File): List<String> {
        val files = directory.listFiles()
        val fileList = ArrayList<String>()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isFile) {
                    fileList.add(file.absolutePath)
                } else {
                    fileList.addAll(getFileList(file))
                }
            }
        }
        return fileList
    }
}
