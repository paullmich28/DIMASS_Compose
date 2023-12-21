package com.example.dimass.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.dimass.R
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()

        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this@LoginActivity, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                // A surface container using the 'background' color from the theme
                LoginPage()
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun LoginPage() {
        var isLoading by remember { mutableStateOf(false) }
        var height by remember{ mutableStateOf(0L) }
        var weight by remember{ mutableStateOf(0L) }

        val dbAuth = FirebaseAuth.getInstance()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    LogoSignIn()
                    FormSignIn()
                }

                Row(
                    modifier = Modifier
                        .weight(1f, false)
                        .padding(0.dp, 20.dp)
                ) {
                    SignUpOption()
                }
            }
        }
    }


    @Composable
    fun LogoSignIn(){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(0.dp, 40.dp, 0.dp, 0.dp)
                .size(235.dp, 235.dp)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FormSignIn(){
        var email by remember { mutableStateOf("") }
        var password by remember {mutableStateOf("")}
        
        val context = LocalContext.current

        val dbAuth = FirebaseAuth.getInstance()

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            label = {
                Text(
                    text = "Email",
                    color = Color.Black
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(0.dp, 40.dp, 0.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                textColor = Color.Black
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(
                    text = "Password",
                    color = Color.Black
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(0.dp, 0.dp),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                textColor = Color.Black
            )
        )

        ElevatedButton(
            onClick = {
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "Your account isn't valid", Toast.LENGTH_LONG).show()
                }else{
                    dbAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener{
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@LoginActivity, MainPageActivity::class.java))
                            finish()
                        }.addOnFailureListener{
                            Toast.makeText(context, "Account not valid", Toast.LENGTH_LONG).show()
                        }
                }
            },
            content = { Text(
                "Login",
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

    @Composable
    fun SignUpOption(){
        val context = LocalContext.current
        Text("Don't have an account? ")
        Text(
            "Sign Up",
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable {
                    startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
                }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        DIMASSTheme {
            LoginPage()
        }
    }
}

