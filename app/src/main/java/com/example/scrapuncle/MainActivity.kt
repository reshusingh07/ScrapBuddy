package com.example.scrapuncle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.scrapuncle.auth.ui.AddAddressScreen
import com.example.scrapuncle.auth.ui.CreateProfileScreen
import com.example.scrapuncle.auth.ui.LoginScreen
import com.example.scrapuncle.auth.ui.OtpScreen
import com.example.scrapuncle.auth.viewmodel.AddAddressViewModel
import com.example.scrapuncle.auth.viewmodel.AuthViewModel
import com.example.scrapuncle.auth.viewmodel.ProfileViewModel
import com.example.scrapuncle.auth.viewmodel.ScheduleViewModel
import com.example.scrapuncle.navigation.AppNavGraph
 import com.example.scrapuncle.ui.theme.ScrapUncleTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

         setContent {
             ScrapUncleTheme {
//                 Surface(
//                     modifier = Modifier.fillMaxSize(),
//                     color = MaterialTheme.colorScheme.background
//                 ) {

//                     Log.d("CHECK", Firebase.auth.currentUser?.uid ?: "no user")
                         val navController = rememberNavController()
                         val authViewModel: AuthViewModel = hiltViewModel()

                         AppNavGraph(
                             navController = navController,
                             authViewModel = authViewModel
                         )

                    val  profileViewModel : ProfileViewModel = hiltViewModel()
//
//                     CreateProfileScreen(
//                         viewModel = profileViewModel,
//                         onCreateAccount = {
//                             navController.navigate("home")
//                         }
//                     )

                     val addressViewModel : AddAddressViewModel = hiltViewModel()
                     val scheduleViewModel: ScheduleViewModel = hiltViewModel()

//                     CreateProfileScreen1(
//                         viewModel = addressViewModel,
//                         scheduleViewModel = scheduleViewModel,
//                         onSchedulePickup = {
//                             navController.navigate("home")
//                         }
//                     )


//                     OtpScreen(
//                         authViewModel = authViewModel,
//                         onNavigateToCreateProfile = {
//                             navController.navigate("create_profile")
//                         }
//                     )

//                 }



             }
        }
    }
}

