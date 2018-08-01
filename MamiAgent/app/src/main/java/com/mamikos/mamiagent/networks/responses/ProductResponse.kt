package com.mamikos.mamiagent.networks.responses

import com.mamikos.mamiagent.ProductEntity

data class ProductResponse(var status: String, var list: ArrayList<ProductEntity>)
{
    override fun toString(): String
    {
        return "ProductResponse(status='$status', list=$list)"
    }
}