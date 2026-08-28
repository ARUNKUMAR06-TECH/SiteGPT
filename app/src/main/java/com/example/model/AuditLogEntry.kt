package com.example.model

data class AuditLogRecord(
    val id: String,
    val timestamp: String,
    val observationSnippet: String,
    val suggestedActivityId: String,
    val confidence: Int,
    val decision: String, // "Approved", "Edited & Approved", "Manual Selection"
    val finalActivityId: String,
    val userRole: String
)
