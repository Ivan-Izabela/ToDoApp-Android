package ma.lab.todoapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import ma.lab.todoapp.utils.logd

class EditActivity: AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var deadLineEditTextDate: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_fragment)

        titleEditText= findViewById(R.id.titleEditText)
        descriptionEditText= findViewById(R.id.descriptionEditText)
        statusSpinner= findViewById(R.id.statusSpinner)
        deadLineEditTextDate=findViewById(R.id.deadLineEditTextDate)

        val intent = intent


        titleEditText.setText(intent.getStringExtra("title"))
        descriptionEditText.setText(intent.getStringExtra("description"))
        statusSpinner.setSelection((statusSpinner.getAdapter() as ArrayAdapter<String>).getPosition(intent.getStringExtra("status")))
        deadLineEditTextDate.setText(intent.getStringExtra("deadline"))

        val buttonD= findViewById<Button>(R.id.deletebutton)
        buttonD.setOnClickListener {
            logd("update button clicked")
            val replyIntent = Intent()
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val status = statusSpinner.selectedItem.toString()
            val deadLine = deadLineEditTextDate.text.toString()

            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton("Yes") { _, _ ->
                val delete = "yes"
                replyIntent.putExtra("delete",delete)
                replyIntent.putExtra("item_title",title)
                replyIntent.putExtra("item_description",description)
                replyIntent.putExtra("item_status",status)
                replyIntent.putExtra("item_deadline",deadLine)
                replyIntent.putExtra("itme_id",intent.getIntExtra("id",-1))
                setResult(Activity.RESULT_OK,replyIntent)
                finish()

            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Delete '${intent.getStringExtra("title")}'?")
            builder.setMessage("Are you sure you want to remove '${intent.getStringExtra("title")}'?")
            builder.create().show()
        }



        val button = findViewById<Button>(R.id.saveButton)
        button.setOnClickListener {
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
                replyIntent.putExtra("itme_id",intent.getIntExtra("id",-1))
                setResult(Activity.RESULT_OK, replyIntent)

            }
            finish()
        }
    }
    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}