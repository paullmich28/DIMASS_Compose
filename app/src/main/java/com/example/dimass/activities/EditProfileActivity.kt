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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                EditProfilePage()
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @Composable
    fun EditProfilePage(){
        var isExpanded by remember { mutableStateOf(false) }
        var isLoading by remember{ mutableStateOf(true) }
        val context = LocalContext.current
        var fName by remember {
            mutableStateOf("")
        }

        var lName by remember {
            mutableStateOf("")
        }

        var weight by remember {
            mutableStateOf(0L)
        }

        var height by remember {
            mutableStateOf(0L)
        }

        var userProgram by remember {
            mutableStateOf("")
        }

        val id = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        LaunchedEffect(key1 = id){
            try{
                val dbRef = FirebaseFirestore
                    .getInstance()
                    .collection("accounts")
                    .document(id)

                try {
                    val docs = dbRef.get().await()

                    fName = docs.getString("firstName") ?: ""
                    lName = docs.getString("lastName") ?: ""
                    weight = docs.getLong("weight") ?: 0L
                    height = docs.getLong("height") ?: 0L
                    userProgram = docs.getString("program") ?: ""
                } catch (e: Exception) {
                    Log.e("exception", "Error", e)
                }
            }catch(e: Exception){
                Log.e("Exception", "Error", e)
            }finally {
                isLoading = false
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.TopCenter
        ){
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BottleGreen
                )
            }else{

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp, 100.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(100.dp, 100.dp)
                    )

                    Row{
                        OutlinedTextField(
                            value = fName,
                            onValueChange = {fName = it},
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            label = { Text("First Name") },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 5.dp)
                        )

                        OutlinedTextField(
                            value = lName,
                            onValueChange = {lName = it},
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            label = { Text("Last Name") },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(start = 5.dp)
                        )
                    }

                    OutlinedTextField(
                        value = "$weight",
                        onValueChange = {weight = it.toLong()},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        label = { Text("Weight") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = "$height",
                        onValueChange = {height = it.toLong()},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        label = { Text("Height") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = {isExpanded = it},
                        modifier = Modifier.padding(vertical = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = userProgram,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("Mass Gain")
                                },
                                onClick = {
                                    userProgram = "Mass Gain"
                                    isExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Diet")
                                },
                                onClick = {
                                    userProgram = "Diet"
                                    isExpanded = false
                                }
                            )
                        }
                    }

                    ElevatedButton(
                        onClick = {
                            if(
                                fName.isEmpty() ||
                                lName.isEmpty() ||
                                weight == 0L ||
                                height == 0L
                            ){
                                Toast.makeText(context, "Please fill the form", Toast.LENGTH_LONG).show()
                            }else{
                                val bmi = (weight/((height/100L) * (height/100L)))
                                FirebaseFirestore.getInstance().collection("accounts").document(id).update(
                                    mapOf(
                                        "firstName" to fName,
                                        "lastName" to lName,
                                        "weight" to weight,
                                        "height" to height,
                                        "bmi" to bmi,
                                        "program" to userProgram
                                    )
                                )

                                val intent = Intent(this@EditProfileActivity, MainPageActivity::class.java)
                                startActivity(intent)
                            }
                        },
                        content = { Text(
                            "Confirm Edit",
                            fontSize = 16.sp
                        ) },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BottleGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        DIMASSTheme {
            EditProfilePage()
        }
    }
}

