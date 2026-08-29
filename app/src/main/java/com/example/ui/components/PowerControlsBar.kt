package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.database.VmEntity
import com.example.data.model.PowerState

@Composable
fun PowerControlsBar(
    vm: VmEntity,
    onPowerOn: () -> Unit,
    onPause: () -> Unit,
    onShutdown: () -> Unit,
    onHardReset: () -> Unit,
    onCreateSnapshot: () -> Unit,
    onOpenDisplay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("power_controls_bar_${vm.id}"),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Power On / Start Button
        FilledTonalIconButton(
            onClick = onPowerOn,
            enabled = vm.powerState != PowerState.RUNNING,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = Color(0xFF10B981),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Power On VM"
            )
        }

        // Open Display Monitor
        FilledTonalIconButton(
            onClick = onOpenDisplay,
            enabled = vm.powerState == PowerState.RUNNING || vm.powerState == PowerState.PAUSED,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Monitor,
                contentDescription = "Open Guest Display"
            )
        }

        // Pause VM
        FilledTonalIconButton(
            onClick = onPause,
            enabled = vm.powerState == PowerState.RUNNING,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = Color(0xFFF59E0B),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pause VM"
            )
        }

        // Hard Reset
        FilledTonalIconButton(
            onClick = onHardReset,
            enabled = vm.powerState == PowerState.RUNNING || vm.powerState == PowerState.PAUSED,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.RestartAlt,
                contentDescription = "Hard Reset VM"
            )
        }

        // Save Snapshot
        FilledTonalIconButton(
            onClick = onCreateSnapshot,
            enabled = vm.powerState == PowerState.RUNNING || vm.powerState == PowerState.PAUSED,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Create Snapshot"
            )
        }

        // Graceful Power Off
        FilledTonalIconButton(
            onClick = onShutdown,
            enabled = vm.powerState != PowerState.POWERED_OFF,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = Color(0xFFEF4444),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = "Power Off VM"
            )
        }
    }
}
