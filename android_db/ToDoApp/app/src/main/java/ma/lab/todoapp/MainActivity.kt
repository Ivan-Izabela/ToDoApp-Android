package ma.lab.todoapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

import ma.lab.todoapp.domain.ToDoItem
import ma.lab.todoapp.remote.Payload
import ma.lab.todoapp.remote.SocketData
import ma.lab.todoapp.remote.ToDoWebSocketClient
import ma.lab.todoapp.utils.Api
import ma.lab.todoapp.utils.TAG
import ma.lab.todoapp.utils.logd
import org.java_websocket.handshake.ServerHandshake
import java.time.LocalDateTime

class MainActivity : AppCompatActivity(), ToDoAdapter.onItemClickListner {
    private lateinit var toDoViewModel: ToDoViewModel
    private lateinit var toDoWebSocketClient: ToDoWebSocketClientActivity
    //private lateinit var manager: Manager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_fragment)

        val recyclerView = findViewById<RecyclerView>(R.id.toDoItemsRecyclerView)
        val adapter = ToDoAdapter(this)
        recyclerView.adapter=adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
        //manager= Manager()

        // Get a new or existing ViewModel from the ViewModelProvider.
        toDoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.


        val  progress =  findViewById<ProgressBar>(R.id.progressBar)

        toDoViewModel.loading.observe(this,{
                loading->
            Log.v(TAG, "dispalyng loading");
            progress.visibility = if (loading!!) View.VISIBLE else View.GONE
        })


        toDoViewModel.allToDo.observe(this,{
            // Update the cached copy of the words in the adapter.
            todos -> todos?.let { adapter.setToDos(it) }
        })

        toDoViewModel.loadingException.observe(this,{ loadingException ->
            if (loadingException != null) {
                Log.i(TAG, "Some error ocured when loading")
                val message = "Loading exception ${loadingException.message}"
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        })

        toDoViewModel.laodItems()

        val btn = findViewById<Button>(R.id.newbutton)
        btn.setOnClickListener {
            Log.v(TAG, "add button clicked")
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            addActivityLauncher.launch(intent)
        }



        adapter.setOnItemClickListner(this);

    }


    private val addActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.v(TAG, "back to main activity")

            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val title= data.getStringExtra("item_title");
                    val description= data.getStringExtra("item_description");
                    val status= data.getStringExtra("item_status");
                    val deadline= data.getStringExtra("item_deadline");
                    val todo=ToDoItem(0,title,description,status,deadline,LocalDateTime.now().toString())
                    toDoViewModel.insert(todo)
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Item not saved",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onEditClick(todo: ToDoItem) {
        Log.v(TAG, "edit button clicked")

        val intent = Intent(this@MainActivity, EditActivity::class.java)
        intent.putExtra("id", todo.id)
        intent.putExtra("title", todo.title)
        intent.putExtra("description", todo.description)
        intent.putExtra("status",todo.status)
        intent.putExtra("deadline",todo.deadline)
        editActivityLauncher.launch(intent)

    }

    private val editActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.v(TAG, "back to main activity")
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val title= data.getStringExtra("item_title");
                    val description= data.getStringExtra("item_description");
                    val status= data.getStringExtra("item_status");
                    val deadline= data.getStringExtra("item_deadline");
                    val id= data.getIntExtra("itme_id",-1)
                    val todo=ToDoItem(id,title,description,status,deadline,LocalDateTime.now().toString())
                    if(data.hasExtra("delete")){
                        toDoViewModel.delete(todo)
                    }
                    else{
                        toDoViewModel.update(todo)
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Item not updated",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    inner class ToDoWebSocketClientActivity(address: String) : ToDoWebSocketClient(address) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            Log.v(TAG, "on open")
            if(Api.tokenInterceptor.token != null){
                val payload = Payload(Api.tokenInterceptor.token!!)
                send(Gson().toJson(SocketData("authorization", payload )))
            }
        }

        override fun onMessage(message: String) {
            Log.v(TAG, "On message")
            //plantListViewModel.newItemIncoming(message)
        }
    }

    private fun setupWebSockets(){
        toDoWebSocketClient = ToDoWebSocketClientActivity("ws://10.152.0.225:3000")
        toDoWebSocketClient.connect()
    }




}