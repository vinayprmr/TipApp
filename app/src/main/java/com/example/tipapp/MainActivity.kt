package com.example.tipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.tipapp.utils.calculateTotalPerPerson
import com.example.tipapp.utils.calculateTotalTip
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
            MainContent()
        }

    }
}


@ExperimentalComposeUiApi
@Composable
private fun MainContent() {
    val splitValueState = remember {
        mutableStateOf(1)
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    val range = IntRange(start = 1, endInclusive = 100)
    BillForm(
        range = range,
        splitValueState = splitValueState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState
    ) {}
}

@ExperimentalComposeUiApi
@Composable
private fun BillForm(
    range: IntRange = 1..100,
    splitValueState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionChange = remember {
        mutableStateOf(0f)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    TopHeader(totalPerPersonState.value)
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
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        fontSize = 18.sp
                    )
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                            splitValueState.value =
                                if (splitValueState.value > 1) splitValueState.value - 1
                                else 1

                            totalPerPersonState.value = calculateTotalPerPerson(
                                tipAmount = tipAmountState.value,
                                personsToSplit = splitValueState.value,
                                billAmount = (totalBillState.value).toInt()
                            )
                        })
                        Text(
                            text = "${splitValueState.value}",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                            splitValueState.value =
                                if (splitValueState.value < range.last) splitValueState.value + 1
                                else 100

                            totalPerPersonState.value = calculateTotalPerPerson(
                                tipAmount = tipAmountState.value,
                                personsToSplit = splitValueState.value,
                                billAmount = (totalBillState.value).toInt()
                            )
                        })
                    }

                }
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 20.dp, end = 40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(150.dp))
                    Text(
                        text = "${tipAmountState.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${((sliderPositionChange.value) * 100).toInt()}%",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(value = sliderPositionChange.value, onValueChange = { newVal ->
                        sliderPositionChange.value = newVal
                        tipAmountState.value = calculateTotalTip(
                            totalBillState.value.toDouble(),
                            ((sliderPositionChange.value) * 100).toInt()
                        )

                        totalPerPersonState.value = calculateTotalPerPerson(
                            tipAmount = tipAmountState.value,
                            personsToSplit = splitValueState.value,
                            billAmount = (totalBillState.value).toInt()
                        )
                    })
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