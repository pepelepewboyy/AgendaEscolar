package com.unipoli.agendaescolar

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //*******DECLARACION DE VARIABLES (PANTALLA)*******//
        val lblFecha = findViewById<TextView>(R.id.lblFecha)
        val containerClase = findViewById<LinearLayout>(R.id.containerClase)

        //*******Obtener fecha*******//
        val formato= SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val fechaActual=formato.format(Date())

        lblFecha.text = fechaActual.replaceFirstChar { it.uppercase() } //Primera letra capitalizada


        //*******CARDS******//

        //Crear la card grande de sección
        val cardSeccion = layoutInflater.inflate(R.layout.card_clases, containerClase, false)

        //Obtener el contenedor interno donde van las clases
        val containerClases = cardSeccion.findViewById<LinearLayout>(R.id.containerClases)


        //LISTA DE CLASES
        val clases = listOf(
            Triple("Calculo multivariable", "Prof. Ibarra", "8:00 AM"),
            Triple("Etica profesional", "Prof. Carmona", "9:30 AM"),
            Triple("Desarrollo de aplicaciones", "Prof. Duarte", "12:00 PM"),
            Triple("Redes", "Prof. Garcia", "2:00 PM"),
            Triple("Bases de datos", "Prof. Mendoza", "4:00 PM")
        )


        //CREAR LAS CARDS AUTOMATICAMENTE
        for (clase in clases) {

            val card = layoutInflater.inflate(R.layout.card_clase, containerClases, false)

            val titulo = card.findViewById<TextView>(R.id.txtMateria)
            val profesor = card.findViewById<TextView>(R.id.txtProfesor)
            val hora = card.findViewById<TextView>(R.id.txtHora)

            titulo.text = clase.first
            profesor.text = clase.second
            hora.text = clase.third

            containerClases.addView(card)
        }


        //Agregar la card grande a la pantalla
        containerClase.addView(cardSeccion)
    }
}