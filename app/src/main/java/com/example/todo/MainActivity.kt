package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //Remove the item from the list
                listOfTasks.removeAt(position)

                //Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //Detect when the user clicks on Add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            //Executed when user clicks on button
////            Log.i("Caren", "User clicked on button")
//
//        }

        loadItems()

        //Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up the button and input field, so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.AddTaskField)
        //Get a reference to the button
        //and then set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            //Grab the text that the user has inputted into @id/AddTaskField
            val userInputtedTask = inputTextField.text.toString()

            //Add the string to out list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size -1)

            //Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data that the user has inputted
    // Save the data by reading and writing from a file


    //Create a method to get the data file we need
    fun getDataFile() : File {

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }
        catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into our file
    fun saveItems() {
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }
        catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}