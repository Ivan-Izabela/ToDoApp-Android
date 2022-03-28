package ma.lab.todoapp.service

import ma.lab.todoapp.domain.ToDoItem
import retrofit2.http.*

interface ToDoService {

    @GET("/item")
    suspend fun getToDoItems(): List<ToDoItem>

    @Headers("Content-Type: application/json")
    @POST("/item")
    suspend fun addToDo(@Body toDoItem: ToDoItem): ToDoItem

    @Headers("Content-Type: application/json")
    @PUT("/item/{id}")
    suspend fun updateToDo(@Path("id") toDoId: String,@Body toDoItem: ToDoItem): ToDoItem

    @Headers("Content-Type: application/json")
    @DELETE("/item/{id}")
    suspend fun deleteToDo(@Path("id") toDoId: String);



    companion object {
        const val SERVICE_ENDPOINT = "http://localhost:3000"
    }

}