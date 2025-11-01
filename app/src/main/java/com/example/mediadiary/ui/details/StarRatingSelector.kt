package com.example.mediadiary.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingSelector(
    rating: Int?,
    onRatingChanged: (Int) -> Unit
){
    var selectedRating by remember { mutableIntStateOf(rating ?: 0) }
    val interactionSource = remember { MutableInteractionSource() }
    Row(
    ){
        for(i in 1..10){
            Icon(
                imageVector = if(i<=selectedRating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = selectedRating.toString(),
                tint = if(i<=selectedRating) Color.Yellow else Color.DarkGray,
                modifier = Modifier.size(30.dp)
                    .clickable(
                        onClick = {
                        selectedRating = i
                        onRatingChanged(selectedRating)
                        },
                        indication = null,
                        interactionSource = interactionSource
                    )
            )
        }
    }
}