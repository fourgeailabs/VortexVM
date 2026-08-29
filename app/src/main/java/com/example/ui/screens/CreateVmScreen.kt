package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OsType
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkSurfaceVariantGlass
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta
import com.example.viewmodel.VmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVmScreen(
    viewModel: VmViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOsType by remember { mutableStateOf(OsType.WINDOWS_11) }
    var vmName by remember { mutableStateOf("Windows 11 Pro 12GB Gaming Workstation") }
    var ramMbSlider by remember { mutableFloatStateOf(12288f) }
    var cpuCoresSlider by remember { mutableFloatStateOf(6f) }
    var diskSizeGbSlider by remember { mutableFloatStateOf(64f) }
    var kvmEnabled by remember { mutableStateOf(true) }
    var virglEnabled by remember { mutableStateOf(true) }
    var tpm2Bypass by remember { mutableStateOf(true) }
    var notesText by remember { mutableStateOf("Configured with maximum 12GB RAM, Mesa Turnip Vulkan 1.3 passthrough, and DXVK 2.3 DirectX translation.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Virtual Machine Profile", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkVoidBg)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkVoidBg)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("create_vm_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // One-Click 12GB Windows 11 Ultra Preset Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, brush = Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta)), shape = RoundedCornerShape(16.dp))
                    .testTag("win11_preset_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Windows 11 Ultra 12GB Gaming Preset",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Applies maximum performance profile: 12GB RAM allotment, 6 CPU Cores, Mesa Turnip Vulkan 1.3 passthrough, VirtIO high-speed network/disk, and automatic TPM 2.0 bypass.",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )

                    Button(
                        onClick = {
                            selectedOsType = OsType.WINDOWS_11
                            vmName = "Windows 11 Pro 12GB Vulkan Gaming"
                            ramMbSlider = 12288f
                            cpuCoresSlider = 6f
                            diskSizeGbSlider = 64f
                            kvmEnabled = true
                            virglEnabled = true
                            tpm2Bypass = true
                            notesText = "Pre-configured 12GB RAM profile with Mesa Turnip Vulkan 1.3 acceleration."
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.DesktopWindows,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text("Apply 12GB Windows 11 Ultra Preset", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // OS Selector Chips
            Text(
                text = "Target Operating System",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OsType.entries.forEach { os ->
                    val isSelected = os == selectedOsType
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NeonCyan else GlassBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedOsType = os
                                vmName = "${os.displayName} VM"
                                ramMbSlider = os.defaultRamMb.toFloat()
                                cpuCoresSlider = os.defaultCores.toFloat()
                                diskSizeGbSlider = os.defaultDiskGb.toFloat()
                                tpm2Bypass = os.isWindows11
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) DarkSurfaceVariantGlass else DarkSurfaceGlass
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) NeonCyan.copy(alpha = 0.25f) else Color(0xFF1E293B)
                                    )
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = os.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) NeonCyan else Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = os.displayName,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Preset: ${os.defaultRamMb / 1024}GB RAM • ${os.defaultCores} Cores • ${os.defaultDiskGb}GB Disk",
                                    fontSize = 11.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }
                    }
                }
            }

            // Profile Name Input
            OutlinedTextField(
                value = vmName,
                onValueChange = { vmName = it },
                label = { Text("Profile Name", color = NeonCyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = GlassBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vm_name_input")
            )

            // Hardware Allocation Sliders (Up to 12 GB RAM)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Hardware Resource Allocation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // RAM Slider up to 12 GB (12288 MB)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Allocated RAM", color = Color(0xFF9CA3AF))
                            Text("${ramMbSlider.toInt()} MB (${ramMbSlider.toInt() / 1024} GB)", fontWeight = FontWeight.Bold, color = NeonEmerald)
                        }
                        Slider(
                            value = ramMbSlider,
                            onValueChange = { ramMbSlider = it },
                            valueRange = 1024f..12288f,
                            steps = 10,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonEmerald,
                                activeTrackColor = NeonEmerald
                            )
                        )
                    }

                    // CPU Cores Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("CPU Cores / Threads", color = Color(0xFF9CA3AF))
                            Text("${cpuCoresSlider.toInt()} Cores", fontWeight = FontWeight.Bold, color = NeonCyan)
                        }
                        Slider(
                            value = cpuCoresSlider,
                            onValueChange = { cpuCoresSlider = it },
                            valueRange = 1f..8f,
                            steps = 6,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonCyan,
                                activeTrackColor = NeonCyan
                            )
                        )
                    }

                    // Disk Size Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Virtual Disk Size (QCOW2)", color = Color(0xFF9CA3AF))
                            Text("${diskSizeGbSlider.toInt()} GB", fontWeight = FontWeight.Bold, color = NeonMagenta)
                        }
                        Slider(
                            value = diskSizeGbSlider,
                            onValueChange = { diskSizeGbSlider = it },
                            valueRange = 8f..128f,
                            steps = 14,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonMagenta,
                                activeTrackColor = NeonMagenta
                            )
                        )
                    }
                }
            }

            // Acceleration & Security Toggles
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Hardware Acceleration & Engine Flags",
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
                            Text("Linux KVM Kernel Acceleration", fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Direct hardware CPU virtualization via /dev/kvm", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                        }
                        Switch(
                            checked = kvmEnabled,
                            onCheckedChange = { kvmEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonEmerald, checkedTrackColor = NeonEmerald.copy(alpha = 0.3f))
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("VirGL 3D GPU Passthrough", fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Host GPU Vulkan 1.3 command stream passthrough", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                        }
                        Switch(
                            checked = virglEnabled,
                            onCheckedChange = { virglEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonCyan.copy(alpha = 0.3f))
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Windows 11 TPM 2.0 Bypass", fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Simulate software TPM chip for Windows 11 setup", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                        }
                        Switch(
                            checked = tpm2Bypass,
                            onCheckedChange = { tpm2Bypass = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonEmerald, checkedTrackColor = NeonEmerald.copy(alpha = 0.3f))
                        )
                    }
                }
            }

            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                label = { Text("Profile Notes & Instructions", color = NeonCyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = GlassBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Save Profile Button
            Button(
                onClick = {
                    viewModel.createVm(
                        name = vmName,
                        osType = selectedOsType,
                        ramMb = ramMbSlider.toInt(),
                        cpuCores = cpuCoresSlider.toInt(),
                        diskSizeGb = diskSizeGbSlider.toInt(),
                        kvmEnabled = kvmEnabled,
                        virglEnabled = virglEnabled,
                        tpm2Bypass = tpm2Bypass,
                        notes = notesText
                    )
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_vm_profile_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Create Virtual Machine Profile", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
