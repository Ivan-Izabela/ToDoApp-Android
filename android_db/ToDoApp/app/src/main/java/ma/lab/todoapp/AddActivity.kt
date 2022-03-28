package ma.lab.todoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import ma.lab.todoapp.domain.ToDoItem
import ma.lab.todoapp.utils.logd
import java.time.LocalDateTime

class AddActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var deadLineEditTextDate: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_fragment)

        titleEditText= findViewById(R.id.titleEditText)
        descriptionEditText= findViewById(R.id.descriptionEditText)
        statusSpinner= findViewById(R.id.statusSpinner)
        deadLineEditTextDate=findViewById(R.id.deadLineEditTextDate)

        val button = findViewById<Button>(R.id.addButton)
        button.setOnClickListener {
            logd("edit button clicked")
            val replyIntent = Intent()
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val status = statusSpinner.selectedItem.toString()
            val deadLine = deadLineEditTextDate.text.toString()

            if(TextUtils.isEmpty(titleEditText.text) || TextUtils.isEmpty(descriptionEditText.text) || TextUtils.isEmpty(deadLineEditTextDate.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            else{
                replyIntent.putExtra("item_title",title)
                replyIntent.putExtra("item_description",description)
                replyIntent.putExtra("item_status",status)
                replyIntent.putExtra("item_deadline",deadLine)
                setResult(Activity.RESULT_OK, replyIntent)

            }
            finish()
        }
    }
    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

}