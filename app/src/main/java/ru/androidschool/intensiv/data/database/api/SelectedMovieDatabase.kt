package ru.androidschool.intensiv.data.database.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieActorJoin
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity

@Database(
    version = 2,
    entities = [MovieDbEntity::class, ActorDbEntity::class, MovieActorJoin::class]
)
abstract class SelectedMovieDatabase : RoomDatabase() {

    abstract fun actorDao(): ActorDao
    abstract fun movieDao(): MovieDao
    abstract fun movieWithActorDao(): MovieWithActorsDao

    companion object {
        private lateinit var INSTANCE: SelectedMovieDatabase

        @Synchronized
        operator fun invoke(context: Context): SelectedMovieDatabase {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    SelectedMovieDatabase::class.java,
                    "SelectedMovieDatabase"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE
        }
    }
}

internal val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE moviesManyToMany ADD COLUMN posterUrl TEXT NOT NULL DEFAULT ''")
    }
}