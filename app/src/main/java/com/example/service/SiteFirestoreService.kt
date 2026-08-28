package com.example.service

import android.util.Log
import com.example.model.SiteEvidenceReport
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object SiteFirestoreService {
    private const val TAG = "SiteFirestoreService"

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w(TAG, "Firestore not initialized or unavailable: ${e.message}")
            null
        }
    }

    suspend fun saveSiteReport(report: SiteEvidenceReport): Boolean {
        return try {
            val db = firestore ?: return false
            val reportMap = hashMapOf(
                "id" to report.id,
                "projectId" to report.projectId,
                "activityId" to report.activityId,
                "activityName" to report.activityName,
                "plannedProgress" to report.plannedProgress,
                "actualProgress" to report.actualProgress,
                "progressVariance" to report.progressVariance,
                "status" to report.status,
                "delayHours" to report.delayHours,
                "delayDays" to report.delayDays,
                "delayReason" to report.delayReason,
                "observation" to report.observation,
                "capturedAt" to report.capturedAt,
                "source" to hashMapOf(
                    "photo" to report.source.photo,
                    "voice" to report.source.voice,
                    "text" to report.source.text
                ),
                "aiConfidence" to report.aiConfidence,
                "isDemo" to report.isDemo,
                "visibleElements" to report.visibleElements,
                "issues" to report.issues,
                "equipment" to report.equipment,
                "materials" to report.materials,
                "syncedToFirestore" to true
            )
            db.collection("site_reports").document(report.id).set(reportMap).await()
            Log.i(TAG, "Successfully synced report ${report.id} to Firestore")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to save report to Firestore: ${e.message}")
            false
        }
    }

    suspend fun updateActivityProgress(
        activityId: String,
        actualProgress: Int,
        actualQuantity: Double?,
        delayReason: String,
        status: String
    ): Boolean {
        return try {
            val db = firestore ?: return false
            val updates = hashMapOf<String, Any>(
                "actualProgress" to actualProgress,
                "delayReason" to delayReason,
                "status" to status,
                "updatedAt" to System.currentTimeMillis()
            )
            if (actualQuantity != null) {
                updates["actualQuantity"] = actualQuantity
            }
            db.collection("activities").document(activityId).set(updates, SetOptions.merge()).await()
            Log.i(TAG, "Successfully updated activity $activityId in Firestore")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to update activity in Firestore: ${e.message}")
            false
        }
    }
}
