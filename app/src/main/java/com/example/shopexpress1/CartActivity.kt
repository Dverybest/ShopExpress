package com.example.shopexpress1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shopexpress1.model.Order
import com.example.shopexpress1.model.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var textView: TextView
    private var list: ArrayList<Product> = ArrayList()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private  var currentUser: FirebaseUser? =null
    private  lateinit var shopingCartCounter: TextView
    private lateinit var listAdapter: ListViewAdapter
    private var total:Double = 0.0
    private lateinit var fab:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title ="Shop Cart"
        listView = findViewById(R.id.listView)
        progressBar = findViewById(R.id.progressBar)
//        textView = findViewById(R.id.total)
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fab = findViewById(R.id.fab)
        listAdapter = ListViewAdapter(this,R.layout.list_view,list)

        listView.adapter = listAdapter


        if(Db.dataset.size!=0){
            Db.dataset.forEach{
                    product-> list.add(product)
                total= total + product.price.toDouble()
            }
            listAdapter.notifyDataSetChanged()
//            textView.setText("N${total}")
        }else{
          Toast.makeText(this,"Nothing in cart yet",Toast.LENGTH_LONG).show()
        }

        fab.setOnClickListener {
            if(!Db.dataset.isEmpty()) {
                val  a = AlertDialog.Builder(this).setMessage("The total cost of your orders is N$total. Payment will be made on delivery").setCancelable(false)
                    .setTitle("Confirm Check-Out")
                    .setNegativeButton("cancel", {dialog, which ->
                        dialog.dismiss()
                    }).setPositiveButton("Proceed",{dialog, which ->
                       post()
                    })
               a.create().show()
            }else{
                Toast.makeText(this,"Nothing in cart yet",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun post(){

        Db.dataset.forEach {

            val orders = Order(it.imageUrl,it.name,it.price,it.category,it.description,Date().time,currentUser!!.email!!)
           val db = FirebaseFirestore.getInstance()

            db.collection("order")
                .add(orders).addOnSuccessListener {
                    progressBar.visibility = View.GONE


                }.addOnFailureListener {
                    Toast.makeText(this,"error : ${it}",Toast.LENGTH_SHORT).show()

                    progressBar.visibility = View.GONE
                    return@addOnFailureListener
                }
        }
        val  a = AlertDialog.Builder(this).setMessage("Your orders have been recieved, you will recieve a call from us soon. Please stay close to your phone").setCancelable(false)
            .setTitle("Received")
            .setNegativeButton("Ok", {dialog, which ->

                Db.dataset.clear()
                list.clear()
                listAdapter.notifyDataSetChanged()
                dialog.dismiss()
            })
        a.create().show()
    }
}
