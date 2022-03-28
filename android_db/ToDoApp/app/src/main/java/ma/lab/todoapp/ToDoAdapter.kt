package ma.lab.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ma.lab.todoapp.domain.ToDoItem


class ToDoAdapter(
    context: Context
): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var todos = mutableListOf<ToDoItem>() // Cached copy of words
    private lateinit var listner: onItemClickListner






    inner class ToDoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val statusText: TextView = itemView.findViewById(R.id.status_text)
        val deadLineText: TextView = itemView.findViewById(R.id.deadLine_text)
        val editButton: TextView = itemView.findViewById(R.id.editButton)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemView = inflater.inflate(R.layout.todo_layout,parent,false)
        return ToDoViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val item=todos[position]
        holder.titleText.text= item.title
        holder.statusText.text=item.status
        holder.deadLineText.text=item.deadline
        holder.editButton.setOnClickListener{
            if (listner != null && position != RecyclerView.NO_POSITION) {
                listner.onEditClick(todos[position])
            }
        };

    }
    internal fun setToDos(todos: List<ToDoItem>) {
        this.todos.clear()
        this.todos.addAll(todos)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    fun getToDoAt(position: Int): ToDoItem? {
        return todos.get(position)
    }


    interface onItemClickListner {
        fun onEditClick(todo: ToDoItem)
    }

    fun setOnItemClickListner(listner: onItemClickListner) {
        this.listner = listner
    }



}




