package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TabType
import com.example.ui.theme.IrisButtonRainbowBrush
import com.example.ui.theme.IrisSurface
import com.example.ui.theme.RainbowCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun IrispopBottomBar(
    activeTab: TabType,
    onTabSelected: (TabType) -> Unit,
    onPlusClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Subtle breathing pulse glow for center Plus button
    val infiniteTransition = rememberInfiniteTransition(label = "plusGlow")
    val glowElevation by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "elevation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(IrisSurface.copy(alpha = 0.98f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Feed / Home
            NavItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Feed",
                        tint = if (activeTab == TabType.FEED) RainbowCyan else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                isSelected = activeTab == TabType.FEED,
                onClick = { onTabSelected(TabType.FEED) },
                testTag = "nav_home"
            )

            // 2. Fist ✊ Reels
            NavItem(
                icon = {
                    Text(
                        text = "✊",
                        fontSize = 22.sp,
                        color = if (activeTab == TabType.REELS) Color.White else TextMuted
                    )
                },
                isSelected = activeTab == TabType.REELS,
                onClick = { onTabSelected(TabType.REELS) },
                testTag = "nav_reels"
            )

            // 3. Center Glowing Pastel Rainbow Plus (+) Action Button
            Box(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .size(52.dp)
                    .shadow(
                        elevation = glowElevation.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = Color(0xFFFBC2EB),
                        spotColor = Color(0xFFA6C1EE)
                    )
                    .clip(RoundedCornerShape(18.dp))
                    .background(IrisButtonRainbowBrush)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onPlusClicked
                    )
                    .testTag("nav_creator_plus"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Content",
                    tint = Color(0xFF0F121B),
                    modifier = Modifier.size(30.dp)
                )
            }

            // 4. Search 🔍
            NavItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Explore and Search",
                        tint = if (activeTab == TabType.SEARCH) RainbowCyan else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                isSelected = activeTab == TabType.SEARCH,
                onClick = { onTabSelected(TabType.SEARCH) },
                testTag = "nav_search"
            )

            // 5. Profile
            NavItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = if (activeTab == TabType.PROFILE) RainbowCyan else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                isSelected = activeTab == TabType.PROFILE,
                onClick = { onTabSelected(TabType.PROFILE) },
                testTag = "nav_profile"
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: @Composable () -> Unit,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(56.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        icon()
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(4.dp)
                .clip(CircleShape)
                .background(if (isSelected) RainbowCyan else Color.Transparent)
        )
    }
}
