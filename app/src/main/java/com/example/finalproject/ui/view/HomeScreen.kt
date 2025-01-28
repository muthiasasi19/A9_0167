package com.example.finalproject.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalproject.R
import com.example.finalproject.ui.navigation.DestinasiHomeFilm
import com.example.finalproject.ui.navigation.DestinasiHomePenayangan
import com.example.finalproject.ui.navigation.DestinasiHomeStudio
import com.example.finalproject.ui.navigation.DestinasiHomeTiket

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image (Blurred)
        Image(
            painter = painterResource(R.drawable.background1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 5.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = Color(0xFF23395D),
                        shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MyDocs",
                    fontSize = 28.sp,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(
                        color = Color(0xFF23395D),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Button Items
                    item {
                        CustomButton(
                            text = "Penayangan",
                            icon = Icons.Default.Menu,
                            onClick = { navController.navigate(DestinasiHomePenayangan.route) }
                        )
                    }
                    item {
                        CustomButton(
                            text = "Tiket Saya",
                            icon = Icons.Default.Face,
                            onClick = { navController.navigate(DestinasiHomeTiket.route) }
                        )
                    }
                    item {
                        CustomButton(
                            text = "Film",
                            icon = Icons.Default.Menu,
                            onClick = { navController.navigate(DestinasiHomeFilm.route) }
                        )
                    }
                    item {
                        CustomButton(
                            text = "Studio",
                            icon = Icons.Default.Face,
                            onClick = { navController.navigate(DestinasiHomeStudio.route) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun CustomButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f), // Ensures square buttons
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.custom_yellow)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text, color = Color.White)
        }
    }
}
