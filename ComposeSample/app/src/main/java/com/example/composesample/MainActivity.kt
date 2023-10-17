package com.example.composesample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composesample.ui.theme.ComposeSampleTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var result by remember {
                mutableStateOf(0.0f)
            }

            val goMain = {
                navController.navigate("Main")
            }

            ComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "Main",
                    ) {
                        composable("Main") {
                            TestAppMain(navController)
                        }

                        composable("Flash") {
                            flashMain(goMain)
                        }

                        composable("BMI") {
                            BMIMain(goMain) {
                                result = it
                                navController.navigate("result")
                            }
                        }
                        composable("result") {
                            ResultMain(result) {
                                navController.navigate("BMI")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun StopWatch(goMain: () -> Unit) {
        var isRunning by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Text(text = "StopWatch")
            }
            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(onClick = goMain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)) {
                    Text(text = "첫 화면으로")
                }
            }

        }
    }

    @Composable
    fun TimeDisplay() {
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp).fillMaxWidth().background(Color.Green) ) {
            Row(modifier = Modifier
                .padding(20.dp),
                horizontalArrangement = Arrangement.Center) {
                Text(text = "mm")
                Text(text = ":")
                Text(text = "ss")
            }
        }

    }

    @Composable
    fun TestAppMain(navController: NavHostController) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        navController.navigate("BMI")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "BMICalculator")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        navController.navigate("Flash")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Flash Light")
                }
            }
        }
    }

    @Composable
    fun flashMain(goMain: () -> Unit) {
        var isOn by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Text(text = "Flash Light")
            }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Switch(checked = isOn, onCheckedChange = {
                    isOn = !isOn
                    startService(Intent(applicationContext, TorchService::class.java).apply{
                        action = if (isOn) {
                            "on"
                        } else {
                            "off"
                        }
                    })
                })
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(onClick = goMain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)) {
                    Text(text = "첫 화면으로")
                }
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BMIMain(goMain: () -> Unit, onClick: ((result: Float) -> Unit)) {
        val (height, setHeight) = remember { mutableStateOf(TextFieldValue()) }
        val (weight, setWeight) = remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                label = { Text(text = "키") },
                value = height,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = setHeight
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "몸무게") },
                value = weight,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = setWeight
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        if (weight.text == ""|| height.text == "") {

                        } else {
                            val result =
                                weight.text.toFloat() / (height.text.toFloat() / 100.0f).pow(2.0f)
                            onClick(result)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "계산하기")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(onClick = goMain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)) {
                    Text(text = "첫 화면으로")
                }
            }
        }
    }

    @Composable
    fun ResultMain(result: Float, onClick: () -> Unit) {
        val resultText = when {
            result >= 35 -> "고도 비만"
            result >= 30 -> "2단계 비만"
            result >= 25 -> "1단계 비만"
            result >= 23 -> "과체중"
            result >= 18.5 -> "정상"
            else -> "저체중"
        }

        val resultIcon = when {
            result >= 23 -> R.drawable.ccc
            result >= 18.5 -> R.drawable.bbb
            else -> R.drawable.aaa
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = resultText)
            Image(painter = painterResource(id = resultIcon), contentDescription = "icon")
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "다시 계산하기")
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ComposeSampleTheme {
//        Greeting("Android")
            TimeDisplay()
        }
    }
}