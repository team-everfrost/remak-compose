package com.everfrost.remak.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.everfrost.remak.data.entity.Document
import kotlinx.coroutines.flow.Flow


@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents")
    fun getAllDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE docId = :docId")
    fun getDocument(docId: String): Flow<Document?>

    @Query("DELETE FROM documents WHERE docId = :docId")
    suspend fun deleteDocument(docId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)

    @Update
    suspend fun updateDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)


}