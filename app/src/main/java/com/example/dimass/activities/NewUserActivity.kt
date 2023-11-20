package com.example.dimass.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen

class NewUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }

    @Composable
    fun NewUserPage(){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FormNewUser()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FormNewUser(){
        var weight by remember { mutableStateOf("") }
        var height by remember { mutableStateOf("") }

        val context = LocalContext.current

        Spacer(modifier = Modifier.padding(0.dp, 15.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = {weight = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("Weight (in kg)") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )

        OutlinedTextField(
            value = height,
            onValueChange = {height = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("Height (in cm)") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )

        ElevatedButton(
            onClick = {
                if(
                    weight.isEmpty() ||
                    height.isEmpty()
                ){
                    Toast.makeText(context, "Please fill the form", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, "Redirected to next page", Toast.LENGTH_LONG).show()
                }
            },
            content = { Text(
                "Submit",
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
    fun GreetingText(){

    }

    @Preview(showBackground = true)
    @Composable
    fun NewUserPagePreview(){
        DIMASSTheme {
            NewUserPage()
        }
    }
}

