package com.unipoli.agendaescolar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainContainer = findViewById<ConstraintLayout>(R.id.main_layout) 
        
        if (mainContainer != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainContainer) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        val containerClase = findViewById<LinearLayout>(R.id.containerClase)


        if (containerClase != null) {
            cargarTextoAgenda(containerClase)
            cargarRecordatorios(containerClase)
            cargarClasesEn(containerClase)
        }


        val btnInicio = findViewById<LinearLayout>(R.id.btnInicio)
        val btnAgenda = findViewById<LinearLayout>(R.id.btnAgenda)
        val btnMensajes = findViewById<LinearLayout>(R.id.btnMensajes)
        val btnAsistencia = findViewById<LinearLayout>(R.id.btnAsistencia)
        val btnConfig = findViewById<LinearLayout>(R.id.btnConfig)

        btnMensajes?.setOnClickListener {
            containerClase?.removeAllViews()
            val view = layoutInflater.inflate(R.layout.layout_mensajes, containerClase, false)
            containerClase?.addView(view)
            val dynamicContainer = view.findViewById<LinearLayout>(R.id.dynamicContainerMensajes)
            if (dynamicContainer != null) cargarMensajes(dynamicContainer)
        }

        btnAsistencia?.setOnClickListener {
            containerClase?.removeAllViews()
            val view = layoutInflater.inflate(R.layout.layout_asistencia, containerClase, false)
            containerClase?.addView(view)


            val btnPickerHistorial = view.findViewById<TextView>(R.id.btnPickerHistorial)
            btnPickerHistorial?.setOnClickListener {
                val calendar = Calendar.getInstance()
                DatePickerDialog(this, { _, anio, mes, dia ->
                    btnPickerHistorial.text = "%02d/%02d/%d".format(dia, mes + 1, anio)

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }


            val btnPresente = view.findViewById<Button>(R.id.btnPresente)
            val btnAusente = view.findViewById<Button>(R.id.btnAusente)

            btnPresente?.setOnClickListener {
                btnPresente.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.BLACK)
                btnAusente.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#EEEEEE"))
            }

            btnAusente?.setOnClickListener {
                btnAusente.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.BLACK)
                btnAusente.setTextColor(android.graphics.Color.WHITE)
                btnPresente.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#EEEEEE"))
                btnPresente.setTextColor(android.graphics.Color.BLACK)
            }
        }

        btnConfig?.setOnClickListener {
            containerClase?.removeAllViews()
            val view = layoutInflater.inflate(R.layout.layout_configuracion, containerClase, false)
            containerClase?.addView(view)

            val switchNotificaciones = view.findViewById<Switch>(R.id.switchNotificaciones)
            val switchSonidos = view.findViewById<Switch>(R.id.switchSonidos)
            val switchTema = view.findViewById<Switch>(R.id.switchTema)

            switchNotificaciones?.setOnCheckedChangeListener { _, isChecked ->

            }

            switchSonidos?.setOnCheckedChangeListener { _, isChecked ->

            }

            switchTema?.setOnCheckedChangeListener { _, isChecked ->

            }

            view.findViewById<Button>(R.id.btnCerrarSesion)?.setOnClickListener {

            }
        }

        btnAgenda?.setOnClickListener {
            if (containerClase != null) {
                containerClase.removeAllViews()

                val viewAgenda = layoutInflater.inflate(R.layout.layout_agenda, containerClase, false)
                containerClase.addView(viewAgenda)

                val dynamicContainer = viewAgenda.findViewById<LinearLayout>(R.id.dynamicContainer)
                val btnPickerFecha = viewAgenda.findViewById<TextView>(R.id.btnPickerFecha)


                if (dynamicContainer != null) {
                    cargarClasesEn(dynamicContainer)
                }


                btnPickerFecha?.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val anio = calendar.get(Calendar.YEAR)
                    val mes = calendar.get(Calendar.MONTH)
                    val dia = calendar.get(Calendar.DAY_OF_MONTH)

                    DatePickerDialog(this, { _, anioSel, mesSel, diaSel ->

                        val fechaFormato = "%02d/%02d/%d".format(diaSel, mesSel + 1, anioSel)
                        btnPickerFecha.text = fechaFormato


                        dynamicContainer?.removeAllViews()
                        cargarClasesEn(dynamicContainer!!)

                    }, anio, mes, dia).show()
                }
            }
        }

        btnInicio?.setOnClickListener {
            containerClase.removeAllViews()
            cargarTextoAgenda(containerClase)
            cargarRecordatorios(containerClase)
            cargarClasesEn(containerClase)
        }
    }

    private fun cargarClasesEn(contenedorDestino: LinearLayout) {
        val cardSeccion = layoutInflater.inflate(R.layout.card_clases, contenedorDestino, false)
        val containerClases = cardSeccion.findViewById<LinearLayout>(R.id.containerClases)

        val clases = listOf(
            Triple("Calculo multivariable", "Prof. Ibarra", "8:00 AM"),
            Triple("Etica profesional", "Prof. Carmona", "9:30 AM"),
            Triple("Desarrollo de aplicaciones", "Prof. Duarte", "12:00 PM"),
            Triple("Redes", "Prof. Garcia", "2:00 PM"),
            Triple("Bases de datos", "Prof. Mendoza", "4:00 PM")
        )

        if (containerClases != null) {
            for (clase in clases) {
                val card = layoutInflater.inflate(R.layout.card_clase, containerClases, false)
                card.findViewById<TextView>(R.id.txtMateria)?.text = clase.first
                card.findViewById<TextView>(R.id.txtProfesor)?.text = clase.second
                card.findViewById<TextView>(R.id.txtHora)?.text = clase.third
                containerClases.addView(card)
            }
        }
        contenedorDestino.addView(cardSeccion)
    }

    private fun cargarTextoAgenda(contenedorDestino: LinearLayout) {
        val cardTextoAgenda = layoutInflater.inflate(
            R.layout.card_text_agenda, contenedorDestino, false
        )

        val lblFecha = cardTextoAgenda.findViewById<TextView>(R.id.lblFecha)
        val formato = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val fechaActual = formato.format(Date())
        lblFecha?.text = fechaActual.replaceFirstChar { it.uppercase() }

        contenedorDestino.addView(cardTextoAgenda)
    }

    private fun cargarRecordatorios(contenedorDestino: LinearLayout) {
        val cardRecordatorios = layoutInflater.inflate(R.layout.card_recordatorios, contenedorDestino, false)
        val containerRecordatorios = cardRecordatorios.findViewById<LinearLayout>(R.id.containerRecordatorios)

        val recordatorios = listOf(
            Pair("Recordatorio 1", "12:00 PM"),
            Pair("Recordatorio 2", "1:00 PM"),
            Pair("Recordatorio 3", "2:00 PM"),
            Pair("Recordatorio 4", "3:00 PM")
        )

        if (containerRecordatorios != null) {
            for (recordatorio in recordatorios) {
                val card = layoutInflater.inflate(R.layout.card_recordatorio, containerRecordatorios, false)
                card.findViewById<TextView>(R.id.txtTituloRecordatorio)?.text = recordatorio.first
                card.findViewById<TextView>(R.id.txtFechaRecordatorio)?.text = recordatorio.second
                containerRecordatorios.addView(card)
            }
        }
        contenedorDestino.addView(cardRecordatorios)
    }
    private fun cargarMensajes(contenedorDestino: LinearLayout) {
        val mensajes = listOf(
            listOf("Reunión de padres de familia", "Importante", "Se les convoca a la reunión de padres de familia el próximo viernes 6 de octubre a las 4:00 PM en el aula múltiple. Es importante su asistencia.", "Domingo, 5 de octubre de 2025  8:00", true),
            listOf("Día de uniforme deportivo", "Informado", "Recuerden que el lunes 7 de septiembre es día de uniforme deportivo para todas las actividades escolares.", "Domingo, 5 de septiembre de 2025  17:54", false),
            listOf("Suspensión de clases", "Urgente", "Debido a las condiciones climáticas, las clases del martes 5 de agosto quedan suspendidas. Se retomarán el miércoles normalmente.", "Viernes, 1 de agosto de 2025  16:58", false),
            listOf("Proyecto de ciencias", "Informado", "Los estudiantes de 3ro grado deben entregar el proyecto de ciencias naturales antes del viernes. Pueden consultar dudas con su maestro.", "Jueves, 25 de julio de 2025  19:34", false),
            listOf("Celebración día del maestro", "Evento", "El próximo viernes celebraremos el día del maestro con actividades especiales. Los estudiantes pueden venir con ropa casual.", "Viernes, 6 de mayo de 2025  14:56", false)
        )

        for (mensaje in mensajes) {
            val card = layoutInflater.inflate(R.layout.card_mensaje, contenedorDestino, false)
            card.findViewById<TextView>(R.id.txtTituloMensaje)?.text = mensaje[0] as String
            card.findViewById<TextView>(R.id.txtBadgeMensaje)?.text = mensaje[1] as String
            card.findViewById<TextView>(R.id.txtDescripcionMensaje)?.text = mensaje[2] as String
            card.findViewById<TextView>(R.id.txtFechaMensaje)?.text = mensaje[3] as String


            val noLeido = mensaje[4] as Boolean
            card.findViewById<View>(R.id.indicadorNoLeido)?.visibility =
                if (noLeido) View.VISIBLE else View.GONE

            contenedorDestino.addView(card)
        }
    }
}