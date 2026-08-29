package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Storage
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileSystemScreen(
    viewModel: VmViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vms by viewModel.vms.collectAsState()
    val sharedFolders by viewModel.currentSharedFolders.collectAsState()

    var newHostPath by remember { mutableStateOf("/sdcard/VortexShared") }
    var newGuestMount by remember { mutableStateOf("Z:\\Shared") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QCOW2 Disks & Shared Storage", fontWeight = FontWeight.Bold, color = Color.White) },
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
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(DarkVoidBg)
                .padding(innerPadding)
                .padding(16.dp)
                .testTag("file_system_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Virtual Storage Directory Overview
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, brush = Brush.horizontalGradient(listOf(NeonCyan, NeonEmerald)), shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Host Storage Directory Mapping",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "Root Virtual Storage: /sdcard/VortexVM/",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonEmerald
                        )
                        Text(
                            text = "Contains guest disk images (.qcow2), ISO install media, VirtIO driver packages, and shared folders mapped directly into guest Windows drives.",
                            fontSize = 11.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }

            // Virtual Disks List
            item {
                Text(
                    text = "Configured Guest Disks (.qcow2)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(vms) { vm ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column {
                                Text(vm.name, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(
                                    text = vm.diskPath,
                                    fontSize = 11.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonCyan.copy(alpha = 0.25f))
                                .border(width = 0.5.dp, color = NeonCyan, shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${vm.diskSizeGb} GB",
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Shared Folder Mapping Creator
            item {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FolderShared,
                                contentDescription = null,
                                tint = NeonMagenta,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Add Host-to-Guest Shared Folder",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        OutlinedTextField(
                            value = newHostPath,
                            onValueChange = { newHostPath = it },
                            label = { Text("Android Host Storage Path", color = NeonCyan) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = newGuestMount,
                            onValueChange = { newGuestMount = it },
                            label = { Text("Guest Windows Drive Mount", color = NeonCyan) },
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
                                vms.firstOrNull()?.let { vm ->
                                    viewModel.addSharedFolder(
                                        vmId = vm.id,
                                        hostPath = newHostPath,
                                        guestMountPoint = newGuestMount,
                                        readOnly = false
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Bridge Shared Directory", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Existing Shared Folders
            item {
                Text(
                    text = "Active Folder Mappings (${sharedFolders.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(sharedFolders) { folder ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Host: ${folder.hostPath}", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Guest Drive: ${folder.guestMountPoint}", fontSize = 11.sp, color = NeonEmerald)
                        }

                        IconButton(onClick = { viewModel.deleteSharedFolder(folder) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Mapping",
                                tint = NeonMagenta
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
