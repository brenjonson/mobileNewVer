package com.example.newhomepage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newhomepage.utils.SessionManager

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        sessionManager = SessionManager(this)

        // ตรวจสอบว่าเป็นแอดมินจริงหรือไม่
        val user = sessionManager.getUser()
        if (user == null || user.role != "admin") {
            Toast.makeText(this, "คุณไม่มีสิทธิ์เข้าถึงหน้านี้", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val createEventButton = findViewById<Button>(R.id.createEventButton)
        createEventButton.setOnClickListener {
            // เปิดหน้าสร้างกิจกรรมใหม่
            val intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }

        val manageEventsButton = findViewById<Button>(R.id.manageEventsButton)
        manageEventsButton.setOnClickListener {
            // เปิดหน้าจัดการกิจกรรมทั้งหมด
            Toast.makeText(this, "กำลังพัฒนา...", Toast.LENGTH_SHORT).show()
        }

        // โค้ดเพิ่มเติมสำหรับฟังก์ชันอื่นๆ ของแอดมิน
    }
}