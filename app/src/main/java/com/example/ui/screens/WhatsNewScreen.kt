package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AccordionItem
import com.example.ui.theme.DarkVoidBg
import com.example.viewmodel.VmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNewScreen(
    viewModel: VmViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.updateLogs.collectAsState()
    val expandedLogId by viewModel.expandedUpdateLogId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("What's New in Vortex VM", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .testTag("whats_new_screen"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Release Notes & Version Updates",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tap on any version below to inspect detailed update notes and historical release logs.",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(
                items = logs,
                key = { it.id }
            ) { log ->
                AccordionItem(
                    updateLog = log,
                    isExpanded = expandedLogId == log.id,
                    onToggle = { viewModel.toggleUpdateLogExpansion(log.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
