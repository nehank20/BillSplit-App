package com.example.bill_split_app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TipCalculator() {


    var amount by remember {
        mutableStateOf("")
    }

    var personCounter by remember {
        mutableStateOf(1)
    }

    var tipPercentage by remember {
        mutableStateOf(0F)
    }


    Column {
        Header(
            amount = formatTwoDecimalPoints(
                getTotalHeaderAmount(
                    amount,
                    personCounter = personCounter,
                    tipPercentage = tipPercentage
                )
            )
        )
        AddSpace(height = 16)
        UserInput(
            amountValue = amount,
            amountValueChange = {
                amount = it
            },
            personCounter = personCounter,
            onAddOrRemovePerson = {
                if (it < 0) {
                    if (personCounter != 1) {
                        personCounter--
                    }
                } else {
                    personCounter++
                }
            },
            tipPercentage = tipPercentage,
            onTipPercentageChange = {
                tipPercentage = it
            })
    }

}

@Composable
fun Header(amount: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Total per person", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            AddSpace(height = 8)
            Text(
                "Rs. $amount", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun UserInput(
    amountValue: String,
    amountValueChange: (String) -> Unit,
    personCounter: Int,
    onAddOrRemovePerson: (Int) -> Unit,
    tipPercentage: Float,
    onTipPercentageChange: (Float) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amountValue,
                placeholder = {
                    Text(text = "Enter your amount")
                },
                onValueChange = {
                    amountValueChange(it)
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )

            AddSpace(height = 16)

            if (amountValue.isNotBlank()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Split")
                    Spacer(modifier = Modifier.fillMaxWidth(0.60F))
                    CustomArrowButton(imageVector = Icons.Default.KeyboardArrowDown) {
                        onAddOrRemovePerson(-1)
                    }

                    Text(text = "$personCounter", Modifier.padding(horizontal = 8.dp))

                    CustomArrowButton(imageVector = Icons.Default.KeyboardArrowUp) {
                        onAddOrRemovePerson(1)
                    }
                }

                AddSpace(height = 16)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Tip")
                    Text(
                        text = "Rs ${
                            formatTwoDecimalPoints(
                                getTipAmount(
                                    amountValue,
                                    tipPercentage
                                )
                            )
                        }",
                    )
                }
                AddSpace(height = 16)

                Text(text = "${formatTwoDecimalPoints(tipPercentage.toString())} %",)

                AddSpace(height = 8)

                Slider(
                    value = tipPercentage,
                    onValueChange = {
                        onTipPercentageChange(it)
                    },
                    steps = 10,
                    valueRange = 0F..100F,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }


        }
    }
}

@Composable
fun CustomArrowButton(imageVector: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onClick()
            },
        shape = CircleShape,
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
    }
}

@Composable
fun AddSpace(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}