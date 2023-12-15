package com.example.dimass.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.R
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ScheduleDetailActivity : ComponentActivity() {
    private lateinit var dbRef: FirebaseFirestore
    private lateinit var dbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme{
                ScheduleDetailPage()
            }
        }
    }

    @Composable
    fun ScheduleDetailPage(){
        var isLoading by remember { mutableStateOf(true) }
        var itemSize by remember{ mutableStateOf(0) }

        dbRef = FirebaseFirestore.getInstance()
        val extras = intent.extras?.getString("id", "")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ){
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BottleGreen
                )
            }else{
                Image(
                    painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "Back Button",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp, 10.dp, 0.dp, 0.dp)
                        .size(48.dp, 48.dp)
                        .clickable {
                            startActivity(
                                Intent(
                                    this@ScheduleDetailActivity,
                                    MainPageActivity::class.java
                                )
                            )
                        }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .offset(y = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){
                    Text(
                        text = "Scheduling Detail: Nama" ,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Green
                        ),
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Start Date: Tanggal",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "End Date: Tanggal",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.height(40.dp))

                            Text(
                                text = "Breakfast: ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                            ElevatedButton(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = BottleGreen,
                                    contentColor = Color.White
                                ),
                            ) {
                                Text(
                                    text = "Detail About the Recipe",
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Lunch: ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                            ElevatedButton(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = BottleGreen,
                                    contentColor = Color.White
                                ),
                            ) {
                                Text(
                                    text = "Detail About the Recipe",
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Dinner: ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                            ElevatedButton(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = BottleGreen,
                                    contentColor = Color.White
                                ),
                            ) {
                                Text(
                                    text = "Detail About the Recipe",
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Total Nutrients:",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun ScheduleDetailPreview() {
        DIMASSTheme {
            ScheduleDetailPage()
        }
    }
}

