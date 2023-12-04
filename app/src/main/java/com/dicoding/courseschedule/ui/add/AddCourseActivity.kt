package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.DayName
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private val viewModel by viewModels<AddCourseViewModel>{
        ListViewModelFactory.createFactory(this)
    }

    private lateinit var binding: ActivityAddCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_course)

        viewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true)
                onBackPressed()
            else {
                val message = getString(R.string.input_empty_message)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_insert -> {
                val courseName = binding.edCourseName.text.toString()
                val day = binding.spinnerDay.selectedItemPosition
                val startTime = binding.tvStartTime.text.toString()
                val endTime = binding.tvEndTime.text.toString()
                val lecturer = binding.edLecturer.text.toString()
                val note = binding.edNote.text.toString()
                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                this.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showStartTimePicker(view: View){
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "START_TIME")
    }

    fun showEndTimePicker(view: View){
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "END_TIME")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        when(tag){
            "START_TIME" -> {
                binding.tvStartTime.text = String.format("%02d:%02d", hour, minute)
            }
            "END_TIME" -> {
                binding.tvEndTime.text = String.format("%02d:%02d", hour, minute)
            }
        }
    }

    fun showToast(Message : String){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show()
    }
}