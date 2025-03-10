package com.example.newhomepage.repositories

import com.example.newhomepage.api.RetrofitClient
import com.example.newhomepage.api.models.EventResponse

class EventRepository {
    private val apiService = RetrofitClient.apiService

    /**
     * ดึงข้อมูลกิจกรรมทั้งหมด
     */
    suspend fun getAllEvents(): Result<List<EventResponse>> {
        return try {
            val response = apiService.getAllEvents()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.events)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to get events"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * ดึงข้อมูลกิจกรรมตาม ID
     */
    suspend fun getEventById(eventId: Int): Result<EventResponse> {
        return try {
            val response = apiService.getEventById(eventId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to get event"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * ค้นหากิจกรรมตามเงื่อนไข
     */
    suspend fun searchEvents(
        title: String? = null,
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<EventResponse>> {
        return try {
            val response = apiService.searchEvents(title, category, startDate, endDate)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.events)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Search failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * ดึงข้อมูลหมวดหมู่ทั้งหมด
     */
    suspend fun getAllCategories(): Result<List<String>> {
        return try {
            val response = apiService.getAllCategories()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to get categories"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * สร้างกิจกรรมใหม่
     */
    suspend fun createEvent(eventData: Map<String, Any>, token: String): Result<Int> {
        return try {
            val authToken = "Bearer $token"
            val response = apiService.createEvent(eventData, authToken)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data ?: -1)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create event"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * อัพเดทกิจกรรม
     */
    suspend fun updateEvent(eventId: Int, eventData: Map<String, Any>, token: String): Result<Boolean> {
        return try {
            val authToken = "Bearer $token"
            val response = apiService.updateEvent(eventId, eventData, authToken)

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update event"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * ลบกิจกรรม
     */
    suspend fun deleteEvent(eventId: Int, token: String): Result<Boolean> {
        return try {
            val authToken = "Bearer $token"
            val response = apiService.deleteEvent(eventId, authToken)

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete event"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * อัพโหลดรูปภาพกิจกรรม
     * หมายเหตุ: ต้องใช้ MultipartBody.Part สำหรับไฟล์ ซึ่งไม่ได้ระบุในโค้ดนี้
     */
    suspend fun uploadEventImage(eventId: Int, imageFile: Any, token: String): Result<String> {
        return try {
            // ตัวอย่างเท่านั้น - คุณต้องสร้าง MultipartBody.Part จากไฟล์ในการใช้งานจริง
            val authToken = "Bearer $token"
            val response = apiService.uploadEventImage(eventId, imageFile, authToken)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data ?: "")
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to upload image"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}