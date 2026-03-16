package com.example.ouraiapp.di

import android.content.Context
import androidx.room.Room
import com.example.ouraiapp.data.db.InterviewPrepDatabase
import com.example.ouraiapp.data.db.QuestionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideInterviewPrepDatabase(
        @ApplicationContext context: Context
    ): InterviewPrepDatabase {
        return Room.databaseBuilder(
            context,
            InterviewPrepDatabase::class.java,
            "interview_prep.db"
        ).build()
    }

    @Provides
    fun provideQuestionDao(database: InterviewPrepDatabase): QuestionDao = database.questionDao()
}
