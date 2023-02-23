package com.photosian.models

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ImageDTO(
    val urls: List<String>
)
