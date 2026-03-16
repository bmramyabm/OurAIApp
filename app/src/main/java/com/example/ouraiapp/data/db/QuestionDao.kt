package com.example.ouraiapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE difficulty_level = :difficulty ORDER BY RANDOM() LIMIT :limit")
    suspend fun getQuestionsForQuiz(difficulty: String, limit: Int): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun countQuestions(): Int
}
