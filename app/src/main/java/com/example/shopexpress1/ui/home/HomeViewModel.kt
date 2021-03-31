package com.example.shopexpress1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopexpress1.R
import com.example.shopexpress1.model.Product

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    private val _imageUrlList = MutableLiveData<ArrayList<Int>>().apply {
        value = fetchData()
    }

    private fun fetchData(): ArrayList<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.image7)
        list.add(R.drawable.image9)
        list.add(R.drawable.image10)
        list.add(R.drawable.image6)
        return list
    }

    val text: LiveData<String> = _text

    val imageUrlList:LiveData<ArrayList<Int>> =_imageUrlList

    private val _productList = MutableLiveData<ArrayList<Product>>().apply {
        value = fetchData2()
    }


    private fun fetchData2(): ArrayList<Product> {
        val list = ArrayList<Product>()
//        list.add(Product(R.drawable.egusi,"Egusi Box","Meal Box","4000.00","500g Egusi (Ground) 400g ugwu leaf,40ml Palm oil, 10 Scotch bonnets (fresh pepper)" +
//                "500g beef/Goatmeat/chicken, 40g Stockfish, 2pairs of seasoning cubes, 40g salt, 2 Large onion, Dry fish"))
//        list.add(Product(R.drawable.fish,"Stockfish","Wholesale Items","10000.00",""))
//        list.add(Product(R.drawable.stew,"Stew Box","Meal Box","4000.00",""))
        return list
    }

    val productList:LiveData<ArrayList<Product>> =_productList
}