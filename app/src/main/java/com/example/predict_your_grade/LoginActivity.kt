package com.example.predict_your_grade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var mailedit: EditText
    lateinit var passwordedit: EditText
    lateinit var loginbutton: Button
    lateinit var sign_google:TextView
    lateinit var signuptextview:TextView
    lateinit var progressbar:ProgressBar

    private lateinit var client:GoogleSignInClient



    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mailedit =findViewById(R.id.edtemail)
        passwordedit = findViewById(R.id.edtpassword)
        loginbutton = findViewById(R.id.btnlogin)
        signuptextview = findViewById(R.id.txtsignupfirst)
        sign_google = findViewById(R.id.signingoogle)
        progressbar = findViewById(R.id.progressBar)

        progressbar.visibility = View.INVISIBLE

        val password = findViewById<EditText>(R.id.edtpassword)
        val showpass = findViewById<Switch>(R.id.switch1)



        auth = FirebaseAuth.getInstance()

        loginbutton.setOnClickListener {

            var email = mailedit.text.toString().trim()
            var password = passwordedit.text.toString().trim()

            progressbar.visibility = View.VISIBLE

            //validate inputs
            if (email.isEmpty()|| password.isEmpty()){
                Toast.makeText(this, "One of the inputs is empty", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()

                        var gotomain = Intent(this, MainActivity::class.java)
                        startActivity(gotomain)
                        finish()
                    }else{
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        showpass.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                password.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }else{
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        signuptextview.setOnClickListener {
            var gotoregister = Intent(this, SignInActivity::class.java)
            startActivity(gotoregister)
            finish()
        }

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)

        sign_google.setOnClickListener {
            val intent = client.signInIntent
            startActivityForResult(intent,10001)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {task->
                    if (task.isSuccessful){

                        val i = Intent(this,MainActivity::class.java)
                        startActivity(i)

                    }else{
                        Toast.makeText(this,task.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                }

                }
        }

    //override fun onStart() {
        //super.onStart()
        //if (FirebaseAuth.getInstance().currentUser != null){
            //val i = Intent(this,MainActivity::class.java)
            //startActivity(i)
        }
    //}







