package com.example.dimass

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                // A surface container using the 'background' color from the theme
                LoginPage()
            }
        }
    }

    @Composable
    fun LoginPage() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Logo()
                    Form()
                }

                Row(
                    modifier = Modifier
                        .weight(1f, false)
                        .padding(0.dp, 20.dp)
                ){
                    SignUpOption()
                }
            }
        }
    }

    @Composable
    fun Logo(){
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
    fun Form(){
        var email by remember { mutableStateOf("") }
        var password by remember {mutableStateOf("")}
        
        val context = LocalContext.current


        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            label = {
                Text("Email")
            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(0.dp, 40.dp, 0.dp, 0.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            label = {
                Text("Password")
            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(0.dp, 0.dp)
        )

        ElevatedButton(
            onClick = {
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "Your account isn't valid", Toast.LENGTH_LONG).show()
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

