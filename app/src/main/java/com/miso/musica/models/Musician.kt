package com.miso.musica.models

import java.util.Date

data class Musician(
    val musicianId:Int,
    val name:String,
    val image:String,
    val description:String,
    val birthDate: String,
    val albums: List<Album>
)
