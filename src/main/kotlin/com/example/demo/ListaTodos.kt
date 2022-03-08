package com.example.demo

import com.google.gson.Gson

class ListaTodos {
    var lista = mutableListOf<SalidaE>()


    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}