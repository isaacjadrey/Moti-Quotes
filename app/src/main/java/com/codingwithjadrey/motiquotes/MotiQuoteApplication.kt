package com.codingwithjadrey.motiquotes

import android.app.Application
import com.codingwithjadrey.motiquotes.data.MotiveRoomDatabase

class MotiQuoteApplication : Application() {
    val quoteDatabase: MotiveRoomDatabase by lazy { MotiveRoomDatabase.getDatabase(this) }
}