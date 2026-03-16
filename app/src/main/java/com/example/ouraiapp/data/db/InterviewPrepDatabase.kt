package com.example.ouraiapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [QuestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class InterviewPrepDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}
