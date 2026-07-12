package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.ChatRoomScreen
import com.example.ui.screens.JoinRoomScreen
import com.example.ui.theme.AnonChatTheme
import com.example.viewmodel.ChatViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AnonChatTheme {
        val navController = rememberNavController()
        val viewModel: ChatViewModel = viewModel()

        NavHost(navController = navController, startDestination = "join") {
          composable("join") {
            JoinRoomScreen(
              onJoinRoom = { code ->
                viewModel.joinRoom(code)
                navController.navigate("chat")
              }
            )
          }
          composable("chat") {
            val state by viewModel.state.collectAsState()
            ChatRoomScreen(
              state = state,
              onSendMessage = { viewModel.sendMessage(it) },
              onBack = { navController.popBackStack() }
            )
          }
        }
      }
    }
  }
}
