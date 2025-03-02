package com.example.fetchcodingexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetchcodingexercise.ui.theme.FetchCodingExerciseTheme
import com.example.fetchcodingexercise.ui.view.ExerciseHeader
import com.example.fetchcodingexercise.ui.view.SearchList
import com.example.fetchcodingexercise.viewModel.ItemViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create item view model
        val itemViewModel = ItemViewModel()

        setContent {
            FetchCodingExerciseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Column (modifier = Modifier.padding(padding).fillMaxSize()
                        .background(Color(247,247,247))
                    ) {
                        ExerciseHeader()
                        SearchList(
                            itemViewModel = itemViewModel
                        )
                    }
                }
            }
        }
    }
}