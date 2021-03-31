package com.example.shopexpress1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.create
import kotlinx.android.synthetic.main.activity_register.password
import kotlinx.android.synthetic.main.activity_register.username
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private val TAG = "Register"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private  var currentUser: FirebaseUser? =null
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressBar = findViewById(R.id.progressBar)
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        create.setOnClickListener{


            val email = username.text.toString()
            val pass = password.text.toString()
            val fullName = name.text.toString()
            val phoneNumber = phone.text.toString()
            val homeAddress = address.text.toString()
            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(fullName)||TextUtils.isEmpty(phoneNumber)||TextUtils.isEmpty(homeAddress)){
                Toast.makeText(this@RegisterActivity, "Please fill all fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pass.length<6){
                Toast.makeText(this@RegisterActivity, "password should not be less than 6 characters",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{ task ->

                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "createUserWithEmail:success")
                            // Create a new user with a first, middle, and last name
                            val user = HashMap<String,Any>()
                            user.put("name", fullName)
                            user.put("email", email)
                            user.put("phoneNumber", phoneNumber)
                            user.put("homeAddress", homeAddress)

                             db.collection("users")
                                 .document(email)
                                 .set(user)
                                .addOnSuccessListener{ documentReference ->
                                    progressBar.visibility = View.GONE
                                    Log.e(TAG,"DocumentSnapshot added with ID: " + documentReference )
                                    Toast.makeText(this@RegisterActivity, "Registration successful.",Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener(OnFailureListener { e ->
                                    Log.e(TAG,"Error adding document", e )
                                })

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.visibility = View.GONE
                            Log.e(TAG,"createUserWithEmail:failure",task.exception )
                            Toast.makeText(this@RegisterActivity, "Registration Failed.",Toast.LENGTH_SHORT).show()

                        }
                    }
        }
    }


}
