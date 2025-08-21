package com.example.myapplication.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.domain.enums.CardBrand
import com.example.myapplication.domain.models.Card
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsCarousel(
    cards: List<Card>?,
    isLoading: Boolean
) {
    val listState = rememberLazyListState()
    val cardWidth = 240.dp
    val spacing = 8.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = (screenWidth - cardWidth) / 2

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {

            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
            val centerCardIndex = visibleItems.minByOrNull {
                abs((it.offset + it.size / 2) - viewportCenter)
            }?.index ?: 0

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val totalCards = cards?.size?:0
                if (totalCards > 1) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                    ) {
                        repeat(totalCards) { index ->
                            val isSelected = centerCardIndex == index
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 12.dp else 8.dp)
                                    .clip(RoundedCornerShape(percent = 50))
                                    .background(
                                        if (isSelected) colorResource(R.color.blue)
                                        else Color.Gray.copy(alpha = 0.5f)
                                    )
                            )
                        }
                    }
                } else {
                    Text(text = stringResource(R.string.no_cards), style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }

                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    flingBehavior = rememberSnapFlingBehavior(listState)
                ) {
                    itemsIndexed(cards?: emptyList()) { index, card ->
                        val itemInfo = visibleItems.find { it.index == index }

                        val brandResource = when (card.brand) {
                            CardBrand.VISA -> R.drawable.visa_logo
                            CardBrand.MASTERCARD -> R.drawable.mastercard_logo
                        }

                        val scale = itemInfo?.let {
                            val itemCenter = it.offset + it.size / 2
                            val distanceToCenter = (itemCenter - viewportCenter).toFloat()
                            val normalized = (abs(distanceToCenter) / viewportCenter).coerceIn(0f, 1f)
                            1f - 0.2f * normalized
                        } ?: 0.8f

                        val alpha = itemInfo?.let {
                            val itemCenter = it.offset + it.size / 2
                            val distanceToCenter = (itemCenter - viewportCenter).toFloat()
                            val normalized = (abs(distanceToCenter) / viewportCenter).coerceIn(0f, 1f)
                            1f - 0.5f * normalized
                        } ?: 0.5f

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        this.alpha = alpha
                                    }
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF1A1A1A))
                                    .size(width = cardWidth, height = 150.dp)
                            ) {
                                Text(
                                    text = card.pan,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                )
                                Image(
                                    painter = painterResource(brandResource),
                                    contentDescription = "Card logo",
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(16.dp)
                                        .size(40.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 10.dp)
                            ) {
                                if (card.isFavorite) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Star",
                                        tint = colorResource(R.color.grey_text),
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.favorite_card),
                                        style = TextStyle(
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = colorResource(R.color.grey_text)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
