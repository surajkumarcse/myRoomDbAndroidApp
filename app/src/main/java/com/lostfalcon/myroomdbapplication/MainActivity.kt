package com.lostfalcon.myroomdbapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import com.lostfalcon.myroomdbapplication.ui.theme.MyRoomDbApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels()
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.createDb(this.applicationContext)
        setContent {

            MyRoomDbApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android",
                        onAddUserClick = { insertUserOnClickListener() },
                        onDeleteUserClick = { deleteUserOnClickListener() },
                        onShowUserClick = { showAllUserOnClickListener() },
                        onUpdateUserClick = { updateFirstUserOnClickListener() }
                        )
                }
            }
        }
    }

    fun insertUserOnClickListener() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.insertUser(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
            )
        }
    }

    fun deleteUserOnClickListener() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.deleteUser()
        }
    }

    fun showAllUserOnClickListener() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, viewModel.getUsers().toString())
        }
    }

    fun updateFirstUserOnClickListener() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.updateActiveUser()
        }
    }
}

@Composable
fun Greeting(name: String,
             modifier: Modifier = Modifier,
             onAddUserClick: () -> Unit,
             onDeleteUserClick: () -> Unit,
             onShowUserClick: () -> Unit,
             onUpdateUserClick: () -> Unit
             ) {
    Column {
        Button(
            onClick = onAddUserClick,
            modifier = modifier
        ) {
            Text(text = "Add Users")
        }

        Button(
            onClick = onDeleteUserClick,
            modifier = modifier
        ) {
            Text(text = "Delete Users")
        }

        Button(
            onClick = onShowUserClick,
            modifier = modifier
        ) {
            Text(text = "Show Users")
        }

        Button(
            onClick = onUpdateUserClick,
            modifier = modifier
        ) {
            Text(text = "Update the first user")
        }
    }
}