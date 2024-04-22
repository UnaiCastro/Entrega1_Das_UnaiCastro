package com.tfg.inicioactivity

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessaging
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL

class FirebaseNotificacion(
    context: Context?,
    workerParams: WorkerParameters?
) :
    Worker(context!!, workerParams!!) {
    override fun doWork(): Result {
        val token = FirebaseMessaging.getInstance().token
        Log.d("Prueba_Kotlin_Unai", "Token --> $token")

        //Se conecta con el servidor
        val direccion =
            "https://34.28.179.255/home/unaicastro/Firebase/Firebase.php"
        var urlConnection: HttpURLConnection? = null
        try {
            val destino = URL(direccion)
            urlConnection = destino.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 5000
            urlConnection!!.readTimeout = 5000

            //Rellenamos los parametros
            val parametros = "token=$token"
            urlConnection.requestMethod = "POST"
            urlConnection.doOutput = true
            urlConnection.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )

            //Enviar los parametros al php
            val out = PrintWriter(urlConnection.outputStream)
            out.print(parametros)
            //Agregar esta línea para asegurarse de que los datos se envíen correctamente
            out.flush()
            val status = urlConnection.responseCode
            Log.d("Prueba_FCM", "Status --> $status")

            //Si la respuesta es "200 OK" Entonces se realiza la recogida de datos
            if (status == 200) {
                val inputStream = BufferedInputStream(
                    urlConnection.inputStream
                )
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                var line: String
                var result = ""
                while (bufferedReader.readLine().also { line = it } != null) {
                    result += line
                }
                Log.d("Prueba_FCM", "resultado --> $result")
                inputStream.close()
                val data: Data = Data.Builder()
                    .putString("result", result)
                    .build()
                return Result.success(data)
            }
        } catch (e: Exception) {
            Log.d("DAS", "Error: $e")
        }
        return Result.failure()
    }
}

