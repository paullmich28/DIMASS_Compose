package com.example.dimass.pages

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.R
import com.example.dimass.activities.LoginActivity
import com.example.dimass.activities.MainPageActivity
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(){
    val context = LocalContext.current

//    val id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//
//    var dbRef = FirebaseFirestore
//        .getInstance()
//        .collection("accounts")
//        .document(id)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        contentAlignment = Alignment.TopCenter
    ){
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
                text = "Nama"
            )

            ElevatedButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(context, "Signed Out from your account", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(0.dp, 10.dp)
                    .fillMaxWidth(0.6f),

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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    DIMASSTheme {
        ProfileScreen()
    }
}