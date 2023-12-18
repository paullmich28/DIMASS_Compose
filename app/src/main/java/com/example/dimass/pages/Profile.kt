package com.example.dimass.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.R
import com.example.dimass.activities.LoginActivity
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

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ProfileScreen(){
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

    var bmi by remember {
        mutableStateOf(0L)
    }

    var isLoading by remember{ mutableStateOf(true) }

    val id = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    GlobalScope.launch(Dispatchers.IO){
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
                bmi = docs.getLong("bmi") ?: 0L
            } catch (e: Exception) {
                Log.e("exception", "Error", e)
            }

            isLoading = false
        }catch(e: Exception){
            Log.e("Exception", "Error", e)
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
                    .padding(0.dp, 100.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_account_circle_24),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp, 100.dp)
                )

                Text(
                    text = "$fName $lName",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Height: $height",
                    color = Color.Black
                )

                Text(
                    text = "Weight: $weight",
                    color = Color.Black
                )

                Text(
                    text = "BMI: $bmi",
                    color = Color.Black
                )
                
                Row(
                    modifier = Modifier.padding(vertical = 25.dp, horizontal = 25.dp)
                ){
                    ElevatedButton(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            Toast.makeText(context, "Signed Out from your account", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 5.dp),

                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BottleGreen,
                            contentColor = Color.White
                        ),

                        content = {
                            Text(
                                text = "Edit Profile"
                            )
                        }
                    )

                    ElevatedButton(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            Toast.makeText(context, "Signed Out from your account", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(start = 5.dp),

                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BottleGreen,
                            contentColor = Color.White
                        ),

                        content = {
                            Text(
                                text = "Sign Out"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    DIMASSTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(0.dp, 48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_account_circle_24),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp, 100.dp)
                )

                Text(
                    text = "Nama",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                Text(
                    text = "Height:",
                    color = Color.Black
                )

                Text(
                    text = "Weight:",
                    color = Color.Black
                )

                Text(
                    text = "BMI:",
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.padding(vertical = 25.dp, horizontal = 25.dp)
                ){
                    ElevatedButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 5.dp),

                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BottleGreen,
                            contentColor = Color.White
                        ),

                        content = {
                            Text(
                                text = "Edit Profile"
                            )
                        }
                    )

                    ElevatedButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(start = 5.dp),

                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BottleGreen,
                            contentColor = Color.White
                        ),

                        content = {
                            Text(
                                text = "Sign Out"
                            )
                        }
                    )
                }
            }
        }
    }
}