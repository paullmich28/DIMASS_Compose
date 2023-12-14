package com.example.dimass.pages

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.activities.NewScheduleActivity
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.example.dimass.ui.theme.LighterGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    var itemSize by remember { mutableStateOf(0) }
    val name = remember{ mutableListOf<String>() }
    val startDate = remember{ mutableListOf<String>() }
    val endDate = remember{ mutableListOf<String>() }
//    val foodName = remember{ mutableListOf<String>() }

    val id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val context = LocalContext.current
    val dbRef = FirebaseFirestore
        .getInstance()
        .collection("scheduling")
        .whereEqualTo("uid", id)

    dbRef.get()
        .addOnSuccessListener {
            itemSize = it.size()
            for (doc in it){
                val nameData = doc.data["name"].toString()
                val startDateData = doc.data["startDate"].toString()
                val endDateData = doc.data["endDate"].toString()

//                val planningArray = doc.get("planning") as? Map<String, Any>
//                val meals = planningArray?.get("meals") as? List<Map<String, Any>>
//
//                meals?.forEach{map ->
//                    foodName.add(map["title"].toString())
//                }

                name.add(nameData)
                startDate.add(startDateData)
                endDate.add(endDateData)
            }
        }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, NewScheduleActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.offset(0.dp, -(60.dp)),
                containerColor = LighterGreen
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGreen),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Program: Diet",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if(itemSize == 0){
                        Text(
                            "There's no diet scheduling yet."
                        )
                    }else{
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
                                            .padding(20.dp),
                                        verticalArrangement = Arrangement.Top,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = name[item],
                                            fontSize = 20.sp
                                        )
                                        Text(
                                            text = "Start Date: ${startDate[item]}",
                                            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp)
                                        )

                                        Text(
                                            text = "End Date: ${endDate[item]}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    DIMASSTheme {
        HomeScreen()
    }
}