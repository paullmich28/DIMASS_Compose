package com.example.dimass.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.dimass.api.daily.DailyModel
import com.example.dimass.api.service.ApiServiceDaily
import com.example.dimass.api.service.ApiServiceWeekly
import com.example.dimass.api.weekly.WeeklyModel
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewScheduleActivity : ComponentActivity() {
    private val apiKey = "04c0654db4c748b28d2a2ddffe4a2cd5"
    private lateinit var dbRef: FirebaseFirestore
    private lateinit var dbAuth: FirebaseAuth

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val apiServiceWeekly by lazy {
        retrofit.create(ApiServiceWeekly::class.java)
    }

    private val apiServiceDaily by lazy{
        retrofit.create(ApiServiceDaily::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                NewSchedulePage()
            }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewSchedulePage(){
        var scheduleName by remember {
            mutableStateOf("")
        }

        var isExpanded by remember {
            mutableStateOf(false)
        }

        val context = LocalContext.current
        val date = LocalDate.now()
        val foodPrograms =
            listOf(
                "Anything",
                "Gluten Free",
                "Ketogenic",
                "Vegetarian",
                "Lacto-Vegetarian",
                "Ovo-Vegetarian",
                "Vegan",
                "Pescetarian",
                "Paleo",
                "Primal"
            )

        var typeOfFood by remember { mutableStateOf(foodPrograms[0]) }

        var dateInADay = date.plusDays(1)
        val dateInAWeek = date.plusDays(7)

        var startDate by remember{ mutableStateOf("") }
        var endDate by remember{ mutableStateOf("") }

        val listProgram = listOf("Daily", "Weekly")
        var selectedOption by remember { mutableStateOf(0) }

        dbAuth = FirebaseAuth.getInstance()
        val id = dbAuth.currentUser?.uid ?: ""

        dbRef = FirebaseFirestore.getInstance()

        val getAcc = dbRef.collection("accounts").document(id)

        var dietOrMassgain by remember{ mutableStateOf("") }

        getAcc.get()
            .addOnSuccessListener {doc ->
                dietOrMassgain = doc.getString("program")!!
            }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ){
                Text(
                    text = "Add Schedule",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(0.dp, 20.dp),
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = scheduleName,
                    onValueChange = {scheduleName = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    label = { Text("Schedule Name") }
                )

                Text(
                    text = "Choose your scheduling type",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 15.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(horizontal = 16.dp),
                ){
                    listProgram.forEachIndexed {i, program ->
                        OutlinedButton(
                            onClick = {
                                selectedOption = i
                                      },
                            modifier = when(i){
                                0 ->
                                    Modifier
                                        .offset(0.dp, 0.dp)
                                        .fillMaxWidth(0.5f)
                                        .zIndex(if (selectedOption == i) 1f else 0f)
                                else ->
                                    Modifier
                                        .offset((-1 * i).dp, 0.dp)
                                        .fillMaxWidth(1f)
                                        .zIndex(if (selectedOption == i) 1f else 0f)
                            },
                            shape = when(i){
                                0 -> RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 0.dp
                                )
                                listProgram.size - 1 -> RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 16.dp
                                )
                                else -> RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            },
                            border = BorderStroke(
                                1.dp, if(selectedOption == i){
                                    Green
                                }else{
                                    Green.copy(alpha = 0.75f)
                                }
                            ),
                            colors = if(selectedOption == i){
                                ButtonDefaults.outlinedButtonColors(
                                    containerColor = Green.copy(alpha = 0.1f),
                                    contentColor = Green
                                )
                            }else{
                                ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = Green
                                )
                            }
                        ) {
                            Text(
                                text = program
                            )
                        }
                    }
                }

                Text(
                    text = "Choose your food type",
                    modifier = Modifier.padding(top = 20.dp, bottom = 5.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {isExpanded = it}
                ) {
                    OutlinedTextField(
                        value = typeOfFood,
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
                        onDismissRequest = {isExpanded = false}
                    ){
                        foodPrograms.forEach {type ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        type
                                    )
                                },
                                onClick = {
                                    typeOfFood = type
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }

                ElevatedButton(
                    onClick = {
                        if(scheduleName.isEmpty()){
                            Toast.makeText(context, "Please fill the schedule name", Toast.LENGTH_SHORT).show()
                        }else{
                            if(listProgram[selectedOption] == "Daily"){
                                val call = if(typeOfFood == "Anything"){
                                    apiServiceDaily.getDailyData(apiKey, "day", null, null)
                                }else{
                                    apiServiceDaily.getDailyData(apiKey, "day", null, typeOfFood.lowercase())
                                }

                                call.enqueue(object: Callback<DailyModel>{
                                    override fun onResponse(call: Call<DailyModel>, response: Response<DailyModel>) {
                                        if(response.isSuccessful){
                                            var docId: String
                                            val dataModel = response.body()
                                            startDate = dateInADay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            endDate = dateInADay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                                            val hashMap = hashMapOf(
                                                "uid" to id,
                                                "name" to scheduleName,
                                                "startDate" to startDate,
                                                "endDate" to endDate,
                                                "planning" to dataModel,
                                                "program" to listProgram[selectedOption]
                                            )

                                            dbRef.collection("scheduling")
                                                .add(hashMap)
                                                .addOnSuccessListener {doc ->
                                                    docId = doc.id
                                                    Toast.makeText(context, "Planning Stored Successfully", Toast.LENGTH_LONG).show()

                                                    val intent = Intent(this@NewScheduleActivity, ScheduleDetailActivity::class.java)
                                                    val bundle = Bundle()

                                                    bundle.putString("documentId", docId)
                                                    intent.putExtras(bundle)

                                                    startActivity(intent)
                                                    finish()
                                                }.addOnFailureListener{
                                                    Toast.makeText(context, "Planning not stored", Toast.LENGTH_LONG).show()
                                                }
                                        }
                                    }

                                    override fun onFailure(call: Call<DailyModel>, t: Throwable) {

                                    }
                                })
                            }else{
                                val call = if(typeOfFood == "Anything"){
                                    apiServiceWeekly.getWeeklyData(apiKey, "day", null, null)
                                }else{
                                    apiServiceWeekly.getWeeklyData(apiKey, "day", null, typeOfFood.lowercase())
                                }
                                call.enqueue(object: Callback<WeeklyModel>{
                                    override fun onResponse(call: Call<WeeklyModel>, response: Response<WeeklyModel>) {
                                        if(response.isSuccessful){
                                            var docId: String
                                            val dataModel = response.body()?.week
                                            startDate = dateInADay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            endDate = dateInAWeek.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                                            val hashMap = hashMapOf(
                                                "uid" to id,
                                                "name" to scheduleName,
                                                "startDate" to startDate,
                                                "endDate" to endDate,
                                                "planning" to dataModel,
                                                "program" to listProgram[selectedOption]
                                            )

                                            dbRef.collection("scheduling")
                                                .add(hashMap)
                                                .addOnSuccessListener {doc ->
                                                    docId = doc.id
                                                    Toast.makeText(context, "Planning Stored Successfully", Toast.LENGTH_LONG).show()

                                                    val intent = Intent(this@NewScheduleActivity, ScheduleDetailActivity::class.java)
                                                    val bundle = Bundle()

                                                    bundle.putString("id", docId)
                                                    intent.putExtras(bundle)

                                                    startActivity(intent)
                                                    finish()
                                                }.addOnFailureListener{
                                                    Toast.makeText(context, "Planning not stored", Toast.LENGTH_LONG).show()
                                                }
                                        }
                                    }

                                    override fun onFailure(call: Call<WeeklyModel>, t: Throwable) {

                                    }
                                })
                            }
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
                        .padding(0.dp, 25.dp)
                        .fillMaxWidth(0.6f)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    @Preview
    fun NewSchedulePreview(){
        DIMASSTheme {
            NewSchedulePage()
        }
    }
}