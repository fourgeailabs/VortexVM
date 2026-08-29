package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PowerState
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta
import com.example.viewmodel.VmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualDisplayScreen(
    vmId: Long,
    viewModel: VmViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vms by viewModel.vms.collectAsState()
    val vm = vms.find { it.id == vmId } ?: vms.firstOrNull()
    val telemetry by viewModel.telemetry.collectAsState()

    var isKeyboardVisible by remember { mutableStateOf(false) }
    var isGamepadVisible by remember { mutableStateOf(false) }
    var cursorPosition by remember { mutableStateOf(Offset(250f, 180f)) }
    var scale by remember { mutableFloatStateOf(1f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = vm?.name ?: "Guest Virtual Display",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${vm?.displayWidth ?: 1280}x${vm?.displayHeight ?: 720} • ${telemetry.fps} FPS • ${vm?.ramMb ?: 12288}MB RAM • KVM Active",
                            fontSize = 11.sp,
                            color = NeonEmerald
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isKeyboardVisible = !isKeyboardVisible }) {
                        Icon(
                            imageVector = Icons.Default.Keyboard,
                            contentDescription = "Toggle Keyboard",
                            tint = if (isKeyboardVisible) NeonCyan else Color.White
                        )
                    }
                    IconButton(onClick = { isGamepadVisible = !isGamepadVisible }) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = "Toggle Gamepad",
                            tint = if (isGamepadVisible) NeonMagenta else Color.White
                        )
                    }
                    IconButton(onClick = { scale = if (scale == 1f) 1.25f else 1f }) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Toggle Scale",
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
                .testTag("virtual_display_screen")
        ) {
            // Live Interactive Monitor Display Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(width = 1.dp, brush = Brush.horizontalGradient(listOf(NeonCyan, NeonEmerald)), shape = RoundedCornerShape(12.dp))
                    .background(Color(0xFF090D16))
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            cursorPosition = Offset(
                                (cursorPosition.x + pan.x).coerceIn(20f, 900f),
                                (cursorPosition.y + pan.y).coerceIn(20f, 600f)
                            )
                            scale = (scale * zoom).coerceIn(0.8f, 2.0f)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (vm?.powerState == PowerState.RUNNING) {
                    // Simulated Windows 11 / Guest Desktop View
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF0F172A), Color(0xFF020617))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DesktopWindows,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier
                                    .height(80.dp)
                                    .width(80.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "${vm.name} Active",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "12GB RAM • Mesa Turnip Vulkan 1.3 • VirtIO Network Bridged",
                                color = NeonEmerald,
                                fontSize = 12.sp
                            )
                        }

                        // Simulated Floating Pointer Cursor
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = cursorPosition.x.dp,
                                    top = cursorPosition.y.dp
                                )
                                .align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mouse,
                                contentDescription = "Cursor",
                                tint = NeonCyan,
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(28.dp)
                            )
                        }

                        // Simulated Taskbar at bottom
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .align(Alignment.BottomCenter)
                                .background(Color(0xFF030712).copy(alpha = 0.95f))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonCyan)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Start", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Text("Task Manager (12GB)", color = Color.White, fontSize = 11.sp)
                                Text("Vortex Storage Z:\\", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    // Powered Off or Paused Screen State
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Guest OS Power State: ${vm?.powerState?.label ?: "POWERED OFF"}",
                            color = Color.LightGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { vm?.let { viewModel.powerOnVm(it.id) } },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald, contentColor = Color.Black)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Boot Virtual Machine", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // On-Screen Virtual Controls Overlays
            if (isKeyboardVisible) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("On-Screen Virtual Keyboard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("ESC", "TAB", "CTRL", "ALT", "WIN", "DEL", "ENTER", "SPACE").forEach { key ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF1E293B))
                                        .border(width = 0.5.dp, color = GlassBorder, shape = RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(key, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            if (isGamepadVisible) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("XInput Gamepad Controller Active", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("A", "B", "X", "Y").forEach { btn ->
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(NeonMagenta)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(btn, color = Color.White, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }

            // Guest OS Quick Power Action Dock at bottom
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { vm?.let { viewModel.powerOnVm(it.id) } },
                        enabled = vm?.powerState != PowerState.RUNNING,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald, contentColor = Color.Black)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Text("Power On", fontSize = 11.sp)
                    }

                    Button(
                        onClick = { vm?.let { viewModel.pauseVm(it.id) } },
                        enabled = vm?.powerState == PowerState.RUNNING,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B), contentColor = Color.Black)
                    ) {
                        Icon(imageVector = Icons.Default.Pause, contentDescription = null)
                        Text("Pause", fontSize = 11.sp)
                    }

                    Button(
                        onClick = { vm?.let { viewModel.hardResetVm(it.id) } },
                        enabled = vm?.powerState == PowerState.RUNNING,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                    ) {
                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null)
                        Text("Reset", fontSize = 11.sp)
                    }

                    Button(
                        onClick = { vm?.let { viewModel.shutdownVm(it.id) } },
                        enabled = vm?.powerState != PowerState.POWERED_OFF,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta, contentColor = Color.White)
                    ) {
                        Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = null)
                        Text("Shutdown", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
