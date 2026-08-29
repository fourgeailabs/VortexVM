package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.AppUpdateLogEntity
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import org.json.JSONArray

@Composable
fun AccordionItem(
    updateLog: AppUpdateLogEntity,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "rotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (updateLog.isCurrentVersion) NeonCyan else GlassBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .testTag("whats_new_accordion_${updateLog.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
    ) {
        Column {
            // Header Row (Clickable toggle)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (updateLog.isCurrentVersion) {
                        Icon(
                            imageVector = Icons.Default.NewReleases,
                            contentDescription = "Current Version",
                            tint = NeonCyan,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    Column {
                        Text(
                            text = updateLog.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (updateLog.isCurrentVersion) NeonCyan.copy(alpha = 0.25f) else Color(0xFF1E293B)
                                    )
                                    .border(
                                        width = 0.5.dp,
                                        color = if (updateLog.isCurrentVersion) NeonCyan else GlassBorder,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "v${updateLog.versionString}",
                                    color = if (updateLog.isCurrentVersion) NeonCyan else Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = updateLog.releaseDate,
                                fontSize = 11.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Toggle Release Notes",
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.rotate(rotationState)
                )
            }

            // Expandable Content Body
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val changes = parseJsonChanges(updateLog.changesJson)
                    changes.forEach { change ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "• ",
                                fontWeight = FontWeight.Bold,
                                color = NeonEmerald
                            )
                            Text(
                                text = change,
                                fontSize = 12.sp,
                                color = Color(0xFFD1D5DB)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun parseJsonChanges(json: String): List<String> {
    return try {
        val array = JSONArray(json)
        val list = mutableListOf<String>()
        for (i in 0 until array.length()) {
            list.add(array.getString(i))
        }
        list
    } catch (e: Exception) {
        listOf(json)
    }
}
