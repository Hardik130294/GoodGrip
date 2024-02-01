package com.hardik.goodgrip.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.models.TodoResponseItem
import com.hardik.goodgrip.models.UserResponseItem

@Database(
    entities = [PostResponseItem::class, CommentResponseItem::class, AlbumResponseItem::class, PhotoResponseItem::class, TodoResponseItem::class, UserResponseItem::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDatabaseInstance : RoomDatabase() {

    abstract fun getRoomDatabaseDao(): RoomDatabaseDao

    companion object {
        @Volatile
        private var instance: RoomDatabaseInstance? = null
        private val LOCK = Any()//single instance

        operator fun invoke(context: Context) =
            instance ?: synchronized(LOCK) {
                instance ?: createDatabase(context).also {
                    instance = it
                }
            }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDatabaseInstance::class.java,
                "database.db"
            ).build()
    }
}
//this is also singleton database