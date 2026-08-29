package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AudioBackend
import com.example.data.model.DxvkVersion
import com.example.data.model.OsType
import com.example.data.model.PowerState
import com.example.data.model.VulkanDriverMode

@Entity(tableName = "vm_profiles")
data class VmEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val osType: OsType,
    val ramMb: Int,
    val cpuCores: Int,
    val diskSizeGb: Int,
    val diskPath: String,
    val isoPath: String = "",
    val powerState: PowerState = PowerState.POWERED_OFF,
    val kvmEnabled: Boolean = true,
    val virglEnabled: Boolean = true,
    val turnipVulkanEnabled: Boolean = true,
    val vulkanDriverMode: VulkanDriverMode = VulkanDriverMode.TURNIP_MESA,
    val dxvkVersion: DxvkVersion = DxvkVersion.DXVK_2_3,
    val asyncShaderPrecompilation: Boolean = true,
    val zramSwapMb: Int = 8192,
    val tpm2Bypass: Boolean = true,
    val audioBackend: AudioBackend = AudioBackend.AAUDIO,
    val displayWidth: Int = 1280,
    val displayHeight: Int = 720,
    val targetRefreshRateHz: Int = 60,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "vm_snapshots")
data class VmSnapshotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vmId: Long,
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val memoryDumpSizeMb: Int = 512,
    val description: String = ""
)

@Entity(tableName = "shared_folders")
data class SharedFolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vmId: Long,
    val hostPath: String,
    val guestMountPoint: String = "Z:\\Shared",
    val readOnly: Boolean = false
)

@Entity(tableName = "app_update_logs")
data class AppUpdateLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val versionString: String,
    val releaseDate: String,
    val title: String,
    val changesJson: String,
    val isCurrentVersion: Boolean = false
)
