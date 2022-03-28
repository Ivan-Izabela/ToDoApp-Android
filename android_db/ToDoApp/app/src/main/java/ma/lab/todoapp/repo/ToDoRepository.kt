package ma.lab.todoapp.repo

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ma.lab.todoapp.db.ToDoDao
import ma.lab.todoapp.domain.ToDoItem
import ma.lab.todoapp.remote.ToDoApi
import ma.lab.todoapp.utils.MyResult
import ma.lab.todoapp.utils.TAG

class ToDoRepository (private val toDoDao: ToDoDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed
    val allToDo: LiveData<List<ToDoItem>> = toDoDao.getAll()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.

    suspend fun refresh(): MyResult<Boolean> {
        return try {
            Log.i(TAG, "trying to refresh plants")
            val todos = ToDoApi.service.getToDoItems()
            for (td in todos) {
                toDoDao.insert(td)
            }
            MyResult.Success(true)
        } catch(e: Exception) {
            MyResult.Error(e)
        }
    }


    suspend fun insert(toDoItem: ToDoItem): MyResult<ToDoItem>{
        return try{
            Log.i(TAG, "trying to save a todo in repo")
            Log.i(TAG, toDoItem.toString())
            val createdTodo=ToDoApi.service.addToDo(toDoItem)
            Log.v(TAG, "REPO RETURN $createdTodo")
            toDoDao.insert(createdTodo)
            MyResult.Success(createdTodo)

        }catch(e: Exception) {
            MyResult.Error(e)
        }
    }


    suspend fun delete(toDoItem: ToDoItem): MyResult<ToDoItem>{
        return try {
            Log.i(TAG, "trying to delete a plant")
            val deletedTodo=ToDoApi.service.deleteToDo(toDoItem.id.toString())
            toDoDao.delete(toDoItem)
            MyResult.Success(deletedTodo)
        }
        catch(e: Exception) {
            MyResult.Error(e)
        }

    }


    suspend fun update(toDoItem: ToDoItem): MyResult<ToDoItem>{
        return try {
            Log.i(TAG, "trying to delete a plant")
            val updatedTodo=ToDoApi.service.updateToDo(toDoItem.id.toString(),toDoItem)
            toDoDao.update(toDoItem)
            MyResult.Success(updatedTodo)
        }
        catch(e: Exception) {
            MyResult.Error(e)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(){
        toDoDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertToDos(todos: List<ToDoItem>){
        toDoDao.insertToDos(todos)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun nrOfItems(){
        toDoDao.numberOfItems()
    }









}