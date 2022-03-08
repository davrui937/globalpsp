package com.example.demo

import com.google.gson.Gson

data class SalidaE(var mensaje:Mensaje, var clave:String) {

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}

data class Salida2(var usuarioId:String, var texto:String) {

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}