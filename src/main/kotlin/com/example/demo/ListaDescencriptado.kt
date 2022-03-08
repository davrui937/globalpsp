package com.example.demo

import com.google.gson.Gson

class ListaDescencriptado {

    var lista = mutableListOf<Salida2>()


    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}