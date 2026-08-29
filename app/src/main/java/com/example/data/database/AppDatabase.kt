package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AudioBackend
import com.example.data.model.DxvkVersion
import com.example.data.model.OsType
import com.example.data.model.PowerState
import com.example.data.model.VulkanDriverMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromPowerState(value: PowerState): String = value.name

    @TypeConverter
    fun toPowerState(value: String): PowerState = try {
        PowerState.valueOf(value)
    } catch (e: Exception) {
        PowerState.POWERED_OFF
    }

    @TypeConverter
    fun fromOsType(value: OsType): String = value.name

    @TypeConverter
    fun toOsType(value: String): OsType = try {
        OsType.valueOf(value)
    } catch (e: Exception) {
        OsType.WINDOWS_11
    }

    @TypeConverter
    fun fromAudioBackend(value: AudioBackend): String = value.name

    @TypeConverter
    fun toAudioBackend(value: String): AudioBackend = try {
        AudioBackend.valueOf(value)
    } catch (e: Exception) {
        AudioBackend.AAUDIO
    }

    @TypeConverter
    fun fromVulkanDriverMode(value: VulkanDriverMode): String = value.name

    @TypeConverter
    fun toVulkanDriverMode(value: String): VulkanDriverMode = try {
        VulkanDriverMode.valueOf(value)
    } catch (e: Exception) {
        VulkanDriverMode.TURNIP_MESA
    }

    @TypeConverter
    fun fromDxvkVersion(value: DxvkVersion): String = value.name

    @TypeConverter
    fun toDxvkVersion(value: String): DxvkVersion = try {
        DxvkVersion.valueOf(value)
    } catch (e: Exception) {
        DxvkVersion.DXVK_2_3
    }
}

@Database(
    entities = [
        VmEntity::class,
        VmSnapshotEntity::class,
        SharedFolderEntity::class,
        AppUpdateLogEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vmDao(): VmDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vortex_vm_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.vmDao())
                    }
                }
            }
        }

        private suspend fun populateInitialData(dao: VmDao) {
            // Seed Windows 11 Ultra Gaming 12GB RAM profile
            val win11Id = dao.insertVm(
                VmEntity(
                    name = "Windows 11 Pro 12GB Vulkan Turbo Gaming",
                    osType = OsType.WINDOWS_11,
                    ramMb = 12288,
                    cpuCores = 6,
                    diskSizeGb = 64,
                    diskPath = "/sdcard/VortexVM/disks/win11_vulkan_gaming.qcow2",
                    isoPath = "/sdcard/VortexVM/iso/Win11_23H2_ARM64.iso",
                    powerState = PowerState.POWERED_OFF,
                    kvmEnabled = true,
                    virglEnabled = true,
                    turnipVulkanEnabled = true,
                    vulkanDriverMode = VulkanDriverMode.TURNIP_MESA,
                    dxvkVersion = DxvkVersion.DXVK_2_3,
                    asyncShaderPrecompilation = true,
                    zramSwapMb = 8192,
                    tpm2Bypass = true,
                    audioBackend = AudioBackend.AAUDIO,
                    targetRefreshRateHz = 60,
                    notes = "Pre-configured 12GB RAM profile for Windows 11 with Mesa Turnip Vulkan 1.3 passthrough, DXVK 2.3 DirectX translation, and VirtIO high-speed drivers."
                )
            )

            // Seed Ubuntu Linux profile
            dao.insertVm(
                VmEntity(
                    name = "Ubuntu 24.04 VirtIO-GPU Workstation",
                    osType = OsType.UBUNTU_LINUX,
                    ramMb = 4096,
                    cpuCores = 2,
                    diskSizeGb = 32,
                    diskPath = "/sdcard/VortexVM/disks/ubuntu24.qcow2",
                    powerState = PowerState.POWERED_OFF,
                    kvmEnabled = true,
                    virglEnabled = true,
                    vulkanDriverMode = VulkanDriverMode.VENUS_VIRGL,
                    notes = "Full desktop Linux environment with Venus VirtIO Vulkan acceleration."
                )
            )

            // Seed initial shared folder
            dao.insertSharedFolder(
                SharedFolderEntity(
                    vmId = win11Id,
                    hostPath = "/sdcard/VortexShared",
                    guestMountPoint = "Z:\\Shared",
                    readOnly = false
                )
            )

            // Seed initial snapshot
            dao.insertSnapshot(
                VmSnapshotEntity(
                    vmId = win11Id,
                    title = "Clean Windows 11 Vulkan Ready",
                    memoryDumpSizeMb = 1024,
                    description = "Fresh Windows 11 desktop with Mesa Turnip, 12GB RAM allocation, and DXVK 2.3 drivers."
                )
            )

            // Seed What's New Release Logs
            dao.insertUpdateLog(
                AppUpdateLogEntity(
                    versionString = "1.02.00",
                    releaseDate = "August 2026",
                    title = "v1.02.00 - Total Visual Cyber-Neon Refresh, 12GB RAM Allotment & Hole Punch Cutout Drawing",
                    changesJson = """[
                        "Complete visual UI redesign featuring a sleek cyber-neon glassmorphism aesthetic with glowing gradients.",
                        "Expanded VM RAM allocation range up to 12 GB (12,288 MB) for heavy multi-tasking and Windows 11 AAA gaming.",
                        "Enabled full edge-to-edge layout drawing behind the Android display cutout / camera hole punch notch.",
                        "Upgraded telemetry gauges and live monitor display controls with futuristic neon visual meters.",
                        "Enhanced hardware acceleration page with 12GB RAM tuning and ZRAM compressed swap controls."
                    ]""",
                    isCurrentVersion = true
                )
            )

            dao.insertUpdateLog(
                AppUpdateLogEntity(
                    versionString = "1.01.00",
                    releaseDate = "August 2026",
                    title = "v1.01.00 - App-Wide Rebranding to Vortex VM & Advanced Vulkan Gaming Engine",
                    changesJson = """[
                        "App-wide rebranding to Vortex VM with new package com.fourgeailabs.vortexvm.",
                        "Advanced Low-Level Vulkan Architecture proposal & implementation for Windows 11 gaming.",
                        "Integrated Mesa Turnip Vulkan 1.3 open-source driver pipeline for Adreno GPUs.",
                        "Integrated DXVK 2.3 DirectX 9/10/11 -> Vulkan translation layer and VKD3D DirectX 12 support."
                    ]""",
                    isCurrentVersion = false
                )
            )

            dao.insertUpdateLog(
                AppUpdateLogEntity(
                    versionString = "1.00.00",
                    releaseDate = "August 2026",
                    title = "v1.00.00 - Initial Release & Hardware Acceleration",
                    changesJson = """[
                        "Initial release supporting Windows 11 ARM64 virtualization templates with TPM 2.0 bypass.",
                        "Native /dev/kvm kernel acceleration check.",
                        "Live Guest Power State Control Dashboard."
                    ]""",
                    isCurrentVersion = false
                )
            )
        }
    }
}
