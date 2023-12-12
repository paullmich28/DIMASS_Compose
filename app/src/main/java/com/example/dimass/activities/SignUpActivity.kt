package com.example.dimass.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.R
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                SignUpPage()
            }
        }
    }

    @Composable
    fun SignUpPage(){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ){
            Image(
                painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                contentDescription = "Back Button",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp, 10.dp, 0.dp, 0.dp)
                    .size(48.dp, 48.dp)
                    .clickable {
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    }
            )
            Column{
                LogoSignUp()
                FormSignUp()
            }
        }
    }

    @Composable
    fun LogoSignUp(){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(235.dp, 235.dp)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FormSignUp(){
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember{ mutableStateOf("") }
        var password by remember{ mutableStateOf("") }
        var confirmPassword by remember{ mutableStateOf("") }

        val dbAuth = FirebaseAuth.getInstance()
        val dbStore = FirebaseFirestore.getInstance()

        val context = LocalContext.current

        Spacer(modifier = Modifier.padding(0.dp, 15.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = {firstName = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = {lastName = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )

        ElevatedButton(
            onClick = {
                if(
                    firstName.isEmpty() ||
                    lastName.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty() ||
                    confirmPassword.isEmpty()
                    ){
                    Toast.makeText(context, "Please fill the form", Toast.LENGTH_LONG).show()
                }else{
                    if(password != confirmPassword){
                        Toast.makeText(context, "Password and confirm password must be the same", Toast.LENGTH_LONG).show()
                    }else{
                        dbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                            if(it.isSuccessful){
                                val id: String = dbAuth.currentUser?.uid ?: ""
                                val hashMap = hashMapOf(
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "weight" to 0f,
                                    "height" to 0f,
                                    "bmi" to 0f,
                                    "program" to "",
                                    "profilePic" to ""
                                )

                                dbStore.collection("accounts")
                                    .document(id)
                                    .set(hashMap)
                                    .addOnCompleteListener{
                                        Toast.makeText(context, "Account created", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this@SignUpActivity, NewUserActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                            }else{
                                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            },
            content = { Text(
                "Sign Up",
                fontSize = 16.sp
            ) },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = BottleGreen,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(0.dp, 50.dp)
                .fillMaxWidth(0.6f)
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun SignUpPreview(){
        DIMASSTheme {
            SignUpPage()
        }
    }
}