package com.example.shopexpress1.model

class Order () {
    lateinit var imageUrl: String
    lateinit var name: String
    lateinit var category: String
    lateinit var price:String
    lateinit var description:String
    lateinit var time :String
    lateinit var email : String

    constructor( imageUrl: String,  name: String, category: String, price:String, description:String,time:Long,email:String):this(){
        this.imageUrl =imageUrl
        this.name = name
        this.category = category
        this.price = price
        this.description = description
        this.time = time.toString()
        this.email = email

    }
}