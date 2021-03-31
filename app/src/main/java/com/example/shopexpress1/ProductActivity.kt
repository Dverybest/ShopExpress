package com.example.shopexpress1

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopexpress1.model.Product
import com.example.shopexpress1.ui.home.ProductRecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_product.*


class ProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productRecyclerAdapter: ProductRecyclerAdapter
    private lateinit var productList:ArrayList<Product>
    private lateinit var productList2:ArrayList<Product>
    private  lateinit var shoppingCartCounter:TextView
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        recyclerView = findViewById(R.id.recyclerView);
        productList = ArrayList();
        productList2 = ArrayList();
        productRecyclerAdapter = ProductRecyclerAdapter(this,productList)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = productRecyclerAdapter

        fetchData2(intent.getStringExtra("category")!!)
    }

    private fun fetchData2(category: String) {
        progressBar.visibility = View.VISIBLE
        productList.clear()
        db.collection("product")
            .document(category).collection("products")
            .get()
            .addOnCompleteListener {
                    task->
                if(task.isSuccessful ){
                   task.result?.forEach {result->
                       val product :Product = result.toObject(Product::class.java)
                       productList.add(product)
                       productList2.add(product)
                   }
                }else {
                    Log.e("error", "Error getting documents.", task.getException());
                }
                if(productList.isEmpty()){
                    Toast.makeText(this, "Nothing to show in this category",
                        Toast.LENGTH_SHORT).show()
                }
                productRecyclerAdapter.notifyDataSetChanged();
            }
        progressBar.visibility = View.GONE

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)

        val itemMessages = menu!!.findItem(R.id.cart)
        itemMessages.setActionView(R.layout.cart)
        val badgeLayout = itemMessages.actionView as RelativeLayout
        badgeLayout.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }
        shoppingCartCounter = badgeLayout.findViewById(R.id.badge_textView) as TextView
        shoppingCartCounter.setVisibility(View.GONE) // initially hidden
        val searchManager =  getSystemService(Context.SEARCH_SERVICE) as SearchManager;
        val searchMenu = menu.findItem(R.id.search);
        val searchView =  searchMenu.getActionView() as SearchView;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                search(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                search(s)
                return false
            }
        })
        render();
        return super.onCreateOptionsMenu(menu)
    }

    fun search(query: String) {
        productList.clear()

        productList2.forEach {
                it->
            productList.add(it)
        }
        if(query==""|| TextUtils.isEmpty(query)){
            Log.e("TAG","reset")
            productRecyclerAdapter.notifyDataSetChanged();
            return;
        }
        productList.clear()

        productList2.forEach {
                i->
            Log.e("TAG",i.name)
            if(i.name.toLowerCase().contains(query.toLowerCase())){
                Log.e("TAG",i.name.toLowerCase())
                productList.add(i)
            }
        }
        productRecyclerAdapter.notifyDataSetChanged();
    }
    override fun onResume() {
        super.onResume()
        render()
    }
    private fun render() {
        if(::shoppingCartCounter.isInitialized){
            if(Db.dataset.size!=0){
                shoppingCartCounter.visibility = View.VISIBLE
                shoppingCartCounter.setText("${Db.dataset.size}")
            }else{
                shoppingCartCounter.visibility = View.GONE
                shoppingCartCounter.setText("${Db.dataset.size}")
            }
        }
    }
}
