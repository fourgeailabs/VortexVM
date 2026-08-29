package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.OpenInNew
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonMagenta
import com.example.viewmodel.VmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    viewModel: VmViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appGitHubUrl by viewModel.appGitHubUrl.collectAsState()
    var isEditingRepoUrl by remember { mutableStateOf(false) }
    var tempRepoUrl by remember { mutableStateOf(appGitHubUrl) }

    val openUrlInBrowser = { url: String ->
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Vortex VM", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(DarkVoidBg)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("about_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero App Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, brush = Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta)), shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.25f))
                            .border(width = 1.dp, color = NeonCyan, shape = CircleShape)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DesktopWindows,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        )
                    }

                    Text(
                        text = "Vortex VM Hyper-Drive",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonCyan.copy(alpha = 0.25f))
                            .border(width = 0.5.dp, color = NeonCyan, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Version 1.02.00",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "High-performance virtual machine engine & control dashboard for Windows 11 and Linux with up to 12GB RAM allotment, Vulkan 1.3 hardware acceleration, display cutout hole punch rendering, and integrated QCOW2 storage management.",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            // Creator & Attribution Card (FourgeAI LABS)
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
                        text = "Application Creator & Developer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B))
                            .border(width = 1.dp, color = NeonCyan, shape = RoundedCornerShape(12.dp))
                            .clickable { openUrlInBrowser("https://github.com/fourgeailabs") }
                            .padding(16.dp)
                            .testTag("creator_fourgeai_labs_button"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column {
                                Text(
                                    text = "FourgeAI LABS",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NeonCyan,
                                    textDecoration = TextDecoration.Underline
                                )
                                Text(
                                    text = "https://github.com/fourgeailabs",
                                    fontSize = 11.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open Creator GitHub",
                            tint = NeonCyan
                        )
                    }
                }
            }

            // App GitHub Repository Card
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "App GitHub Repository",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        IconButton(onClick = { isEditingRepoUrl = !isEditingRepoUrl }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Repository Link",
                                tint = NeonCyan
                            )
                        }
                    }

                    if (isEditingRepoUrl) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = tempRepoUrl,
                                onValueChange = { tempRepoUrl = it },
                                label = { Text("App GitHub Repository URL", color = NeonCyan) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = GlassBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    viewModel.setAppGitHubUrl(tempRepoUrl)
                                    isEditingRepoUrl = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                            ) {
                                Text("Save Repository URL", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E293B))
                                .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(12.dp))
                                .clickable { openUrlInBrowser(appGitHubUrl) }
                                .padding(16.dp)
                                .testTag("app_github_repo_button"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "GitHub Repository",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = appGitHubUrl,
                                    fontSize = 11.sp,
                                    color = NeonCyan,
                                    textDecoration = TextDecoration.Underline
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.Launch,
                                contentDescription = "Launch Repo URL",
                                tint = NeonCyan
                            )
                        }
                    }
                }
            }

            // Engine & System Information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = GlassBorder, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Technical Architecture Specs",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    listOf(
                        "Package Identifier" to "com.fourgeailabs.vortexvm",
                        "Maximum RAM Allocation" to "Up to 12 GB (12,288 MB)",
                        "Display Cutout Support" to "Short-Edges Full Hole Punch Bleed",
                        "Virtualization Subsystem" to "Linux KVM / QEMU 8.2 Engine",
                        "3D Graphics Driver" to "Mesa Turnip Vulkan 1.3 Passthrough",
                        "Windows 11 Target" to "ARM64 & x86_64 with TPM 2.0 Bypass"
                    ).forEach { (label, value) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(label, fontSize = 11.sp, color = Color(0xFF9CA3AF))
                            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
