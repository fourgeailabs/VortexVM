package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.VmEntity
import com.example.data.model.PowerState
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkSurfaceVariantGlass
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta

@Composable
fun VmCard(
    vm: VmEntity,
    onClick: () -> Unit,
    onPowerOn: () -> Unit,
    onPause: () -> Unit,
    onShutdown: () -> Unit,
    onHardReset: () -> Unit,
    onCreateSnapshot: () -> Unit,
    onOpenDisplay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRunning = vm.powerState == PowerState.RUNNING

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = if (isRunning) {
                    Brush.horizontalGradient(listOf(NeonCyan, NeonEmerald))
                } else {
                    Brush.horizontalGradient(listOf(GlassBorder, GlassBorder))
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .testTag("vm_card_${vm.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRunning) DarkSurfaceVariantGlass else DarkSurfaceGlass
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: OS Icon, Name, Power State Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (vm.osType.isWindows11) NeonCyan.copy(alpha = 0.25f) else NeonMagenta.copy(alpha = 0.25f)
                            )
                            .border(
                                width = 1.dp,
                                color = if (vm.osType.isWindows11) NeonCyan else NeonMagenta,
                                shape = CircleShape
                            )
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = vm.osType.icon,
                            contentDescription = vm.osType.displayName,
                            tint = if (vm.osType.isWindows11) NeonCyan else NeonMagenta,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = vm.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = vm.osType.displayName,
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }

                // Power State Badge
                val (badgeBg, badgeText, badgeColor) = when (vm.powerState) {
                    PowerState.RUNNING -> Triple(NeonEmerald.copy(alpha = 0.25f), "RUNNING", NeonEmerald)
                    PowerState.PAUSED -> Triple(Color(0xFFF59E0B).copy(alpha = 0.25f), "PAUSED", Color(0xFFF59E0B))
                    PowerState.SUSPENDED -> Triple(Color(0xFF8B5CF6).copy(alpha = 0.25f), "SUSPENDED", Color(0xFF8B5CF6))
                    PowerState.POWERED_OFF -> Triple(Color(0xFF374151), "OFF", Color(0xFF9CA3AF))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg)
                        .border(width = 1.dp, color = badgeColor, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Tech Specs & Hardware Accel Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SpecPill(icon = Icons.Default.Memory, text = "${vm.ramMb / 1024} GB RAM", tint = NeonEmerald)
                SpecPill(icon = Icons.Default.Bolt, text = "${vm.cpuCores} Cores", tint = NeonCyan)
                SpecPill(icon = Icons.Default.Storage, text = "${vm.diskSizeGb} GB Disk", tint = NeonMagenta)

                if (vm.virglEnabled) {
                    SpecPill(
                        icon = Icons.Default.VideogameAsset,
                        text = "Vulkan 1.3",
                        tint = NeonCyan
                    )
                }
                if (vm.tpm2Bypass) {
                    SpecPill(
                        icon = Icons.Default.Shield,
                        text = "TPM 2.0",
                        tint = NeonEmerald
                    )
                }
            }

            if (vm.notes.isNotBlank()) {
                Text(
                    text = vm.notes,
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Embedded Live Power Controls
            PowerControlsBar(
                vm = vm,
                onPowerOn = onPowerOn,
                onPause = onPause,
                onShutdown = onShutdown,
                onHardReset = onHardReset,
                onCreateSnapshot = onCreateSnapshot,
                onOpenDisplay = onOpenDisplay
            )
        }
    }
}

@Composable
private fun SpecPill(
    icon: ImageVector,
    text: String,
    tint: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF1E293B))
            .border(width = 0.5.dp, color = GlassBorder, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .height(12.dp)
                    .width(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}
