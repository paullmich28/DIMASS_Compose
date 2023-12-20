package com.example.dimass.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.lifecycle.lifecycleScope
import com.example.dimass.R
import com.example.dimass.model.Notification
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.example.dimass.ui.theme.Red
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ScheduleDetailActivity : ComponentActivity() {
    private lateinit var dbRef: FirebaseFirestore

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
    @Composable
    fun ScheduleDetailPage(){
        var isLoading by remember { mutableStateOf(true) }
        var itemSize by remember{ mutableStateOf(0) }

        val listTiming = listOf("Breakfast", "Lunch", "Dinner")

        val breakfast = remember{ mutableListOf<String>() }
        val lunch = remember{ mutableListOf<String>() }
        val dinner = remember{ mutableListOf<String>() }

        val calories = remember{ mutableListOf<String>() }
        val carbohydrates = remember{ mutableListOf<String>() }
        val fat = remember{ mutableListOf<String>() }
        val protein = remember{ mutableListOf<String>() }

        var startDateDisplay by remember{ mutableStateOf("") }
        var endDateDisplay by remember{ mutableStateOf("") }

        var scheduleName by remember{ mutableStateOf("") }
        var notificationIDs = remember{ listOf<Int>() }

        var urlFood = remember{ mutableListOf<String>() }

        dbRef = FirebaseFirestore.getInstance()
        val docId = intent.extras?.getString("id", "") ?: ""

        val mealsByDays = remember{ mutableListOf<Map<String, Any>>() }

        lifecycleScope.launch(Dispatchers.IO) {
            try{
                val detailPlan = dbRef.collection("scheduling").document(docId)
                try{
                    val doc = detailPlan.get().await()
                    val program = doc.getString("program") ?: ""
                    val startDate = doc.getString("startDate") ?: ""
                    val endDate = doc.getString("endDate") ?: ""

                    scheduleName = doc.getString("name") ?: ""
                    notificationIDs = doc.get("key_notification") as? List<Int> ?: listOf()

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

                            urlFood.add(map["sourceUrl"].toString())

                            counter++
                        }

                        calories.add(nutrients?.get("calories").toString())
                        carbohydrates.add(nutrients?.get("carbohydrates").toString())
                        fat.add(nutrients?.get("fat").toString())
                        protein.add(nutrients?.get("protein").toString())
                    }else{
                        val days = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
                        val planning = doc.get("planning") as? Map<String, Any>

                        days.forEach{day ->
                            val mealsEachDay = planning?.get(day) as? Map<String, Any> ?: mapOf()
                            mealsByDays.add(mealsEachDay)
                        }

                        mealsByDays.forEach {
                            val meals = it["meals"] as? List<Map<String, Any>>
                            val nutrients = it["nutrients"] as? Map<String, Any>
                            var counter = 0

                            meals?.forEach {map ->
                                when(counter % 3){
                                    0 -> breakfast.add(map["title"].toString())
                                    1 -> lunch.add(map["title"].toString())
                                    2 -> dinner.add(map["title"].toString())
                                }

                                counter++

                                urlFood.add(map["sourceUrl"].toString())
                            }

                            calories.add(nutrients?.get("calories").toString())
                            carbohydrates.add(nutrients?.get("carbohydrates").toString())
                            fat.add(nutrients?.get("fat").toString())
                            protein.add(nutrients?.get("protein").toString())
                        }
                    }

                    startDateDisplay = startDate
                    endDateDisplay = endDate
                    isLoading = false
                }catch (e: Exception){
                    Log.d("Testing", e.message!!)
                }
            }catch(e: Exception){
                Log.d("Testing", e.message!!)
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
                        .padding(20.dp, 10.dp, 0.dp, 0.dp)
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
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 60.dp)
                        .offset(y = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ){
                    Text(
                        text = "Scheduling Detail: $scheduleName" ,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp),
                        color = Color.Black
                    )

                    Text(
                        text = "$startDateDisplay - $endDateDisplay",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color.Black
                    )

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
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
                                        text = "Day ${item + 1}",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "${listTiming[0]}: ${breakfast[item]}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 20.sp,
                                        color = Color.Black
                                    )
                                    ElevatedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFood[item]))
                                            startActivity(intent)
                                        },
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
                                        fontSize = 20.sp,
                                        color = Color.Black
                                    )
                                    ElevatedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFood[item + 1]))
                                            startActivity(intent)
                                        },
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
                                        fontSize = 20.sp,
                                        color = Color.Black
                                    )
                                    ElevatedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFood[item + 2]))
                                            startActivity(intent)
                                        },
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
                                        fontSize = 20.sp,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Calories: ${calories[item]}",
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Carbohydrates: ${carbohydrates[item]}",
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Fat: ${fat[item]}",
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Protein: ${protein[item]}",
                                        color = Color.Black
                                    )
                                }
                            }

                            if(item == itemSize - 1){
                                ElevatedButton(
                                    onClick = {
                                        val builder = AlertDialog.Builder(this@ScheduleDetailActivity)
                                        builder.setTitle("Delete Schedule")
                                        builder.setMessage("Are you sure you want to delete the schedule?")
                                        builder.apply{
                                            setPositiveButton("Yes"){_, _ ->
                                                notificationIDs.forEach { id ->
                                                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                                    val intent = Intent(applicationContext, Notification::class.java)
                                                    val pendingIntent = PendingIntent.getBroadcast(
                                                        applicationContext,
                                                        id,
                                                        intent,
                                                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                                                    )
                                                    alarmManager.cancel(pendingIntent)
                                                }
                                                dbRef.collection("scheduling").document(docId).delete()
                                                val intent = Intent(this@ScheduleDetailActivity, MainPageActivity::class.java)
                                                startActivity(intent)
                                            }

                                            setNegativeButton("No"){_, _ ->

                                            }
                                        }.create().show()
                                    },
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        contentColor = Color.White,
                                        containerColor = Red
                                    ),
                                    modifier = Modifier.padding(5.dp)
                                ) {
                                    Text(
                                        text = "Delete Scheduler",
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGreen),
                contentAlignment = Alignment.TopCenter
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .offset(y = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){
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
                                text = "Day",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Breakfast",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            ElevatedButton(
                                onClick = {
//                            val parsedDate = LocalDate.now()
//                            val hour = LocalDateTime.now().hour
//                            val minute = LocalDateTime.now().minute
//                            for(i in 0..5){
//                                scheduleNotification(parsedDate, hour, minute + 15, i)
//                            }
                                },
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
                                text = "Breakfast",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            ElevatedButton(
                                onClick = {
//                            val parsedDate = LocalDate.now()
//                            val hour = LocalDateTime.now().hour
//                            val minute = LocalDateTime.now().minute
//                            for(i in 0..5){
//                                scheduleNotification(parsedDate, hour, minute + 15, i)
//                            }
                                },
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
                                text = "Breakfast ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            ElevatedButton(
                                onClick = {
//                            val parsedDate = LocalDate.now()
//                            val hour = LocalDateTime.now().hour
//                            val minute = LocalDateTime.now().minute
//                            for(i in 0..5){
//                                scheduleNotification(parsedDate, hour, minute + 15, i)
//                            }
                                },
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
                                fontSize = 20.sp,
                                color = Color.Black
                            )

                            Text(
                                text = "Calories:",
                                color = Color.Black
                            )

                            Text(
                                text = "Carbohydrates: ",
                                color = Color.Black
                            )

                            Text(
                                text = "Fat: ",
                                color = Color.Black
                            )

                            Text(
                                text = "Protein:",
                                color = Color.Black
                            )
                        }
                    }

                    ElevatedButton(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.elevatedButtonColors(
                            contentColor = Color.LightGray,
                            containerColor = Red
                        )
                    ) {
                        Text(
                            text = "Delete Scheduler",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

