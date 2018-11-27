package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class PhotosApi : MamikosAgentBaseApi()
{
    class ListPhotoApi(var id: String): PhotosApi()
    class MediaApi: PhotosApi()
    class DeleteMediaApi(var id: Int): PhotosApi()
    class GetEditPhotoApi(var id: String): PhotosApi()

    override val basePath: String
        get() = "http://songturu.mamikos.com/api/v1"

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod
        get() {
            return when (this)
            {
                is ListPhotoApi -> APIMethod.GET
                is MediaApi -> APIMethod.UPLOAD
                is DeleteMediaApi -> APIMethod.POST
                is GetEditPhotoApi -> APIMethod.GET
                else -> APIMethod.GET
            }
        }
    override val path: String
        get() {
            return when (this)
            {
                is ListPhotoApi -> "photos/$id"
                is MediaApi -> "media"
                is DeleteMediaApi -> "delete/photo/$id"
                is GetEditPhotoApi -> "edit/$id"
                else -> ""
            }
        }
}