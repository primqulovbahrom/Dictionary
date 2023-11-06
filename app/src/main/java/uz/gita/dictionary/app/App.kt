package uz.gita.dictionary.app

import android.app.Application
import uz.gita.dictionary.model.local.room.DictionaryDatabase

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        DictionaryDatabase.init(this)
    }
}