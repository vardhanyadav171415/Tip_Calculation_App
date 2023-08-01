package com.example.jettipapp.widgets

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier=Modifier.padding(5.dp)
@Composable
fun RoundedIconButtton(modifier: Modifier=Modifier,imageVector: ImageVector,onClick:()->Unit,
                 tint: Color =Color.Black.copy(alpha = 0.8f),
                backgroundColor: Color=MaterialTheme.colors.background,
                elevation: Dp =4.dp){
             Card(modifier= modifier
                 .padding(all = 0.dp)
                 .clickable { onClick.invoke() }
                 .then(IconButtonSizeModifier), shape =
             CircleShape,backgroundColor=backgroundColor,elevation=elevation) {
                 androidx.compose.material.Icon(imageVector = imageVector, contentDescription ="Plus of minus icon",tint=tint)
             }
}