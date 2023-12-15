package com.example.dimass.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.dimass.activities.ScheduleDetailActivity
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.example.dimass.ui.theme.LighterGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun HomeScreen(){
    var itemSize by remember { mutableStateOf(0) }
    val name = remember{ mutableListOf<String>() }
    val startDate = remember{ mutableListOf<String>() }
    val endDate = remember{ mutableListOf<String>() }
    var listOfScheduleId = remember{ mutableListOf<String>() }

    var userProgram by remember{ mutableStateOf("") }
//    val foodName = remember{ mutableListOf<String>() }

    val id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val context = LocalContext.current
    val dbRef = FirebaseFirestore
        .getInstance()

    var isLoading by remember { mutableStateOf(true) }

    GlobalScope.launch(Dispatchers.IO){
        try{
            val scheduling = dbRef.collection("scheduling").whereEqualTo("uid", id)
            val accounts = dbRef.collection("accounts").document(id)

            try {
                val docs = scheduling.get().await()
                itemSize = docs.size()

                docs.map {doc ->
                    val nameData = doc.getString("name")!!
                    val startDateData = doc.getString("startDate")!!
                    val endDateData = doc.getString("endDate")!!

                    name.add(nameData)
                    startDate.add(startDateData)
                    endDate.add(endDateData)
                    listOfScheduleId.add(doc.id)
                }
            } catch (e: Exception) {
                Log.e("exception", "Error", e)
            }

            userProgram = try {
                val doc = accounts.get().await()
                doc.getString("program") ?: ""
            } catch (e: Exception) {
                ""
            }

            isLoading = false
        }catch (e: Exception){
            Log.e("TAG", "Error getting documents", e)
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
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BottleGreen
                    )
                }else{
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Program: $userProgram",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if(itemSize == 0){
                            Text(
                                "There's no diet scheduling yet."
                            )
                        }else{
                            LazyColumn(
                                modifier = Modifier.padding(bottom = 30.dp)
                            ){
                                items(count = itemSize){item ->
                                    Card(
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Green
                                        ),
                                        modifier = Modifier.padding(20.dp),
                                        onClick = {
                                            val intent = Intent(context, ScheduleDetailActivity::class.java)
                                            val bundle = Bundle()

                                            bundle.putString("id", listOfScheduleId[item])

                                            intent.putExtras(bundle)

                                            context.startActivity(intent)
                                        }
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