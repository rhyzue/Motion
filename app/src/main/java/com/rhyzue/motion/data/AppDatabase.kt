package com.rhyzue.motion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [Task::class, Type::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun typeDao(): TypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            println("GET DATABASE")
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "motion_database.db"
                ).fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            println("OPEN CALLBACK")
            INSTANCE?.let { database ->
                scope.launch (Dispatchers.IO){
                    populateDatabase(database.taskDao(), database.typeDao())
                }
            }
        }

        suspend fun populateDatabase(taskDao: TaskDao, typeDao: TypeDao) {
            val type = Type(id=0,name="None", description="None", color="#ffffff")
            var task = Task(name="beginInsertTest", type=0, date_assigned = Date(),complete=false, deadline=null, goal_id=0)
            val pr = taskDao.insert(task)
            val pr2 = typeDao.insert(type)
            println("ADDED TYPES")
            println(pr)
            println(pr2)
        }
    }
}

