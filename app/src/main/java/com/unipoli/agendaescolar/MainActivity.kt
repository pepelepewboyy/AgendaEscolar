package com.unipoli.agendaescolar

import com.unipoli.agendaescolar.data.Recordatorio
import com.unipoli.agendaescolar.data.Clase


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button

import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.CheckBox


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color


import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unipoli.agendaescolar.data.AppDatabase
import com.unipoli.agendaescolar.data.Horario
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

            // 1. Limpiar el contenedor principal
            containerClase?.removeAllViews()

            // 2. Inflar el layout de agenda (SIN attach todavía)
            val viewAgenda = layoutInflater.inflate(
                R.layout.layout_agenda,
                containerClase,
                false
            )

            // 3. Agregarlo al contenedor principal
            containerClase?.addView(viewAgenda)

            // 4. Obtener el contenedor dinámico DESDE viewAgenda
            val dynamicContainer = viewAgenda.findViewById<LinearLayout>(R.id.dynamicContainer)
                val dynamicContainer = viewAgenda.findViewById<LinearLayout>(R.id.dynamicContainer)
                val btnPickerFecha = viewAgenda.findViewById<TextView>(R.id.btnPickerFecha)


            // 5. Validar que sí existe
            if (dynamicContainer == null) {
                Toast.makeText(this, "Error: dynamicContainer no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 6. Limpiar por si acaso
            dynamicContainer.removeAllViews()

            // 7. Inflar la card (IMPORTANTE: parent = dynamicContainer)
            val cardAgenda = layoutInflater.inflate(
                R.layout.card_clases_agenda,
                dynamicContainer,
                false
            )

            // 8. Agregar la card al contenedor dinámico
            containerClase.addView(cardAgenda)

            // Abrir dialogo en el boton de agregar horario
            val btnHorario = viewAgenda.findViewById<FloatingActionButton>(R.id.btnAddHorario)
            btnHorario?.setOnClickListener {
                abrirDialogoHorario()
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

            //Abrir calendario en el picker
            val btnPickerFecha = viewAgenda.findViewById<TextView>(R.id.btnPickerFecha)
            btnPickerFecha?.setOnClickListener {
                val calendar = Calendar.getInstance()

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePicker = DatePickerDialog(
                    this,
                    { _, selectedYear, selectedMonth, selectedDay ->

                        // ⚠️ El mes empieza en 0, por eso se suma 1
                        val fechaSeleccionada = String.format(
                            "%02d/%02d/%04d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear
                        )

                        btnPickerFecha.text = fechaSeleccionada

                    },
                    year,
                    month,
                    day
                )

                datePicker.show()
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
        val btnAddClase = cardSeccion.findViewById<TextView>(R.id.btnAddClase)

        btnAddClase.setOnClickListener {
            abrirDialogoClases()
        }

        val db = AppDatabase.getDatabase(this)
        val horarioDao = db.horarioDao()

        val diaActual = obtenerDiaActual() // 🔥 AQUÍ

        lifecycleScope.launch {

            val lista = horarioDao.getClasesPorDia(diaActual)

            containerClases.removeAllViews()

            if (lista.isEmpty()) {
                val txt = TextView(this@MainActivity)
                txt.text = "No hay clases para hoy ($diaActual)"
                containerClases.addView(txt)
                return@launch
            }

            lista.forEach { clase ->

                val card = layoutInflater.inflate(R.layout.card_clase, containerClases, false)

                card.findViewById<TextView>(R.id.txtMateria).text = clase.titulo
                card.findViewById<TextView>(R.id.txtProfesor).text = clase.profesor
                card.findViewById<TextView>(R.id.txtHora).text =
                    "${clase.horaInicio} - ${clase.horaFin}"

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

        val cardRecordatorios = layoutInflater.inflate(
            R.layout.card_recordatorios,
            contenedorDestino,
            false
        )

        val containerRecordatorios = cardRecordatorios.findViewById<LinearLayout>(R.id.containerRecordatorios)
        val btnAddRecordatorio = cardRecordatorios.findViewById<TextView>(R.id.btnAddRecordatorio)

        contenedorDestino.removeAllViews()
        contenedorDestino.addView(cardRecordatorios)
        val db = AppDatabase.getDatabase(this)
        val dao = db.recordatorioDao()
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
    private fun abrirDialogoClases(){
        val db = AppDatabase.getDatabase(this)
        val dao = db.clasesDao()

        val dialogView = layoutInflater.inflate(R.layout.dialog_registrar_clase, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Registrar Clase")
            .setView(dialogView)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()

        val edtMateria = dialogView.findViewById<EditText>(R.id.inputNombreClase)
        val edtDocente = dialogView.findViewById<EditText>(R.id.inputDocente)

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val docente = edtDocente.text.toString().trim()
            val clase = edtMateria.text.toString().trim()

            if (clase.isNotEmpty() && docente.isNotEmpty()) {
                val newclase = Clase(
                    titulo = clase,
                    profesor = docente
                )
                lifecycleScope.launch {
                    dao.insertclase(newclase)
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

    private fun abrirDialogoHorario() {

        val db = AppDatabase.getDatabase(this)
        val daoClase = db.clasesDao()
        val daoHorario = db.horarioDao()

        val dialogView = layoutInflater.inflate(R.layout.dialog_registrar_horario, null)

        val spinner = dialogView.findViewById<Spinner>(R.id.spnClases)
        val inputInicio = dialogView.findViewById<EditText>(R.id.inputHorarioInicio)
        val inputFin = dialogView.findViewById<EditText>(R.id.inputHorarioFin)

        val cbLunes = dialogView.findViewById<CheckBox>(R.id.cbLunes)
        val cbMartes = dialogView.findViewById<CheckBox>(R.id.cbMartes)
        val cbMiercoles = dialogView.findViewById<CheckBox>(R.id.cbMiercoles)
        val cbJueves = dialogView.findViewById<CheckBox>(R.id.cbJueves)
        val cbViernes = dialogView.findViewById<CheckBox>(R.id.cbViernes)
        val cbSabado = dialogView.findViewById<CheckBox>(R.id.cbSabado)

        // 🔒 bloquear escritura manual
        inputInicio.inputType = 0
        inputInicio.isFocusable = false

        inputFin.inputType = 0
        inputFin.isFocusable = false

        val calendar = Calendar.getInstance()

        // ⏰ TIME PICKER INICIO
        inputInicio.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    inputInicio.setText(String.format("%02d:%02d", hour, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        // ⏰ TIME PICKER FIN
        inputFin.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    inputFin.setText(String.format("%02d:%02d", hour, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        // 🔥 cargar clases
        lifecycleScope.launch {
            val lista = daoClase.getAllClases()

            if (lista.isEmpty()) {
                Toast.makeText(this@MainActivity, "No hay clases registradas", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_item,
                lista
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Registrar Horario")
            .setView(dialogView)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()

        // 🔥 función segura
        fun toMinutos(hora: String): Int? {
            return try {
                val partes = hora.split(":")
                if (partes.size != 2) return null
                partes[0].toInt() * 60 + partes[1].toInt()
            } catch (e: Exception) {
                null
            }
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

            val claseSeleccionada = spinner.selectedItem as? Clase
            if (claseSeleccionada == null) {
                Toast.makeText(this, "Selecciona una clase", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val horaInicio = inputInicio.text.toString().trim()
            val horaFin = inputFin.text.toString().trim()

            if (horaInicio.isEmpty() || horaFin.isEmpty()) {
                Toast.makeText(this, "Selecciona horario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val inicioMin = toMinutos(horaInicio)
            val finMin = toMinutos(horaFin)

            if (inicioMin == null || finMin == null) {
                Toast.makeText(this, "Formato de hora inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (finMin <= inicioMin) {
                Toast.makeText(this, "La hora fin debe ser mayor que inicio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dias = mutableListOf<String>()
            if (cbLunes.isChecked) dias.add("Lunes")
            if (cbMartes.isChecked) dias.add("Martes")
            if (cbMiercoles.isChecked) dias.add("Miércoles")
            if (cbJueves.isChecked) dias.add("Jueves")
            if (cbViernes.isChecked) dias.add("Viernes")
            if (cbSabado.isChecked) dias.add("Sábado")

            if (dias.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un día", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {

                for (dia in dias) {

                    val existentes = daoHorario.getHorariosPorDia(dia)

                    val conflicto = existentes.any {
                        val iniExist = toMinutos(it.horaInicio) ?: return@any false
                        val finExist = toMinutos(it.horaFin) ?: return@any false

                        inicioMin < finExist && finMin > iniExist
                    }

                    if (conflicto) {
                        Toast.makeText(
                            this@MainActivity,
                            "Conflicto de horario en $dia",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }

                    val horario = Horario(
                        idClase = claseSeleccionada.id,
                        dia = dia,
                        horaInicio = horaInicio,
                        horaFin = horaFin
                    )

                    daoHorario.insertHorario(horario)
                }

                Toast.makeText(this@MainActivity, "Horario guardado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    private fun obtenerDiaActual(): String {
        val dias = arrayOf("Domingo","Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val calendar = Calendar.getInstance()
        return dias[calendar.get(Calendar.DAY_OF_WEEK)-1]
    }
    private fun obtenerDiaDesdeFecha(calendar: Calendar): String {
        val formato = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val dia = formato.format(calendar.time)
        return dia.replaceFirstChar { it.uppercase() }
    }
    private fun cargarClasesPorDia(contenedorDestino: LinearLayout, dia: String) {

        val cardSeccion = layoutInflater.inflate(R.layout.card_clases, contenedorDestino, false)
        val containerClases = cardSeccion.findViewById<LinearLayout>(R.id.containerClases)

        val db = AppDatabase.getDatabase(this)
        val horarioDao = db.horarioDao()

        contenedorDestino.removeAllViews()

        lifecycleScope.launch {

            val lista = horarioDao.getClasesPorDia(dia)

            containerClases.removeAllViews()

            if (lista.isEmpty()) {
                val txt = TextView(this@MainActivity)
                txt.text = "No hay clases para $dia"
                containerClases.addView(txt)
            } else {
                lista.forEach { clase ->

                    val card = layoutInflater.inflate(R.layout.card_clase, containerClases, false)

                    card.findViewById<TextView>(R.id.txtMateria).text = clase.titulo
                    card.findViewById<TextView>(R.id.txtProfesor).text = clase.profesor
                    card.findViewById<TextView>(R.id.txtHora).text =
                        "${clase.horaInicio} - ${clase.horaFin}"

                    containerClases.addView(card)
                }
            }
        }

        contenedorDestino.addView(cardSeccion)
    }


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