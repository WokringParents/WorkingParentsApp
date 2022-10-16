package com.example.workingparents

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("fileName")
    val fileName: String,

    @SerializedName("fileDownloadUri")
    val fileDownloadUri: String,

    @SerializedName("fileType")
    val fileType: String,

    @SerializedName("size")
    val size: Long
    )
