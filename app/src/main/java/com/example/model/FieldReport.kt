package com.example.model

data class ExtractedFieldData(
    val activityType: String,
    val location: String,
    val quantity: Double,
    val unit: String,
    val workforce: Int,
    val dateText: String = "Today",
    val photoEvidenceDetected: Boolean = false,
    val visionAnalysis: String = ""
)

data class FieldReportRecord(
    val id: String,
    val timestamp: String,
    val location: String,
    val observation: String,
    val quantity: Double,
    val unit: String,
    val workforce: Int,
    val linkedActivityId: String,
    val linkedActivityName: String,
    val confidence: Int,
    val approvalStatus: String, // "Approved", "Edited & Approved", "Pending"
    val photoAnalysis: String? = null
)
