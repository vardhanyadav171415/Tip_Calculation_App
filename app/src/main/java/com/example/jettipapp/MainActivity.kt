package com.example.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.util.calculateTotalTip
import com.example.jettipapp.util.calculatetotalPerPerson
import com.example.jettipapp.widgets.RoundedIconButtton

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                myApp{
                   // TopHeader()
                    MainContent()
                }

        }
    }
}
@Composable
fun myApp(content:@Composable () -> Unit){
     JetTipAppTheme{

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

//@Preview
@Composable
fun TopHeader(totalPerPerson:Double=0.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(15.dp)
//        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))))
        .clip(CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)
        ){
        Column(modifier=Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center) {
            val total="%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.h5)
            Text(text = "$$total", style = MaterialTheme.typography.h4, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
         myApp {

         }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){

    val totalPerPersonState= remember {
        mutableStateOf<Double>(0.0)
    }
    val tipAmountState= remember {
        mutableStateOf(0.0)
    }
    val range=IntRange(start = 1 , endInclusive = 100)
    var splitByCount= remember {
        mutableStateOf(1)
    }
    Column(modifier=Modifier.padding(all=12.dp)) {
        BillForm(
            splitByCount = splitByCount,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState
        ) { billAmt ->

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByCount: MutableState<Int>,
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
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()
    val keyboardController = LocalSoftwareKeyboardController.current

    TopHeader(totalPerPerson = totalPerPersonState.value)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        InputField(
            valueState = totalBillState,
            labelId = "Enter Bill",
            enabled = true,
            isSingleLine = true,
            onAction = KeyboardActions {
                if (!validState) return@KeyboardActions
                onValChange(totalBillState.value.trim())
                keyboardController?.hide()
            }
        )

        if (validState) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
            ) {
                Text(
                    text = "Split",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier=Modifier.width(200.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundedIconButtton(
                        imageVector = Icons.Default.Remove,
                        onClick = {
                            splitByCount.value = maxOf(1, splitByCount.value - 1)
                            totalPerPersonState.value = calculatetotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitBy = splitByCount.value,
                                tipPercentage = tipPercentage
                            )
                        }
                    )
                    Text(
                        text = "${splitByCount.value}",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal =1.dp)
                    )
                    RoundedIconButtton(
                        imageVector = Icons.Default.Add,
                        onClick = {
                            splitByCount.value = minOf(range.last, splitByCount.value + 1)
                            totalPerPersonState.value = calculatetotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitBy = splitByCount.value,
                                tipPercentage = tipPercentage
                            )
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tip",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Text(
                    text = "$ ${tipAmountState.value}",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 9.dp, horizontal = 130.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = "$tipPercentage%",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Slider(
                value = sliderPositionState.value,
                onValueChange = {
                    sliderPositionState.value = it
                    tipAmountState.value = calculateTotalTip(
                        totalBill = totalBillState.value.toDouble(),
                        tipPercentage = tipPercentage
                    )
                    totalPerPersonState.value = calculatetotalPerPerson(
                        totalBillState.value.toDouble(),
                        splitBy = splitByCount.value,
                        tipPercentage = tipPercentage
                    )
                },
                modifier = Modifier.padding(vertical = 8.dp),
                steps = 5,
                onValueChangeFinished = {}
            )
        } else {
            Box() {
            }
        }
    }
}

