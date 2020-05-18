package com.rhyzue.motion.data

import androidx.lifecycle.LiveData
import androidx.room.*
import org.apache.commons.lang3.time.DateUtils
import java.util.*

@Entity(tableName = "goal")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val content: String,
    @ColumnInfo val date_posted: Date,
    @ColumnInfo val time_achieve: Int,
    @ColumnInfo val status: Boolean ,
)

@Dao
interface GoalDao{
    @Query("SELECT * FROM goal")
    fun getAllGoal(): LiveData<List<Goal>>

    @Query("SELECT * FROM goal WHERE id = :id")
    suspend fun getGoalById(id: Int): Goal

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(goal: Goal): Long

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun modifyGoal(goal: Goal)

    @Query("DELETE FROM goal WHERE id = :id")
    suspend fun removeGoal(id: Int)

    @Query("DELETE FROM goal")
    fun deleteAll()


}
class GoalRepository(private val goalDao: GoalDao) {

    val allGoals: LiveData<List<Goal>> = goalDao.getAllGoal()

    suspend fun getGoalById(id: Int): Goal{
        return goalDao.getGoalById(id)
    }

    fun insert(goal: Goal): Long{
        return goalDao.insert(goal)
    }

    suspend fun modifyGoal(goal: Goal){
        goalDao.modifyGoal(goal)
    }

    suspend fun removeGoal(id: Int){
        goalDao.removeGoal(id)
    }


}