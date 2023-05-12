package com.example.predict_your_grade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    lateinit var editfullname: EditText
    lateinit var editemail: EditText
    lateinit var editpassword: EditText
    lateinit var createbutton: Button

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        editfullname = findViewById(R.id.edtname)
        editemail = findViewById(R.id.edtemail)
        editpassword = findViewById(R.id.edtpassword)
        createbutton = findViewById(R.id.btncreate)

        val password = findViewById<EditText>(R.id.edtpassword)
        val showpass = findViewById<Switch>(R.id.switch2)


        //initialize firebase again
        auth = FirebaseAuth.getInstance()

        createbutton.setOnClickListener {
            var name = editfullname.text.toString().trim()
            var email = editemail.text.toString().trim()
            var password = editpassword.text.toString().trim()

            //validate inputs
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ){
                Toast.makeText(this, "One of the fields is empty", Toast.LENGTH_SHORT).show()
            }else{

                //create a user in firebase
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){

                    if (it.isSuccessful){
                        Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show()
                        var gotologin = Intent(this, LoginActivity::class.java)
                        startActivity(gotologin)
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

        showpass.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                password.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }else{
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

    }
}


