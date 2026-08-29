package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector

enum class PowerState(val label: String) {
    POWERED_OFF("Powered Off"),
    RUNNING("Running"),
    PAUSED("Paused"),
    SUSPENDED("Suspended")
}

enum class OsType(
    val displayName: String,
    val defaultRamMb: Int,
    val defaultCores: Int,
    val defaultDiskGb: Int,
    val icon: ImageVector,
    val isWindows11: Boolean = false
) {
    WINDOWS_11("Windows 11 Pro 12GB Vulkan Gaming", 12288, 6, 64, Icons.Default.DesktopWindows, true),
    WINDOWS_10("Windows 10 x64 Workstation", 8192, 4, 32, Icons.Default.DesktopWindows),
    UBUNTU_LINUX("Ubuntu 24.04 LTS (VirtIO 3D)", 4096, 2, 20, Icons.Default.Terminal),
    ALPINE_LINUX("Alpine Linux (Minimal)", 1024, 1, 8, Icons.Default.Terminal),
    REACT_OS("ReactOS x86 Legacy", 512, 1, 10, Icons.Default.Computer),
    CUSTOM("Custom ISO / Disk Image", 4096, 2, 20, Icons.Default.VideogameAsset)
}

enum class VulkanDriverMode(val displayName: String, val description: String) {
    TURNIP_MESA("Mesa Turnip Vulkan 1.3 (Adreno)", "Open-source Vulkan driver with native DirectX 11/12 via DXVK/VKD3D"),
    VENUS_VIRGL("VirtIO-GPU Venus (Host Passthrough)", "Low-level Vulkan command stream passthrough to host GPU"),
    ZINK_GL("Zink (OpenGL over Vulkan)", "Translates guest OpenGL calls directly to Vulkan pipeline"),
    SYSTEM_DEFAULT("System Default Driver", "Standard system graphics stack")
}

enum class DxvkVersion(val displayName: String) {
    DXVK_2_3("DXVK v2.3.1 (Latest DirectX 11/10/9)"),
    DXVK_1_10("DXVK v1.10.3 (Legacy DirectX 9)"),
    VKD3D_2_10("VKD3D-Proton v2.10 (DirectX 12)")
}

enum class AudioBackend(val displayName: String) {
    AAUDIO("AAudio (Ultra-Low Latency)"),
    OPENSL("OpenSL ES"),
    PULSEAUDIO("PulseAudio (Network Bridge)"),
    DISABLED("Muted")
}

data class SystemTelemetry(
    val cpuUsagePercent: Int = 0,
    val ramUsedMb: Int = 0,
    val ramTotalMb: Int = 12288,
    val vramUsedMb: Int = 0,
    val diskReadKbps: Int = 0,
    val diskWriteKbps: Int = 0,
    val fps: Int = 0,
    val isKvmActive: Boolean = true,
    val vulkanPipelineFps: Int = 60,
    val shaderCacheHitsPercent: Int = 98,
    val temperatureCelsius: Float = 38.5f
)
