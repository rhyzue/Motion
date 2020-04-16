package com.rhyzue.motion.dataClasses

import java.util.*

data class Task(
    val id: Int,
    val name: String,
    val type: Int?,
    val date_assigned: Date,
    val deadline: Date?,
    val auto_push: Boolean,
    val goal_id: Int?
)