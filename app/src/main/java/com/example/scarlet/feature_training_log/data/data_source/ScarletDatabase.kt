package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.scarlet.feature_training_log.data.data_source.converters.DateConverter
import com.example.scarlet.feature_training_log.data.data_source.dao.BlockDao
import com.example.scarlet.feature_training_log.data.data_source.dao.DayDao
import com.example.scarlet.feature_training_log.data.data_source.dao.ExerciseDao
import com.example.scarlet.feature_training_log.data.data_source.dao.MovementDao
import com.example.scarlet.feature_training_log.data.data_source.dao.SessionDao
import com.example.scarlet.feature_training_log.data.data_source.dao.SetDao
import com.example.scarlet.feature_training_log.data.data_source.entity.BlockEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.DayEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SetEntity

@Database(
    entities = [
        BlockEntity::class,
        DayEntity::class,
        SessionEntity::class,
        ExerciseEntity::class,
        MovementEntity::class,
        SetEntity::class
    ],
    version = 6,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = ScarletDatabase.Migration1to2::class),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 4, to = 5, spec = ScarletDatabase.Migration4to5::class),
        AutoMigration(from = 5, to = 6, spec = ScarletDatabase.Migration5to6::class)
    ],
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class ScarletDatabase : RoomDatabase() {

    abstract val blockDao: BlockDao
    abstract val dayDao: DayDao
    abstract val sessionDao: SessionDao
    abstract val exerciseDao: ExerciseDao
    abstract val setDao: SetDao
    abstract val movementDao: MovementDao

    @RenameColumn(tableName = "set", fromColumnName = "rpe", toColumnName = "rating")
    class Migration1to2: AutoMigrationSpec

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP INDEX `index_exercise_sessionId_order`")
                database.execSQL("ALTER TABLE `exercise` ADD COLUMN `supersetOrder` INTEGER NOT NULL DEFAULT 1")
                database.execSQL("CREATE UNIQUE INDEX `index_exercise_sessionId_order_supersetOrder` ON `exercise` (`sessionId`, `order`, `supersetOrder`)")
            }
        }
    }

    @DeleteColumn(tableName = "day", columnName = "name")
    class Migration4to5: AutoMigrationSpec

    @RenameColumn(tableName = "set", fromColumnName = "weight", toColumnName = "load")
    class Migration5to6: AutoMigrationSpec
}