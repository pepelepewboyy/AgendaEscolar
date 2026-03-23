package com.unipoli.agendaescolar

import com.unipoli.agendaescolar.data.RecordatorioRoomDatabase
import com.unipoli.agendaescolar.data.Recordatorio
import com.unipoli.agendaescolar.data.RecordatorioDao

import android.annotation.SuppressLint
import android.os.Bundle

import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ArrayAdapter
import android.widget.Spinner


import android.app.DatePickerDialog
import android.app.TimePickerDialog


import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

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

        //*******DECLARACION DE VARIABLES (PANTALLA)*******//
        val lblFecha = findViewById<TextView>(R.id.lblFecha)
        val containerClase = findViewById<LinearLayout>(R.id.containerClase)

        //*******Obtener fecha*******//
        val formato = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val fechaActual = formato.format(Date())
        lblFecha.text = fechaActual.replaceFirstChar { it.uppercase() }

        //*******CARGAR CONTENIDO INICIAL*******//
        if (containerClase != null) {
            cargarRecordatorios(containerClase)
            cargarClasesEn(containerClase)
        }

        //*******NAVEGACIÓN*******//
        val btnInicio = findViewById<LinearLayout>(R.id.btnInicio)
        val btnAgenda = findViewById<LinearLayout>(R.id.btnAgenda)

        btnAgenda?.setOnClickListener {
            if (containerClase != null) {
                containerClase.removeAllViews()

                val viewAgenda = layoutInflater.inflate(R.layout.layout_agenda, containerClase, false)
                containerClase.addView(viewAgenda)

                val dynamicContainer = viewAgenda.findViewById<LinearLayout>(R.id.dynamicContainer)

                if (dynamicContainer != null) {
                    cargarClasesEn(dynamicContainer)
                }
            }
        }

        btnInicio?.setOnClickListener {
             recreate() 
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
    private fun cargarRecordatorios(contenedorDestino: LinearLayout) {

        val cardRecordatorios = layoutInflater.inflate(
            R.layout.card_recordatorios,
            contenedorDestino,
            false
        )

        val containerRecordatorios = cardRecordatorios.findViewById<LinearLayout>(R.id.containerRecordatorios)
        val btnAddRecordatorio = cardRecordatorios.findViewById<TextView>(R.id.btnAddRecordatorio)

        contenedorDestino.removeAllViews()
        contenedorDestino.addView(cardRecordatorios)

        val db = RecordatorioRoomDatabase.getDatabase(this)
        val dao = db.recordatorioDao()

        // 🔥 FUNCIÓN INTERNA PARA RENDERIZAR
        fun render(lista: List<Recordatorio>) {
            containerRecordatorios.removeAllViews()

            lista.forEach { recordatorio ->
                val card = layoutInflater.inflate(
                    R.layout.card_recordatorio,
                    containerRecordatorios,
                    false
                )

                card.findViewById<TextView>(R.id.txtTituloRecordatorio).text = recordatorio.titulo
                card.findViewById<TextView>(R.id.txtFechaRecordatorio).text = recordatorio.fecha
                card.findViewById<TextView>(R.id.txtHoraRecordatorio).text = recordatorio.hora

                containerRecordatorios.addView(card)
            }
        }

        // 🔥 CARGA INICIAL
        lifecycleScope.launch {
            val lista = dao.getAllRecordatorios()
            render(lista)
        }

        // 🔥 BOTÓN
        btnAddRecordatorio.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.dialog_registrar_recordatorio, null)

            val edtTitulo = dialogView.findViewById<EditText>(R.id.inputTituloRecordatorio)
            val edtHora = dialogView.findViewById<EditText>(R.id.inputHoraRecordatorio)
            val edtFecha = dialogView.findViewById<EditText>(R.id.inputFechaRecordatorio)
            val edtPrioridad = dialogView.findViewById<Spinner>(R.id.inputPrioridad)

            val calendar = Calendar.getInstance()

            edtFecha.inputType = 0
            edtFecha.isFocusable = false
            edtHora.inputType = 0
            edtHora.isFocusable = false

            edtFecha.setOnClickListener {
                val datePicker = DatePickerDialog(
                    this,
                    { _, year, month, day ->
                        val fecha = String.format("%02d/%02d/%04d", day, month + 1, year)
                        edtFecha.setText(fecha)
                        calendar.set(year, month, day)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }

            edtHora.setOnClickListener {
                val timePicker = TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        val hora = String.format("%02d:%02d", hour, minute)
                        edtHora.setText(hora)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePicker.show()
            }

            val opciones = listOf("Selecciona la prioridad", "Alta", "Media", "Baja")
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            edtPrioridad.adapter = adapter

            val dialog = AlertDialog.Builder(this)
                .setTitle("Registrar Recordatorio")
                .setView(dialogView)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create()

            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                val titulo = edtTitulo.text.toString().trim()
                val hora = edtHora.text.toString().trim()
                val fecha = edtFecha.text.toString().trim()
                val prioridad = edtPrioridad.selectedItem.toString()

                if (titulo.isNotEmpty() && hora.isNotEmpty() && fecha.isNotEmpty() && edtPrioridad.selectedItemPosition > 0) {

                    val recordatorio = Recordatorio(
                        titulo = titulo,
                        fecha = fecha,
                        hora = hora,
                        prioridad = prioridad
                    )

                    lifecycleScope.launch {
                        dao.insertRecordatorio(recordatorio)

                        // 🔥 RECARGAR DESDE BD
                        val lista = dao.getAllRecordatorios()
                        render(lista)
                    }

                    dialog.dismiss()

                } else {
                    Toast.makeText(
                        this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}