package com.example.ouraiapp.data.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val questionDao: QuestionDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun seedIfNeeded() {
        scope.launch {
            if (questionDao.countQuestions() == 0) {
                questionDao.insertAll(QuestionSeedData.sampleQuestions())
            }
        }
    }
}
