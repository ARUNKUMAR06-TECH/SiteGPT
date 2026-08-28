package com.example.model

enum class UserRole(
    val title: String,
    val badgeLabel: String,
    val description: String
) {
    SITE_ENGINEER(
        title = "Site Engineer",
        badgeLabel = "Field Execution",
        description = "Direct access to Field Capture, AI Activity Matching, and Schedule verification."
    ),
    PLANNING_ENGINEER(
        title = "Planning Engineer",
        badgeLabel = "Schedule & Controls",
        description = "Oversees schedule baseline, validates AI matches, audits progress, and tracks variance."
    ),
    MANAGEMENT(
        title = "Management",
        badgeLabel = "Executive Insights",
        description = "Strategic overview of project health, critical path risks, and AI predictive recommendations."
    )
}
