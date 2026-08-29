package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PowerState
import kotlinx.coroutines.flow.Flow

@Dao
interface VmDao {
    @Query("SELECT * FROM vm_profiles ORDER BY createdTimestamp DESC")
    fun getAllVms(): Flow<List<VmEntity>>

    @Query("SELECT * FROM vm_profiles WHERE id = :id")
    fun getVmById(id: Long): Flow<VmEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVm(vm: VmEntity): Long

    @Update
    suspend fun updateVm(vm: VmEntity)

    @Query("UPDATE vm_profiles SET powerState = :state WHERE id = :id")
    suspend fun updatePowerState(id: Long, state: PowerState)

    @Delete
    suspend fun deleteVm(vm: VmEntity)

    // Snapshots
    @Query("SELECT * FROM vm_snapshots WHERE vmId = :vmId ORDER BY timestamp DESC")
    fun getSnapshotsForVm(vmId: Long): Flow<List<VmSnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: VmSnapshotEntity): Long

    @Delete
    suspend fun deleteSnapshot(snapshot: VmSnapshotEntity)

    // Shared Folders
    @Query("SELECT * FROM shared_folders WHERE vmId = :vmId")
    fun getSharedFoldersForVm(vmId: Long): Flow<List<SharedFolderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSharedFolder(folder: SharedFolderEntity): Long

    @Delete
    suspend fun deleteSharedFolder(folder: SharedFolderEntity)

    // Update Logs for What's New
    @Query("SELECT * FROM app_update_logs ORDER BY id DESC")
    fun getAllUpdateLogs(): Flow<List<AppUpdateLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateLog(log: AppUpdateLogEntity): Long
}
