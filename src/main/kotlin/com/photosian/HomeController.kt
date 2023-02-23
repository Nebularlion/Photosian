package com.photosian
import com.photosian.models.ImageDTO
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import service.S3Service

@Controller
open class ImageController(val s3Service: S3Service) {

    @Get
    fun index() = mapOf("message" to "Hello World")

    @Get("/images")
    fun getImageUrls(): HttpResponse<ImageDTO> {
        val imageDTO: ImageDTO = s3Service.listBucketObjects()
        return HttpResponse.ok(imageDTO)

    }
}