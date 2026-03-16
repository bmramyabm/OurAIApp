package com.example.ouraiapp

import android.app.Application
import com.example.ouraiapp.data.db.DatabaseSeeder
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class InterviewPrepApplication : Application() {

    @Inject
    lateinit var databaseSeeder: DatabaseSeeder

    override fun onCreate() {
        super.onCreate()
        databaseSeeder.seedIfNeeded()
    }
}
