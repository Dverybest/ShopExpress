package com.example.shopexpress1

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import androidx.core.app.ActivityCompat
import java.io.File
import android.graphics.BitmapFactory
import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.shopexpress1.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.content_post.*
import java.util.*
import kotlin.collections.ArrayList

class PostActivity : AppCompatActivity() {

    private lateinit var category: Spinner
    private lateinit var title: EditText
    private lateinit var description:EditText
    private lateinit var location:EditText
    private lateinit var price:EditText
    private lateinit var image: ImageView
    private lateinit var upload: Button
    private lateinit var storage: FirebaseStorage
    val db = FirebaseFirestore.getInstance()
    private lateinit var f: File
    private  lateinit var  uri :Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        category = findViewById(R.id.pCategory);
        title = findViewById(R.id.pName);
        description = findViewById(R.id.pDescription);
        price = findViewById(R.id.pPrice);
        upload = findViewById(R.id.upload);

        image  = findViewById(R.id.pImage);

        category.setAdapter(load());
        upload.setOnClickListener {

            val photoPickerIntent = Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");

            if (ActivityCompat.checkSelfPermission(this@PostActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@PostActivity,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                startActivityForResult(photoPickerIntent, 1);

            }else{

                val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE ,android.Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(this@PostActivity,permission,1);
                startActivityForResult(photoPickerIntent, 1);
            }
        }

        fab.setOnClickListener {
            if(::f.isInitialized&&!TextUtils.isEmpty(title.text)&&!TextUtils.isEmpty(price.text)&&!TextUtils.isEmpty(description.text)&&category.selectedItemPosition!=0){
                upload()
            }else{
                Toast.makeText(this,"Please fill everything corretly",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data?.getData()
                uri = selectedImage!!
                val filePath = selectedImage?.let { getPath(it) }
                 f = filePath?.let { File(it) }!!
                //compressed = compress(f)

                val bitmap = BitmapFactory.decodeFile(f.path)
                image.setImageBitmap(bitmap)

            }
    }

    fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        // imagePath = cursor.getString(column_index);

        return cursor.getString(column_index)
    }

    fun load(): ArrayAdapter<*> {
        val listLevel = ArrayList<String>()

        listLevel.add("Select Categories")
        listLevel.add("Smoothie Box")
        listLevel.add("Meal Box")
        listLevel.add("Wholesale Items")
        listLevel.add("Kitchen Utilites")
        listLevel.add("Featured Products")
        return ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, listLevel)
    }

    fun upload(){
        progressBar.visibility = View.VISIBLE
        storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val itemImage = storageReference.child("images/${UUID.randomUUID()}${f.name}")

         itemImage.putFile(uri).addOnSuccessListener {
            itemImage.downloadUrl.addOnSuccessListener {
                val product:Product = Product(it.toString(),pName.text.toString(),category.selectedItem.toString(),price.text.toString(),description.text.toString())
                db.collection("product").document(category.selectedItem.toString()).collection("products")
                    .add(product).addOnSuccessListener {
                    Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }.addOnFailureListener {
                    Toast.makeText(this,"error : ${it}",Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }.addOnFailureListener{
                Toast.makeText(this,"error : ${it}",Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }

        }.addOnFailureListener{
             Toast.makeText(this,"error : ${it}",Toast.LENGTH_SHORT).show()
             progressBar.visibility = View.GONE
         }
    }
}
