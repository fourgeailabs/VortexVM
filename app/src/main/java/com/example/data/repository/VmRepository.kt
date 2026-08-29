package com.example.data.repository

import com.example.data.database.AppUpdateLogEntity
import com.example.data.database.SharedFolderEntity
import com.example.data.database.VmDao
import com.example.data.database.VmEntity
import com.example.data.database.VmSnapshotEntity
import com.example.data.model.PowerState
import kotlinx.coroutines.flow.Flow

class VmRepository(private val vmDao: VmDao) {
    val allVms: Flow<List<VmEntity>> = vmDao.getAllVms()
    val allUpdateLogs: Flow<List<AppUpdateLogEntity>> = vmDao.getAllUpdateLogs()

    fun getVmById(id: Long): Flow<VmEntity?> = vmDao.getVmById(id)

    suspend fun insertVm(vm: VmEntity): Long = vmDao.insertVm(vm)

    suspend fun updateVm(vm: VmEntity) = vmDao.updateVm(vm)

    suspend fun updatePowerState(id: Long, state: PowerState) = vmDao.updatePowerState(id, state)

    suspend fun deleteVm(vm: VmEntity) = vmDao.deleteVm(vm)

    fun getSnapshotsForVm(vmId: Long): Flow<List<VmSnapshotEntity>> = vmDao.getSnapshotsForVm(vmId)

    suspend fun insertSnapshot(snapshot: VmSnapshotEntity): Long = vmDao.insertSnapshot(snapshot)

    suspend fun deleteSnapshot(snapshot: VmSnapshotEntity) = vmDao.deleteSnapshot(snapshot)

    fun getSharedFoldersForVm(vmId: Long): Flow<List<SharedFolderEntity>> = vmDao.getSharedFoldersForVm(vmId)

    suspend fun insertSharedFolder(folder: SharedFolderEntity): Long = vmDao.insertSharedFolder(folder)

    suspend fun deleteSharedFolder(folder: SharedFolderEntity) = vmDao.deleteSharedFolder(folder)
}
