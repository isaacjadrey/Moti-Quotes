package com.codingwithjadrey.motiquotes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface MotiveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(motive: Motive)

    @Update
    suspend fun update(motive: Motive)

    @Delete
    suspend fun delete(motive: Motive)

    @Query("SELECT * FROM motive_item WHERE id = :id")
    fun getMotiveItem(id: Int): Flow<Motive>

    @Query("SELECT * FROM motive_item ORDER BY creationDate DESC")
    fun getALlMotiveItems(): Flow<List<Motive>>
}