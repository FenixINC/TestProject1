package com.taras.admin.app_mobile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthorizationActivity : AppCompatActivity() {
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mReference: DatabaseReference? = null

    private var mUserPassword: EditText? = null
    private var mUserEmail: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mReference = mFirebaseDatabase?.getReference("Authorization")

        mUserPassword = findViewById(R.id.user_password)
        mUserEmail = findViewById(R.id.user_email)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser != null) {
                val intent = Intent(this@AuthorizationActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }

        findViewById<View>(R.id.login).setOnClickListener { v ->
            val userPassword = mUserPassword?.text.toString()
            val userEmail = mUserEmail?.text.toString()

            if (!TextUtils.isEmpty(userPassword) && !TextUtils.isEmpty(userEmail)) {
                mFirebaseAuth?.signInWithEmailAndPassword(userEmail, userPassword)?.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(applicationContext, getString(R.string.authorization_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth?.addAuthStateListener(mAuthListener!!)
    }
}