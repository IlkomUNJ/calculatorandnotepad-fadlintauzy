package com.example.kakka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kakka.ui.theme.KAKKATheme


object Destinations {
    const val MenuScreen = "menu_screen"
    const val CalcScreen = "calculator_screen"
    const val TeditorScreen = "text_editor_screen"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NoteManager.initialize(this)
        setContent {
            KAKKATheme {
                AppNavHost()
            }
        }
    }
}

private fun NoteManager.initialize(activity: com.example.kakka.MainActivity) {}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.MenuScreen,
        modifier = Modifier.fillMaxSize()
    ) {

        composable(Destinations.MenuScreen) {
            MenuScreen(navController = navController)
        }
        composable(Destinations.CalcScreen) {
            SetupLayout()
        }
        composable(Destinations.TeditorScreen) {
            EditScreen(0, navController)
        }
    }
}
@Composable
fun MenuScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1E1E1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate(Destinations.CalcScreen) },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(64.dp),
            ) {
                Text("Calculator Screen", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Destinations.TeditorScreen) },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(64.dp),
            ) {
                Text("Text Editor Screen", fontSize = 20.sp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    KAKKATheme {
        AppNavHost()
    }
}