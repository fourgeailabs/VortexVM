package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.AppUpdateLogEntity
import com.example.data.database.SharedFolderEntity
import com.example.data.database.VmEntity
import com.example.data.database.VmSnapshotEntity
import com.example.data.model.OsType
import com.example.data.model.PowerState
import com.example.data.model.SystemTelemetry
import com.example.data.repository.VmRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class VmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: VmRepository
    val vms: StateFlow<List<VmEntity>>
    val updateLogs: StateFlow<List<AppUpdateLogEntity>>

    private val _selectedVmId = MutableStateFlow<Long?>(null)
    val selectedVmId: StateFlow<Long?> = _selectedVmId.asStateFlow()

    private val _telemetry = MutableStateFlow(SystemTelemetry())
    val telemetry: StateFlow<SystemTelemetry> = _telemetry.asStateFlow()

    // Accordion state for What's New: ID of expanded item, or null if all closed
    private val _expandedUpdateLogId = MutableStateFlow<Long?>(null)
    val expandedUpdateLogId: StateFlow<Long?> = _expandedUpdateLogId.asStateFlow()

    // App GitHub repo URL (default, customizable)
    private val _appGitHubUrl = MutableStateFlow("https://github.com/fourgeailabs/Vectras-VM-Android")
    val appGitHubUrl: StateFlow<String> = _appGitHubUrl.asStateFlow()

    private val _currentSnapshots = MutableStateFlow<List<VmSnapshotEntity>>(emptyList())
    val currentSnapshots: StateFlow<List<VmSnapshotEntity>> = _currentSnapshots.asStateFlow()

    private val _currentSharedFolders = MutableStateFlow<List<SharedFolderEntity>>(emptyList())
    val currentSharedFolders: StateFlow<List<SharedFolderEntity>> = _currentSharedFolders.asStateFlow()

    private var telemetryJob: Job? = null

    init {
        val dao = AppDatabase.getDatabase(application).vmDao()
        repository = VmRepository(dao)

        vms = repository.allVms.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        updateLogs = repository.allUpdateLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        startTelemetrySimulator()
    }

    fun selectVm(id: Long) {
        _selectedVmId.value = id
        loadVmDetails(id)
    }

    private fun loadVmDetails(vmId: Long) {
        viewModelScope.launch {
            repository.getSnapshotsForVm(vmId).collect { snapshots ->
                _currentSnapshots.value = snapshots
            }
        }
        viewModelScope.launch {
            repository.getSharedFoldersForVm(vmId).collect { folders ->
                _currentSharedFolders.value = folders
            }
        }
    }

    // Power State Controls
    fun setPowerState(vmId: Long, state: PowerState) {
        viewModelScope.launch {
            repository.updatePowerState(vmId, state)
            if (state == PowerState.RUNNING) {
                _selectedVmId.value = vmId
            }
        }
    }

    fun powerOnVm(vmId: Long) = setPowerState(vmId, PowerState.RUNNING)
    fun pauseVm(vmId: Long) = setPowerState(vmId, PowerState.PAUSED)
    fun shutdownVm(vmId: Long) = setPowerState(vmId, PowerState.POWERED_OFF)
    fun hardResetVm(vmId: Long) {
        viewModelScope.launch {
            repository.updatePowerState(vmId, PowerState.POWERED_OFF)
            delay(500)
            repository.updatePowerState(vmId, PowerState.RUNNING)
        }
    }

    fun createSnapshot(vmId: Long, title: String, description: String) {
        viewModelScope.launch {
            val snapshot = VmSnapshotEntity(
                vmId = vmId,
                title = title.ifBlank { "Snapshot ${System.currentTimeMillis() % 10000}" },
                memoryDumpSizeMb = 1024,
                description = description
            )
            repository.insertSnapshot(snapshot)
            repository.updatePowerState(vmId, PowerState.SUSPENDED)
        }
    }

    // VM Creation
    fun createVm(
        name: String,
        osType: OsType,
        ramMb: Int,
        cpuCores: Int,
        diskSizeGb: Int,
        kvmEnabled: Boolean,
        virglEnabled: Boolean,
        tpm2Bypass: Boolean,
        notes: String
    ) {
        viewModelScope.launch {
            val newVm = VmEntity(
                name = name.ifBlank { "${osType.displayName} VM" },
                osType = osType,
                ramMb = ramMb,
                cpuCores = cpuCores,
                diskSizeGb = diskSizeGb,
                diskPath = "/sdcard/VectrasVM/disks/${name.lowercase().replace(" ", "_")}.qcow2",
                isoPath = if (osType.isWindows11) "/sdcard/VectrasVM/iso/Win11_23H2_ARM64.iso" else "",
                kvmEnabled = kvmEnabled,
                virglEnabled = virglEnabled,
                turnipVulkanEnabled = true,
                tpm2Bypass = tpm2Bypass,
                notes = notes
            )
            val newId = repository.insertVm(newVm)
            // Default shared folder
            repository.insertSharedFolder(
                SharedFolderEntity(
                    vmId = newId,
                    hostPath = "/sdcard/VectrasShared",
                    guestMountPoint = "Z:\\Shared"
                )
            )
        }
    }

    fun updateVmConfig(vm: VmEntity) {
        viewModelScope.launch {
            repository.updateVm(vm)
        }
    }

    fun deleteVm(vm: VmEntity) {
        viewModelScope.launch {
            repository.deleteVm(vm)
            if (_selectedVmId.value == vm.id) {
                _selectedVmId.value = null
            }
        }
    }

    // Shared Folders Management
    fun addSharedFolder(vmId: Long, hostPath: String, guestMountPoint: String, readOnly: Boolean) {
        viewModelScope.launch {
            repository.insertSharedFolder(
                SharedFolderEntity(
                    vmId = vmId,
                    hostPath = hostPath.ifBlank { "/sdcard/VectrasShared" },
                    guestMountPoint = guestMountPoint.ifBlank { "Z:\\Shared" },
                    readOnly = readOnly
                )
            )
        }
    }

    fun deleteSharedFolder(folder: SharedFolderEntity) {
        viewModelScope.launch {
            repository.deleteSharedFolder(folder)
        }
    }

    // Accordion toggle for "What's New" screen
    fun toggleUpdateLogExpansion(id: Long) {
        _expandedUpdateLogId.value = if (_expandedUpdateLogId.value == id) null else id
    }

    // Update App GitHub URL
    fun setAppGitHubUrl(url: String) {
        _appGitHubUrl.value = url.ifBlank { "https://github.com/fourgeailabs/Vectras-VM-Android" }
    }

    private fun startTelemetrySimulator() {
        telemetryJob?.cancel()
        telemetryJob = viewModelScope.launch {
            while (true) {
                val runningVmList = vms.value.filter { it.powerState == PowerState.RUNNING }
                val isAnyRunning = runningVmList.isNotEmpty()

                if (isAnyRunning) {
                    val activeVm = runningVmList.first()
                    val targetFps = if (activeVm.virglEnabled) Random.nextInt(52, 61) else Random.nextInt(24, 32)
                    _telemetry.value = SystemTelemetry(
                        cpuUsagePercent = Random.nextInt(18, 65),
                        ramUsedMb = activeVm.ramMb - Random.nextInt(500, 1200),
                        ramTotalMb = activeVm.ramMb,
                        vramUsedMb = if (activeVm.virglEnabled) Random.nextInt(1024, 3072) else 256,
                        diskReadKbps = Random.nextInt(2400, 18500),
                        diskWriteKbps = Random.nextInt(1200, 9200),
                        fps = targetFps,
                        isKvmActive = activeVm.kvmEnabled,
                        temperatureCelsius = 38.0f + Random.nextFloat() * 4f
                    )
                } else {
                    _telemetry.value = SystemTelemetry(
                        cpuUsagePercent = Random.nextInt(2, 6),
                        ramUsedMb = 820,
                        ramTotalMb = 8192,
                        vramUsedMb = 120,
                        diskReadKbps = 0,
                        diskWriteKbps = 0,
                        fps = 0,
                        isKvmActive = true,
                        temperatureCelsius = 35.2f
                    )
                }
                delay(1200)
            }
        }
    }
}
