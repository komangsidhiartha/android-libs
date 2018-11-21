package com.mamikos.mamiagent.activity

data class ProductEntity(var id: Int, var name: String, var brand: String, var imageUrl: String, var description: String, var price: String)
{
    override fun toString(): String
    {
        return "ProductEntity(id=$id, name='$name', brand='$brand', imageUrl='$imageUrl', description='$description', price='$price')"
    }
}