package com.aura.voicechat.ui.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Event Banner Carousel
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Features:
 * - Auto-scrolling banners
 * - Manual swipe support
 * - Indicator dots
 * - Click to event detail
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventBannerCarousel(
    banners: List<EventBanner>,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollDelayMillis: Long = 3000
) {
    if (banners.isEmpty()) return
    
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val scope = rememberCoroutineScope()
    
    // Auto-scroll effect
    LaunchedEffect(pagerState.currentPage) {
        delay(autoScrollDelayMillis)
        val nextPage = (pagerState.currentPage + 1) % banners.size
        scope.launch {
            pagerState.animateScrollToPage(nextPage)
        }
    }
    
    Box(modifier = modifier) {
        // Banner Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val banner = banners[page]
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable {
                        banner.eventId?.let { onBannerClick(it) }
                    },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Banner Image
                    AsyncImage(
                        model = banner.imageUrl,
                        contentDescription = banner.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Gradient overlay for better text visibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                    
                    // Banner Title
                    if (banner.title.isNotEmpty()) {
                        Text(
                            text = banner.title,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        // Page Indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { index ->
                val isSelected = pagerState.currentPage == index
                
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 8.dp),
                    shape = CircleShape,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.White.copy(alpha = 0.5f)
                    }
                ) {}
            }
        }
    }
}

/**
 * Compact version for smaller spaces
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompactEventBannerCarousel(
    banners: List<EventBanner>,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return
    
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(pagerState.currentPage) {
        delay(3000)
        val nextPage = (pagerState.currentPage + 1) % banners.size
        scope.launch {
            pagerState.animateScrollToPage(nextPage)
        }
    }
    
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        val banner = banners[page]
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable {
                    banner.eventId?.let { onBannerClick(it) }
                },
            shape = RoundedCornerShape(8.dp)
        ) {
            AsyncImage(
                model = banner.imageUrl,
                contentDescription = banner.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// Data class
data class EventBanner(
    val id: String,
    val imageUrl: String,
    val title: String,
    val eventId: String?,
    val link: String?,
    val order: Int
)
