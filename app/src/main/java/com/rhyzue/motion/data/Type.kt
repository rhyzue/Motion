package com.rhyzue.motion.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "type")
data class Type(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String?,
    @ColumnInfo val color: String
)

@Dao
interface TypeDao{

    @Query("SELECT * FROM type")
    fun getAllTypes(): LiveData<List<Type>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(type: Type):Long

    @Query("DELETE FROM type")
    fun deleteAll()

}

class TypeRepository(private val typeDao: TypeDao) {

    val allTypes: LiveData<List<Type>> = typeDao.getAllTypes()

    fun getAllTypes(){
        typeDao.getAllTypes()
    }

    fun insert(type: Type){
        typeDao.insert(type)
    }

}