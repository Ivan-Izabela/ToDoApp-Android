package ma.lab.todoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ma.lab.todoapp.db.ToDoRoomDatabase
import ma.lab.todoapp.domain.ToDoItem
import ma.lab.todoapp.repo.ToDoRepository
import ma.lab.todoapp.service.ServiceFactory
import ma.lab.todoapp.service.ToDoService
import ma.lab.todoapp.utils.MyResult
import ma.lab.todoapp.utils.TAG

class ToDoViewModel(application: Application):AndroidViewModel(application) {
    private val repository: ToDoRepository



    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.


    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }
    private val mutableIsFetching = MutableLiveData<Boolean>().apply{ value = false}
    private val mutableFetchingException = MutableLiveData<Exception>().apply{ value = null }
    private val mutableIsDeleting = MutableLiveData<Boolean>().apply{ value = false}
    private val mutableDeletingException = MutableLiveData<Exception>().apply{ value = null}
    private val mutableJobDone = MutableLiveData<Boolean>().apply{ value = false }


    val allToDo: LiveData<List<ToDoItem>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingException: LiveData<Exception> = mutableException
    val isFetching: LiveData<Boolean> = mutableIsFetching
    val fetchingException: LiveData<Exception> = mutableFetchingException
    val isDeleting: LiveData<Boolean> = mutableIsDeleting
    val deletingException: LiveData<Exception> = mutableDeletingException
    val jobDone: LiveData<Boolean> = mutableJobDone


    init {
        val toDoDao = ToDoRoomDatabase.getDatabase(application,viewModelScope).toDoDao()
        repository = ToDoRepository(toDoDao)
        allToDo=repository.allToDo
    }


    fun laodItems() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = repository.refresh()){
                is MyResult.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }

            }
            mutableLoading.value = false
        }
    }


    /*
    fun fetchToDoFromNetwork(){

        viewModelScope.launch {
            mutableLoading.value = true
            try{
                mutableTodos.value=service.getToDoItems()
                launch(Dispatchers.IO) {
                    repository.deleteAll()
                    repository.insertToDos(allToDo.value!!)
                    print("aici1")

                }
            }catch (e: Exception) {
                mutableMessage.value = "Received an error while retrieving the data: ${e.message}"
            } finally {
                mutableLoading.value = false
            }
        }
    }

    fun fetchToDo(){
        mutableLoading.value = true
        try{
            GlobalScope.launch (Dispatchers.IO){
                val nrOfItems = repository.nrOfItems();
                print(nrOfItems)
                if (nrOfItems.equals(0)) {
                    fetchToDoFromNetwork()
                }
            }

        }catch (e: Exception) {
            mutableMessage.value = "Received an error while retrieving local data: ${e.message}"
        } finally {
            mutableLoading.value = false
        }
    }*/




    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */



    fun insert(toDoItem: ToDoItem) {
        mutableLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            Log.v(TAG, "save toDo...");
            mutableIsFetching.value = true
            mutableFetchingException.value = null
            val result: MyResult<ToDoItem>


            result = repository.insert(toDoItem)

            when(result) {
                is MyResult.Success -> {
                    Log.d(TAG, "saveItem succeeded");
                    mutableJobDone.value = true
                }
                is MyResult.Error -> {
                    Log.w(TAG, "saveItem failed", result.exception);
                    mutableFetchingException.value = result.exception
                }
            }
            mutableIsFetching.value = false
        }
    }

    fun delete(toDoItem: ToDoItem) {
        viewModelScope.launch {
            Log.i(TAG, "plant edit view model delete plant")

            mutableIsDeleting.value = true
            mutableDeletingException.value = null

            try{
                val deletionResult = repository.delete(toDoItem)
                Log.v(TAG, deletionResult.toString())
                mutableIsDeleting.value = false
                mutableDeletingException.value = null
                mutableJobDone.value = true
            }catch(e: Exception){
                Log.i(TAG, e.printStackTrace().toString())
                mutableFetchingException.value = e
                mutableIsFetching.value = false
            }
        }
    }

    fun update(toDoItem: ToDoItem) {
        viewModelScope.launch {
            Log.v(TAG, "update toDo...");
            mutableIsFetching.value = true
            mutableFetchingException.value = null
            val result: MyResult<ToDoItem>


            result = repository.update(toDoItem)

            when (result) {
                is MyResult.Success -> {
                    Log.d(TAG, "saveItem succeeded");
                    mutableJobDone.value = true
                }
                is MyResult.Error -> {
                    Log.w(TAG, "saveItem failed", result.exception);
                    mutableFetchingException.value = result.exception
                }
            }
            mutableIsFetching.value = false
        }
    }


}

