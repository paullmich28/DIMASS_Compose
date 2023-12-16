package com.example.dimass.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleDetailActivity : ComponentActivity() {
    private lateinit var dbRef: FirebaseFirestore
    private lateinit var dbAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme{
                ScheduleDetailPage()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun ScheduleDetailPage(){
        var isLoading by remember { mutableStateOf(true) }
        var itemSize by remember{ mutableStateOf(0) }

        val context = LocalContext.current

        val listTiming = listOf("Breakfast", "Lunch", "Dinner")

        var dates = remember{ mutableListOf<LocalDate>() }

        val breakfast = remember{ mutableListOf<String>() }
        val lunch = remember{ mutableListOf<String>() }
        val dinner = remember{ mutableListOf<String>() }

        dbRef = FirebaseFirestore.getInstance()
        val docId = intent.extras?.getString("id", "") ?: ""

        var mealsByDays = remember{ mutableListOf<Map<String, Any>>() }

        GlobalScope.launch(Dispatchers.IO) {
            val detailPlan = dbRef.collection("scheduling").document(docId)
            try{
                val doc = detailPlan.get().await()
                val program = doc.getString("program") ?: ""
                val startDate = doc.getString("startDate") ?: ""
                val endDate = doc.getString("endDate") ?: ""

                itemSize = if(program == "Daily"){
                    1
                }else{
                    7
                }

                if(program == "Daily"){
                    val planning = doc.get("planning") as? Map<String, Any>
                    val meals = planning?.get("meals") as? List<Map<String, Any>>
                    val nutrients = planning?.get("nutrients") as? Map<String, Any>

                    var counter = 0

                    meals?.forEach {map ->
                        when(counter % 3){
                            0 -> breakfast.add(map["title"].toString())
                            1 -> lunch.add(map["title"].toString())
                            2 -> dinner.add(map["title"].toString())
                        }

                        counter++
                    }

                    isLoading = false
                }else{
                    val days = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
                    val planning = doc.get("planning") as? Map<String, Any>

                    days.forEach{day ->
                        val mealsEachDay = planning?.get(day) as? Map<String, Any> ?: mapOf()
                        mealsByDays.add(mealsEachDay)
                    }

                    mealsByDays.forEach {
                        val meals = it["meals"] as? List<Map<String, Any>>
                        var counter = 0

                        meals?.forEach {map ->
                            when(counter % 3){
                                0 -> breakfast.add(map["title"].toString())
                                1 -> lunch.add(map["title"].toString())
                                2 -> dinner.add(map["title"].toString())
                            }

                            counter++
                        }
                    }

                    isLoading = false
                }

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val parsedDateStart = LocalDate.parse(startDate, formatter)
                val parsedDateEnd = LocalDate.parse(endDate, formatter)

                while(!parsedDateStart.isAfter(parsedDateEnd)){
                    dates.add(parsedDateStart)
                    parsedDateStart.plusDays(1)
                }
            }catch(e: Exception){

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
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){
                    Text(
                        text = "Scheduling Detail: Nama" ,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    LazyColumn{
                        items(count = itemSize){item ->
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
                                        text = "Start Date: ${dates[item]}",
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
                                        text = "${listTiming[0]}: ${breakfast[item]}",
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
                                        text = "${listTiming[1]}: ${lunch[item]}",
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
                                        text = "${listTiming[2]}: ${dinner[item]} ",
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
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun ScheduleDetailPreview() {
        DIMASSTheme {
            ScheduleDetailPage()
        }
    }
}

