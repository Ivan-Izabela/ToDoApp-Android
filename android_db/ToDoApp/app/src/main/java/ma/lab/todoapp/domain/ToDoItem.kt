package ma.lab.todoapp.domain

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "todo_table")
data class ToDoItem (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int =0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "deadline") val deadline: String?,
    @ColumnInfo(name = "createdAt") val createdAt: String
) {

    companion object{
        fun getAutoId():Int{
            val random= Random()
            return random.nextInt(100)
        }
    }

    override fun toString(): String {
        return "ToDoItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", deadline='" + deadline + '\'' +
                ", createdAt=" + createdAt +
                "}"
    }


}