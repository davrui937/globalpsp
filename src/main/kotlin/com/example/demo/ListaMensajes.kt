package com.example.demo

import com.google.gson.Gson

class ListaMensajes() {

    var list= mutableListOf<Mensaje>()

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}