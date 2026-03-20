package com.unipoli.agendaescolar

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private data class ClaseAgenda(
        val nombre: String,
        val docente: String,
        val dias: String,
        val horario: String
    )

    private val clasesAgenda = mutableListOf(
        ClaseAgenda("Calculo multivariable", "Prof. Ibarra", "Lunes y Miércoles", "8:00 AM - 9:30 AM"),
        ClaseAgenda("Etica profesional", "Prof. Carmona", "Martes", "9:30 AM - 11:00 AM"),
        ClaseAgenda("Desarrollo de aplicaciones", "Prof. Duarte", "Jueves", "12:00 PM - 2:00 PM"),
        ClaseAgenda("Redes", "Prof. Garcia", "Viernes", "2:00 PM - 3:30 PM"),
        ClaseAgenda("Bases de datos", "Prof. Mendoza", "Lunes", "4:00 PM - 6:00 PM")
    )

    private var agendaContainer: LinearLayout? = null

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

        val lblFecha = findViewById<TextView>(R.id.lblFecha)
        val containerClase = findViewById<LinearLayout>(R.id.containerClase)

        val formato = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val fechaActual = formato.format(Date())
        lblFecha.text = fechaActual.replaceFirstChar { it.uppercase() }

        if (containerClase != null) {
            cargarRecordatorios(containerClase)
            cargarClasesEn(containerClase)
        }

        val btnInicio = findViewById<LinearLayout>(R.id.btnInicio)
        val btnAgenda = findViewById<LinearLayout>(R.id.btnAgenda)

        btnAgenda?.setOnClickListener {
            if (containerClase != null) {
                containerClase.removeAllViews()

                val viewAgenda = layoutInflater.inflate(R.layout.layout_agenda, containerClase, false)
                containerClase.addView(viewAgenda)

                agendaContainer = viewAgenda.findViewById(R.id.dynamicContainer)
                refrescarListaAgenda()

                val fabAgregarClase = viewAgenda.findViewById<FloatingActionButton>(R.id.fabAddClase)
                fabAgregarClase?.setOnClickListener { mostrarModalRegistroClase() }
            }
        }

        btnInicio?.setOnClickListener {
            recreate()
        }
    }

    private fun refrescarListaAgenda() {
        agendaContainer?.let { dynamicContainer ->
            dynamicContainer.removeAllViews()
            cargarClasesEn(dynamicContainer)
        }
    }

    private fun mostrarModalRegistroClase() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_registrar_clase, null)
        val inputNombreClase = dialogView.findViewById<EditText>(R.id.inputNombreClase)
        val inputDocente = dialogView.findViewById<EditText>(R.id.inputDocente)
        val inputDias = dialogView.findViewById<EditText>(R.id.inputDias)
        val inputHorario = dialogView.findViewById<EditText>(R.id.inputHorario)

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_registrar_clase_title)
            .setView(dialogView)
            .setPositiveButton(R.string.dialog_guardar) { _, _ ->
                val nombre = inputNombreClase.text.toString().trim()
                val docente = inputDocente.text.toString().trim()
                val dias = inputDias.text.toString().trim()
                val horario = inputHorario.text.toString().trim()

                if (nombre.isBlank() || docente.isBlank() || dias.isBlank() || horario.isBlank()) {
                    Toast.makeText(this, R.string.error_campos_obligatorios, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                clasesAgenda.add(
                    ClaseAgenda(
                        nombre = nombre,
                        docente = docente,
                        dias = dias,
                        horario = horario
                    )
                )

                refrescarListaAgenda()
            }
            .setNegativeButton(R.string.dialog_cancelar, null)
            .show()
    }

    private fun cargarClasesEn(contenedorDestino: LinearLayout) {
        val cardSeccion = layoutInflater.inflate(R.layout.card_clases, contenedorDestino, false)
        val containerClases = cardSeccion.findViewById<LinearLayout>(R.id.containerClases)

        if (containerClases != null) {
            for (clase in clasesAgenda) {
                val card = layoutInflater.inflate(R.layout.card_clase, containerClases, false)
                card.findViewById<TextView>(R.id.txtMateria)?.text = clase.nombre
                card.findViewById<TextView>(R.id.txtProfesor)?.text = "${clase.docente} • ${clase.dias}"
                card.findViewById<TextView>(R.id.txtHora)?.text = clase.horario
                containerClases.addView(card)
            }
        }
        contenedorDestino.addView(cardSeccion)
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
}
