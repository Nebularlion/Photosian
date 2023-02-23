package service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ListObjectsV2Result
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.photosian.models.ImageDTO
import jakarta.inject.Singleton
import java.net.URL
import java.time.Instant
import java.util.*

val BUCKET_NAME: String = System.getenv("S3_BUCKET")
val BUCKET_REGION: String = System.getenv("S3_REGION")

@Singleton
class S3Service {
    private val s3Client: AmazonS3 = AmazonS3ClientBuilder
        .standard()
        .withRegion(BUCKET_REGION)
        .build()

    fun listBucketObjects(): ImageDTO {
        val result: ListObjectsV2Result = s3Client.listObjectsV2(BUCKET_NAME)
        val objects: List<S3ObjectSummary> = result.objectSummaries

        val presignedUrls: List<String> = objects.map{s3Object ->
            generatePresignedForObject(s3Object.key)
        }

        return ImageDTO(presignedUrls)
    }

    private fun generatePresignedForObject(objectKey: String): String {
        val expiration = Date()
        var expTimeMillis: Long = Instant.now().toEpochMilli()
        expTimeMillis += (1000 * 60 * 60).toLong()
        expiration.time = expTimeMillis
        
        val generatePresignedUrlRequest: GeneratePresignedUrlRequest = GeneratePresignedUrlRequest(BUCKET_NAME, objectKey)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration)
        val url: URL = s3Client.generatePresignedUrl(generatePresignedUrlRequest)

       return url.toString()
    }
}