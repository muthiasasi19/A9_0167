package com.example.finalproject.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@Composable
fun HomeScreen(
    navController: NavController, // NavController untuk navigasi
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        Image(
            painter = painterResource(R.drawable.background1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 5.dp),
            contentScale = ContentScale.Crop
        )

        // Bagian Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    color = Color(0xAA90CAF9),
                    shape = RoundedCornerShape(bottomEnd = 50.dp)
                )
                .padding(start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Cinemaku",
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.width(30.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Button Film
                Button(
                    onClick = { navController.navigate("home_film") }, // Navigasi ke HomeFilm
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 140.dp, height = 80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.teal_200))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Text(text = "Film")
                    }
                }

                // Button Studio
                Button(
                    onClick = { navController.navigate("home_studio") }, // Navigasi ke HomeStudio
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 140.dp, height = 80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.teal_200))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Text(text = "Studio")
                    }
                }
            }


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Button Penayangan
                Button(
                    onClick = {
                        Log.d("HomeScreen", "Tombol Penayangan diklik, menuju ke home_penayangan")
                        try {
                            navController.navigate("home_penayangan")
                            Log.d("HomeScreen", "Navigasi ke home_penayangan berhasil")
                        } catch (e: Exception) {
                            Log.e("HomeScreen", "Gagal navigasi ke home_penayangan: ${e.message}", e)
                        }
                    }, // Navigasi ke HomePenayangan
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 140.dp, height = 80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.teal_200))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.custom_movie_icon),
                            contentDescription = "Penayangan",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Text(text = "Penayangan")
                    }
                }

                // Button Tiket
                Button(
                    onClick = {
                        Log.d("HomeScreen", "Tombol Tiket diklik, menuju ke home_tiket")
                        navController.navigate("home_tiket")
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 140.dp, height = 80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.teal_200))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.custom_movie_icon),
                            contentDescription = "Tiket",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Text(text = "Tiket")
                    }
                }
            }
        }
    }
}