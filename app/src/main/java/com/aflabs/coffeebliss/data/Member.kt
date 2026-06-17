package com.aflabs.coffeebliss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val points: Int = 0
) {
    // Generate formatted member ID like MBR00001, MBR00123
    val formattedId: String
        get() = String.format("MBR%05d", id)

    // Status member based on points:
    // Bronze: 0-49, Silver: 50-149, Gold: 150+
    val statusTier: String
        get() = when {
            points >= 150 -> "Gold Member"
            points >= 50 -> "Silver Member"
            else -> "Bronze Member"
        }
}
