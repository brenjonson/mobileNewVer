package com.example.newhomepage

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newhomepage.repositories.EventRepository
import com.example.newhomepage.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateEventActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var createButton: Button
    private lateinit var eventRepository: EventRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize views
        titleEditText = findViewById(R.id.eventTitleEditText)
        descriptionEditText = findViewById(R.id.eventDescriptionEditText)
        locationEditText = findViewById(R.id.eventLocationEditText)
        dateEditText = findViewById(R.id.eventDateEditText)
        timeEditText = findViewById(R.id.eventTimeEditText)
        createButton = findViewById(R.id.createEventButton)

        eventRepository = EventRepository()
        sessionManager = SessionManager(this)

        createButton.setOnClickListener {
            createEvent()
        }
    }

    private fun createEvent() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val location = locationEditText.text.toString()
        val date = dateEditText.text.toString()
        val time = timeEditText.text.toString()

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
            return
        }

        // สร้าง Map ของข้อมูลกิจกรรม
        val eventData = mapOf(
            "title" to title,
            "description" to description,
            "location" to location,
            "date" to date,
            "time" to time,
            "created_by" to (sessionManager.getUser()?.id ?: 0)
        )

        // ดึง token จาก SessionManager
        val token = sessionManager.getToken() ?: ""
        if (token.isEmpty()) {
            Toast.makeText(this, "กรุณาเข้าสู่ระบบใหม่", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    eventRepository.createEvent(eventData, token)
                }

                result.fold(
                    onSuccess = { eventId ->
                        Toast.makeText(this@CreateEventActivity, "สร้างกิจกรรมสำเร็จ", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = { exception ->
                        Toast.makeText(this@CreateEventActivity, "เกิดข้อผิดพลาด: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@CreateEventActivity, "เกิดข้อผิดพลาด: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}