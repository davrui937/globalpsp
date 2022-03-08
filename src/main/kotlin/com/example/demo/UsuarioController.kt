package com.example.demo

import com.google.gson.Gson
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@RestController
class UsuarioController(private val usuarioRepository: UsuarioRepository, private val mensajesRepository: MensajesRepository) {
    val type = "AES/ECB/PKCS5Padding"

    @PostMapping("crearUsuario")
    fun crearUsuario(@RequestBody texto: String) : Any{

        val gson = Gson()
        val entrada= gson.fromJson(texto, Usuario::class.java)
        entrada.clave=generarkey()

        usuarioRepository.findAll().forEach{
            if(it.nombre==entrada.nombre && it.pass!=entrada.pass){
                return Error(1,"Pass invalida")
            }

            if(it.nombre==entrada.nombre && it.pass==entrada.pass){
                return it.clave
            }
        }
        usuarioRepository.save(entrada)
        return entrada.clave
    }


    @PostMapping("crearMensaje")
    fun crearMensaje(@RequestBody texto: String) : Any{
        val gson = Gson()
        val entrada= gson.fromJson(texto, Mensaje::class.java)

        usuarioRepository.findAll().forEach{
            if(it.nombre.equals(entrada.usuarioId)){
                mensajesRepository.save(entrada)
                return "Success"
            }

        }
        return Error(2,"Usuario inexistente")
    }

    @GetMapping("descargarMensajes")
    fun descargarMensajes() : ListaMensajes {
       val salida= ListaMensajes()
        mensajesRepository.findAll().forEach{
        salida.list.add(it)
       }
     return salida
    }


    @GetMapping("descargarMensajesFiltrados")
    fun descargarMensajesFiltrados(@RequestBody texto: String) : ListaMensajes {
        val salida = ListaMensajes()

        mensajesRepository.findAll().forEach{
            if(it.texto.contains(texto)){
                salida.list.add(it)
            }
        }
        return salida
    }

    @GetMapping("obtenerMensajesYLlaves")
    fun obtenerMensajesYLlaves(@RequestBody texto: String) : Any {
        val gson = Gson()
        val entrada= gson.fromJson(texto, Usuario::class.java)
        val admin= usuarioRepository.findById(1)

        if (entrada.nombre==admin.get().nombre && entrada.pass==admin.get().pass){

        val salida = ListaTodos()
        mensajesRepository.findAll().forEach{
            val aux=it
            usuarioRepository.findAll().forEach{
                if (aux.usuarioId.equals(it.nombre)){
                    salida.lista.add(SalidaE(aux,it.clave))
                }
            }
        }
        return salida}
        return Error(3,"Pass de administrador incorrecta")
    }

    @GetMapping("obtenerMensajesDescifrados")
    fun obtenerMensajesDescifrados(@RequestBody texto: String) : Any {
        val gson = Gson()
        val entrada= gson.fromJson(texto, Usuario::class.java)
        val admin= usuarioRepository.findById(1)

        if (entrada.nombre==admin.get().nombre && entrada.pass==admin.get().pass){
            val salida = ListaDescencriptado()
            mensajesRepository.findAll().forEach{
                val aux=it
                usuarioRepository.findAll().forEach{
                    if (aux.usuarioId.equals(it.nombre)){
                        val des=descifrar(aux.texto,it.clave)
                        salida.lista.add(Salida2(it.nombre,des))
                    }
                }
            }
            return salida}
        return Error(3,"Pass de administrador incorrecta")
    }






    fun generarkey(): String {
        val lista = '0'..'9'
        var aux=""
        var cont=0
        do {
            val generar = lista.random()
            aux += generar
            cont++
        }while (cont < 20)
        return aux
    }

    private fun cifrar(textoEnString : String, llaveEnString : String) : String {
        println("Voy a cifrar: $textoEnString")
        val cipher = Cipher.getInstance(type)
        cipher.init(Cipher.ENCRYPT_MODE, getKey(llaveEnString))
        val textCifrado = cipher.doFinal(textoEnString.toByteArray(Charsets.UTF_8))
        println("Texto cifrado $textCifrado")
        val textCifradoYEncodado = Base64.getEncoder().encodeToString(textCifrado)
        println("Texto cifrado y encodado $textCifradoYEncodado")
        return textCifradoYEncodado
    }

    private fun descifrar(textoCifradoYEncodado : String, llaveEnString : String) : String {
        println("Voy a descifrar $textoCifradoYEncodado")
        val cipher = Cipher.getInstance(type)
        cipher.init(Cipher.DECRYPT_MODE, getKey(llaveEnString))
        val textCifradoYDencodado = Base64.getUrlDecoder().decode(textoCifradoYEncodado)
        println("Texto cifrado $textCifradoYDencodado")
        val textDescifradoYDesencodado = String(cipher.doFinal(textCifradoYDencodado))
        println("Texto cifrado y desencodado $textDescifradoYDesencodado")
        return textDescifradoYDesencodado
    }

    private fun getKey(llaveEnString : String): SecretKeySpec {
        var llaveUtf8 = llaveEnString.toByteArray(Charsets.UTF_8)
        val sha = MessageDigest.getInstance("SHA-1")
        llaveUtf8 = sha.digest(llaveUtf8)
        llaveUtf8 = llaveUtf8.copyOf(16)
        return SecretKeySpec(llaveUtf8, "AES")
    }
}