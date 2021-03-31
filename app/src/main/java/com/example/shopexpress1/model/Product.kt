package com.example.shopexpress1.model

import java.io.Serializable

class Product() :Serializable {

    lateinit var imageUrl: String
    lateinit var name: String
    lateinit var category: String
    lateinit var price:String
    lateinit var description:String

    constructor( imageUrl: String,  name: String, category: String, price:String, description:String):this(){
        this.imageUrl =imageUrl
        this.name = name
        this.category = category
        this.price = price
        this.description = description
    }
}