package com.example.tipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipapp.components.InputTextField
import com.example.tipapp.ui.theme.TipAppTheme
import com.example.tipapp.widgets.RoundIconButton

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipAppTheme {
                MainPage()
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun MainPage() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            TopHeader()
            MainContent()
        }

    }
}


@ExperimentalComposeUiApi
@Composable
private fun MainContent() {

    BillForm {

    }
}

@ExperimentalComposeUiApi
@Composable
private fun BillForm(onValChange: (String) -> Unit = {}) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp), elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            InputTextField(modifier = Modifier.fillMaxWidth(),
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                })
            if (validState) {
                Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = { /*TODO*/ })

                        RoundIconButton(imageVector = Icons.Default.Add, onClick = { /*TODO*/ })
                    }

                }
            } else {
                Box {}
            }
        }
    }
}

@Composable
private fun TopHeader(totalPerPerson: Double = 0.0) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.Cyan
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total Per Person", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            val total = "%.2f".format(totalPerPerson)
            Text(text = "$$total", fontWeight = FontWeight.Bold, fontSize = 26.sp)
        }
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipAppTheme {
        MainPage()
    }
}