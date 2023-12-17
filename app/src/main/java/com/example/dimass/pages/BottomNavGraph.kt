package com.example.dimass.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dimass.model.BottomBarScreen
import com.example.dimass.pages.HomeScreen
import com.example.dimass.pages.ProfileScreen
import com.example.dimass.pages.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ){
        composable(route = BottomBarScreen.Home.route){
            HomeScreen()
        }

        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen()
        }

    }
}