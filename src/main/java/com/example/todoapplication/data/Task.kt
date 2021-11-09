package com.example.todoapplication.data
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val title: String,
    val description: String,
    val date: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0): Parcelable {
}
