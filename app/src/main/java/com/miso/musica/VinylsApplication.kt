package com.miso.musica

import android.app.Application
import com.miso.musica.database.VinylRoomDatabase

class VinylsApplication: Application()  {
    val database by lazy { VinylRoomDatabase.getDatabase(this) }
}