package ma.lab.todoapp.remote
import com.google.gson.GsonBuilder
import ma.lab.todoapp.domain.ToDoItem
import ma.lab.todoapp.utils.Api


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ToDoApi {
    private const val URL = "http://localhost:3000"

    interface Service {
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
        suspend fun deleteToDo(@Path("id") toDoId: String): ToDoItem
    }
    val service: Service = Api.retrofit.create(Service::class.java)
}