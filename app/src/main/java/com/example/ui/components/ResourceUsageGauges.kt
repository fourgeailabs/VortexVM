package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SystemTelemetry
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta

@Composable
fun ResourceUsageGauges(
    telemetry: SystemTelemetry,
    modifier: Modifier = Modifier
) {
    val animatedCpu by animateFloatAsState(targetValue = telemetry.cpuUsagePercent / 100f, label = "cpu")
    val ramPercent = if (telemetry.ramTotalMb > 0) telemetry.ramUsedMb.toFloat() / telemetry.ramTotalMb else 0f
    val animatedRam by animateFloatAsState(targetValue = ramPercent, label = "ram")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(NeonCyan.copy(alpha = 0.5f), NeonMagenta.copy(alpha = 0.3f), GlassBorder)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("resource_telemetry_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "System Telemetry",
                        tint = NeonCyan,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Real-Time Telemetry Gauge",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (telemetry.isKvmActive) NeonEmerald.copy(alpha = 0.25f) else Color(0xFFD97706).copy(alpha = 0.25f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (telemetry.isKvmActive) NeonEmerald else Color(0xFFD97706),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = if (telemetry.isKvmActive) NeonEmerald else Color(0xFFD97706),
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .height(14.dp)
                        )
                        Text(
                            text = if (telemetry.isKvmActive) "KVM ACTIVE" else "EMULATED",
                            color = if (telemetry.isKvmActive) NeonEmerald else Color(0xFFD97706),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // CPU & RAM Gauge Bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CPU
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CPU Load",
                            fontSize = 11.sp,
                            color = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = "${telemetry.cpuUsagePercent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { animatedCpu },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (telemetry.cpuUsagePercent > 80) NeonMagenta else NeonCyan,
                        trackColor = Color(0xFF1E293B)
                    )
                }

                // RAM (Up to 12 GB)
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "RAM (${telemetry.ramTotalMb / 1024} GB Max)",
                            fontSize = 11.sp,
                            color = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = "${telemetry.ramUsedMb} MB",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonEmerald
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { animatedRam },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonEmerald,
                        trackColor = Color(0xFF1E293B)
                    )
                }
            }

            // Quick Telemetry Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TelemetryBadge(
                    icon = Icons.Default.Memory,
                    label = "VRAM",
                    value = "${telemetry.vramUsedMb} MB",
                    tint = NeonCyan
                )
                TelemetryBadge(
                    icon = Icons.Default.Storage,
                    label = "Disk I/O",
                    value = "${telemetry.diskReadKbps / 1024} MB/s",
                    tint = NeonMagenta
                )
                TelemetryBadge(
                    icon = Icons.Default.Speed,
                    label = "FPS",
                    value = if (telemetry.fps > 0) "${telemetry.fps} FPS" else "60 FPS",
                    tint = NeonEmerald
                )
                TelemetryBadge(
                    icon = Icons.Default.Thermostat,
                    label = "Temp",
                    value = "%.1f°C".format(telemetry.temperatureCelsius),
                    tint = Color(0xFFF59E0B)
                )
            }
        }
    }
}

@Composable
private fun TelemetryBadge(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .height(18.dp)
                .width(18.dp),
            tint = tint
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color(0xFF9CA3AF)
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
