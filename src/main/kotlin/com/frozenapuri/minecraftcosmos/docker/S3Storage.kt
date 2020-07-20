package com.frozenapuri.minecraftcosmos.docker


import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3Object
import mu.KotlinLogging
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Represents a connection to an AWS S3 bucket.
 */
internal object S3Storage {

    private val log = KotlinLogging.logger {}

    private val clientRegion = Regions.US_WEST_2
    private val bucketName = "automatic-minecraft-data"

    private val s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(clientRegion)
            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
            .build()

    fun writeByteArrayOutputStream(byteArrayOutputStream: ByteArrayOutputStream, s3Filename: String) {
        log.info("Will upload zip file to: $s3Filename")

        val metadata = ObjectMetadata()
        metadata.contentType = "application/zip"
        val inputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val request = PutObjectRequest(bucketName, s3Filename, inputStream, metadata)

        s3Client.putObject(request)
        log.info("Upload complete: $s3Filename")
    }

    fun readFile(filename: String): InputStream {
        val requestedObject = getFullS3Object(filename)
        return requestedObject.objectContent
    }

    private fun getFullS3Object(key: String): S3Object {
        return s3Client.getObject(GetObjectRequest(bucketName, key))
    }
}
