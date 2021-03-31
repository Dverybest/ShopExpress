package com.example.shopexpress1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopexpress1.model.Product

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {

    private  lateinit var shopingCartCounter:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val product:Product = intent.getSerializableExtra("product") as Product

        Glide.with(this)
            .load(product.imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.ic_shopping_cart_black_24dp))
            .into(img)
       // img.setImageResource(product.imageUrl)
        price.setText(product.price)
        name.setText("N${product.name}")
        description.setText(product.description)
        addCart.setOnClickListener{
            Db.dataset.add(product)
            render()
            Toast.makeText(this,"Added to cart",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)

        val itemMessages = menu!!.findItem(R.id.cart)
        val item = menu.findItem(R.id.search)
        item.setVisible(false)
        itemMessages.setActionView(R.layout.cart)
        val badgeLayout = itemMessages.actionView as RelativeLayout
        badgeLayout.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }
        shopingCartCounter = badgeLayout.findViewById(R.id.badge_textView) as TextView
        shopingCartCounter.setVisibility(View.GONE) // initially hidden
        render();
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        render()
    }
    private fun render() {
        if(::shopingCartCounter.isInitialized){
            if(Db.dataset.size!=0){
                shopingCartCounter.visibility = View.VISIBLE
                shopingCartCounter.setText("${Db.dataset.size}")
            }else{
                shopingCartCounter.visibility = View.GONE
                shopingCartCounter.setText("${Db.dataset.size}")
            }
        }
    }
}
