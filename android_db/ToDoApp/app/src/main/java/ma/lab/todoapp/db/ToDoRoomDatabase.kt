package ma.lab.todoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ma.lab.todoapp.domain.ToDoItem
import java.time.LocalDateTime

@Database(entities = [ToDoItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ToDoRoomDatabase : RoomDatabase(){

    abstract fun toDoDao():ToDoDao

    companion object{
        @Volatile
        private var INSTANCE : ToDoRoomDatabase?=null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
            ): ToDoRoomDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoRoomDatabase::class.java,
                    "todo_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .addCallback(ToDoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }

        }

        private class ToDoDatabaseCallback(
            private val scope: CoroutineScope
        ): RoomDatabase.Callback(){
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.toDoDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        fun populateDatabase(toDoDao: ToDoDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            toDoDao.deleteAll()


            var toDoItem = ToDoItem(1,"Lab2 ma","crud ui naiv","Started",
                LocalDateTime.of(2021,12,8,10,0,0).toString(),
                LocalDateTime.now().toString() );
            toDoDao.insert(toDoItem);

        }
    }
}

