package ma.lab.todoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

import ma.lab.todoapp.domain.ToDoItem

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_table")
    fun getAll(): LiveData<List<ToDoItem>>

    @Insert
    fun insert(toDo: ToDoItem)

    @Insert
    fun insertToDos(todos: List<ToDoItem>)

    @Delete
    fun delete(toDo: ToDoItem)

    @Update
    fun update(toDo: ToDoItem);

    @Query("DELETE FROM todo_table")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM todo_table")
    fun numberOfItems(): Int


}