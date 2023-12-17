package com.example.dimass.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NewUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                // A surface container using the 'background' color from the theme
                NewUserPage()
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun NewUserPage(){
        var fName by remember{ mutableStateOf("") }
        var lName by remember{ mutableStateOf("") }
        var isLoading by remember{ mutableStateOf(true) }

        val id: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val dbRef = FirebaseFirestore.getInstance()

        GlobalScope.launch(Dispatchers.IO){
            try{
                val doc = dbRef
                    .collection("accounts")
                    .document(id)
                    .get()
                    .await()

                fName = doc.getString("firstName") ?: ""
                lName = doc.getString("lastName") ?: ""

                isLoading = false
            }catch(e: Exception){

            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.Center
        ){
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BottleGreen
                )
            }else{
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GreetingText(fName, lName)
                    FormNewUser()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FormNewUser(){
        var weight by remember { mutableStateOf("") }
        var height by remember { mutableStateOf("") }

        val context = LocalContext.current

        val id: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val dbRef = FirebaseFirestore
            .getInstance()
            .collection("accounts")
            .document(id)

        OutlinedTextField(
            value = weight,
            onValueChange = {weight = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            label = { Text("Weight (in kg)") },
            modifier = Modifier
                .padding(0.dp, 50.dp, 0.dp, 0.dp)
                .fillMaxWidth(0.6f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                textColor = Color.Black
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = {height = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            label = { Text("Height (in cm)") },
            modifier = Modifier
                .fillMaxWidth(0.6f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                textColor = Color.Black
            )
        )

        ElevatedButton(
            onClick = {
                if(
                    weight.isEmpty() ||
                    height.isEmpty()
                ){
                    Toast.makeText(context, "Please fill the form", Toast.LENGTH_LONG).show()
                }else{
                    val intent = Intent(this@NewUserActivity, BmiActivity::class.java)

                    dbRef.update(
                        mapOf(
                            "weight" to weight.toFloat(),
                            "height" to height.toFloat(),
                            "bmi" to (weight.toFloat()/((height.toFloat()/100) * (height.toFloat()/100)))
                        )
                    )
                    startActivity(intent)
                    finish()
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
    fun GreetingText(fName: String, lName: String){
        Text(
            text = "Hello $fName $lName"
        )

        Text(
            text = "Before we get started, please fill the form below"
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun NewUserPagePreview(){
        DIMASSTheme {
            NewUserPage()
        }
    }
}

