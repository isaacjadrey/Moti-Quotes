package com.codingwithjadrey.motiquotes

import android.app.Application
import com.codingwithjadrey.motiquotes.data.database.MotiveRoomDatabase

class MotiQuoteApplication : Application() {
    val quoteDatabase: MotiveRoomDatabase by lazy { MotiveRoomDatabase.getDatabase(this) }
}