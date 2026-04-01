package ru.nsu.salina.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.nsu.salina.data.local.dao.ActivePlanDao
import ru.nsu.salina.data.local.dao.CompletedWorkoutDao
import ru.nsu.salina.data.local.dao.DayPlanDao
import ru.nsu.salina.data.local.dao.ExerciseDao
import ru.nsu.salina.data.local.entity.ActivePlanEntity
import ru.nsu.salina.data.local.entity.CompletedWorkoutEntity
import ru.nsu.salina.data.local.entity.DayPlanEntity
import ru.nsu.salina.data.local.entity.ExerciseEntity

@Database(
    entities = [
        ActivePlanEntity::class,
        DayPlanEntity::class,
        ExerciseEntity::class,
        CompletedWorkoutEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activePlanDao(): ActivePlanDao
    abstract fun dayPlanDao(): DayPlanDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun completedWorkoutDao(): CompletedWorkoutDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE active_plan ADD COLUMN currentDayDate TEXT")
            }
        }

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "harmonie.db"
                ).addMigrations(MIGRATION_1_2).build().also { INSTANCE = it }
            }
    }
}
