package com.example.shopexpress1.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopexpress1.R
import com.example.shopexpress1.model.Category

class DashboardViewModel : ViewModel() {

    private val _categoryList = MutableLiveData<ArrayList<Category>>().apply {
        value = fetchData()
    }


    private fun fetchData(): ArrayList<Category> {
        val list = ArrayList<Category>()
        list.add(Category(R.drawable.image7,"Smoothie Box"))
        list.add(Category(R.drawable.meal,"Meal Box"))
        list.add(Category(R.drawable.kitchen,"Kitchen Utilities"))
        list.add(Category(R.drawable.fish,"Wholesale Items"))
        return list
    }

    val categoryList:LiveData<ArrayList<Category>> =_categoryList
}