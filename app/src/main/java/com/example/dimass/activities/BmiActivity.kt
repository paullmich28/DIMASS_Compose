package com.example.dimass.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.ui.theme.Blue
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.example.dimass.ui.theme.Mustard
import com.example.dimass.ui.theme.Red
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class BmiActivity : ComponentActivity() {
    private lateinit var dbRef: DocumentReference
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                // A surface container using the 'background' color from the theme
                BmiText()
            }
        }
    }

    @Composable
    fun BmiText(){
        val color: Color
        val category: String
        val program: String
        var bmiFloat by remember{ mutableStateOf(0f) }
        var bmi by remember{ mutableStateOf("") }

        id = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        dbRef = FirebaseFirestore
            .getInstance()
            .collection("accounts")
            .document(id)

        dbRef.get()
            .addOnSuccessListener {
                bmi = it.data?.get("bmi").toString()
                bmiFloat = bmi.toFloat()
            }

        if(bmiFloat < 18.5){
            category = "Underweight"
            color = Blue
            program = "Mass Gain"
        }else if(bmiFloat >= 18.5 && bmi.toFloat() < 25){
            category = "Normal"
            color = Green
            program = ""
        }else if(bmiFloat >= 25 && bmi.toFloat() < 30){
            category = "Overweight"
            color = Mustard
            program = "Diet"
        }else{
            category = "Obese"
            color = Red
            program = "Diet"
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.Center
        ){
            BmiCard(category, color, bmi, program)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BmiCard(category: String, color: Color, bmi: String, program: String){
        var isExpanded by remember {
            mutableStateOf(false)
        }

        var typeOfProgram by remember {
            mutableStateOf("")
        }

        var programRec by remember{
            mutableStateOf("We recommend you to do a $program. But if you have another option, you can choose below")
        }

        if(program.lowercase() == ""){
            programRec = "You can do Diet and Mass Gain. Choose one."
        }else if(program.lowercase() == "diet"){
            typeOfProgram = "Diet"
            programRec = "We recommend you to do a $typeOfProgram"
        }else{
            typeOfProgram = "Mass Gain"
            programRec = "We recommend you to do a $typeOfProgram"
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = color
                )
            ){
                Column(
                    modifier = Modifier
                        .defaultMinSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your BMI is $bmi",
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "You're $category",
                        modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 0.dp),
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                }
            }

            Text(
                text = programRec,
                modifier = Modifier
                    .padding(0.dp, 10.dp),
                color = Color.Black
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {isExpanded = it}
            ) {
                TextField(
                    value = typeOfProgram,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {isExpanded = false}
                ){
                    DropdownMenuItem(
                        text = {
                               Text("Mass Gain")
                        },
                        onClick = {
                            typeOfProgram = "Mass Gain"
                            isExpanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                               Text("Diet")
                        },
                        onClick = {
                            typeOfProgram = "Diet"
                            isExpanded = false
                        }
                    )
                }
            }

            ElevatedButton(
                onClick = {
                    dbRef.update(
                        mapOf(
                            "program" to typeOfProgram
                        )
                    )

                    val intent = Intent(this@BmiActivity, MainPageActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .padding(0.dp, 10.dp)
                    .fillMaxWidth(0.6f),

                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = BottleGreen,
                    contentColor = Color.White
                ),

            ) {
                Text(
                    text = "Next"
                )
            }
        }
    }

    @Preview
    @Composable
    fun BmiReview(){
        DIMASSTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGreen),
                contentAlignment = Alignment.Center
            ){
                BmiCard(category = "Obese", color = Red, bmi = "30", "Diet")
            }
        }
    }
}

