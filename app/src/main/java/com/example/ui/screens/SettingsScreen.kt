package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta
import com.example.viewmodel.VmViewModel

@Composable
fun SettingsScreen(
    viewModel: VmViewModel,
    onNavigateToWhatsNew: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToHardwareAccel: () -> Unit,
    onNavigateToFileSystem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var runInBackground by remember { mutableStateOf(true) }
    var autoKvm by remember { mutableStateOf(true) }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkVoidBg)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("settings_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Application Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Primary Navigation Options
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Column {
                    // What's New Link
                    SettingsMenuItem(
                        icon = Icons.Default.NewReleases,
                        iconTint = NeonCyan,
                        title = "What's New",
                        subtitle = "View current v1.02.00 release notes and historical updates",
                        onClick = onNavigateToWhatsNew,
                        tag = "menu_whats_new"
                    )

                    // About Screen Link
                    SettingsMenuItem(
                        icon = Icons.Default.Info,
                        iconTint = NeonEmerald,
                        title = "About Vortex VM",
                        subtitle = "FourgeAI LABS app details, version 1.02.00, and GitHub links",
                        onClick = onNavigateToAbout,
                        tag = "menu_about"
                    )

                    // Hardware Accel Link
                    SettingsMenuItem(
                        icon = Icons.Default.Speed,
                        iconTint = NeonMagenta,
                        title = "Vulkan 1.3 & 12GB RAM Engine",
                        subtitle = "Tune KVM kernel status, VirGL 3D, and 12GB RAM swap",
                        onClick = onNavigateToHardwareAccel,
                        tag = "menu_hardware_accel"
                    )

                    // File System Link
                    SettingsMenuItem(
                        icon = Icons.Default.Storage,
                        iconTint = NeonCyan,
                        title = "File System & Shared Storage",
                        subtitle = "Manage QCOW2 virtual disks and host storage bridges",
                        onClick = onNavigateToFileSystem,
                        tag = "menu_file_system"
                    )
                }
            }

            // Engine Runtime Preferences
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Virtual Machine Engine Preferences",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Background VM Execution", fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Keep guest OS active when app is minimized", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                        }
                        Switch(
                            checked = runInBackground,
                            onCheckedChange = { runInBackground = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonCyan.copy(alpha = 0.3f))
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Auto-Detect /dev/kvm", fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Automatically enable KVM on supported kernels", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                        }
                        Switch(
                            checked = autoKvm,
                            onCheckedChange = { autoKvm = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonEmerald, checkedTrackColor = NeonEmerald.copy(alpha = 0.3f))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag(tag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF9CA3AF)
        )
    }
}
