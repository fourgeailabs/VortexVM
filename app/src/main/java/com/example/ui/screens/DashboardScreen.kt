package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PowerState
import com.example.ui.components.ResourceUsageGauges
import com.example.ui.components.VmCard
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta
import com.example.viewmodel.VmViewModel

@Composable
fun DashboardScreen(
    viewModel: VmViewModel,
    onCreateVmClick: () -> Unit,
    onOpenDisplayClick: (Long) -> Unit,
    onHardwareAccelClick: () -> Unit,
    onFileSystemClick: () -> Unit,
    onOpenVmDetailClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val vmList by viewModel.vms.collectAsState()
    val telemetry by viewModel.telemetry.collectAsState()
    val activeRunningVm = vmList.find { it.powerState == PowerState.RUNNING }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoidBg)
            .padding(16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Real-Time Hardware Telemetry Header Gauge
        item {
            ResourceUsageGauges(telemetry = telemetry)
        }

        // Active Guest Monitor Banner
        if (activeRunningVm != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, brush = Brush.horizontalGradient(listOf(NeonCyan, NeonEmerald)), shape = RoundedCornerShape(16.dp))
                        .testTag("active_vm_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "GUEST ACTIVE: ${activeRunningVm.name}",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Vulkan 1.3 Passthrough Active • ${telemetry.fps} FPS • ${activeRunningVm.ramMb / 1024}GB RAM",
                                color = NeonEmerald,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { onOpenDisplayClick(activeRunningVm.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Monitor,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text("Launch Display", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Actions Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Hyper-Drive Quick Actions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCreateVmClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DesktopWindows,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("+ 12GB Windows VM", maxLines = 1, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onHardwareAccelClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = NeonMagenta,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Vulkan Engine", maxLines = 1, color = Color.White)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onFileSystemClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("QCOW2 Disks & Storage", maxLines = 1, color = Color.White)
                    }
                }
            }
        }

        // VM Profiles Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Virtual Machines (${vmList.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                OutlinedButton(
                    onClick = onCreateVmClick,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Profile",
                        tint = NeonCyan
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Profile", color = NeonCyan)
                }
            }
        }

        // VM Profile Cards
        items(
            items = vmList,
            key = { it.id }
        ) { vm ->
            VmCard(
                vm = vm,
                onClick = { onOpenVmDetailClick(vm.id) },
                onPowerOn = { viewModel.powerOnVm(vm.id) },
                onPause = { viewModel.pauseVm(vm.id) },
                onShutdown = { viewModel.shutdownVm(vm.id) },
                onHardReset = { viewModel.hardResetVm(vm.id) },
                onCreateSnapshot = {
                    viewModel.createSnapshot(
                        vmId = vm.id,
                        title = "Quick Snapshot",
                        description = "Saved from Dashboard"
                    )
                },
                onOpenDisplay = { onOpenDisplayClick(vm.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
