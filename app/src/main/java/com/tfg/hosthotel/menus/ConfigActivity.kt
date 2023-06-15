package com.tfg.hosthotel.menus

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.tfg.hosthotel.MainActivity
import com.tfg.hosthotel.R
import java.util.*

class ConfigActivity : AppCompatActivity() {

    private lateinit var languageSpinner: Spinner
    private var selectedLanguage: String = "en" // idioma por defecto: inglés

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        // Inicializa el Spinner
        languageSpinner = findViewById(R.id.spinner_language)

        // Obtiene el idioma seleccionado guardado en las preferencias compartidas
        val sp = getSharedPreferences("SP", Context.MODE_PRIVATE)
        selectedLanguage = sp.getString("Language", "en") ?: "en"

        // Establece la selección del Spinner según el idioma guardado
        val languageIndex = when (selectedLanguage) {
            "es" -> 1 // Español
            else -> 0 // Inglés (por defecto)
        }
        languageSpinner.setSelection(languageIndex)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view != null) {
                    selectedLanguage = when (position) {
                        0 -> "en" // inglés
                        1 -> "es" // español
                        else -> "en" // idioma por defecto
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            val swi = findViewById<Switch>(R.id.switch_theme)
            val theme = if (swi.isChecked) 0 else 1

            saveTheme(theme)
            saveLanguage(selectedLanguage)

            Toast.makeText(this,R.string.txt_chang_applied, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        // Llama a setDayNight() para establecer el tema correcto al iniciar la actividad
        setDayNight()
    }

    override fun onResume() {
        super.onResume()
        // Restaura el tema y el idioma al volver a abrir la actividad
        setDayNight()
        setLocale(selectedLanguage)
    }

    override fun onPause() {
        super.onPause()
        // Guarda el idioma seleccionado en las preferencias compartidas
        saveLanguage(selectedLanguage)
    }

    private fun saveTheme(theme: Int) {
        val sp = getSharedPreferences("SP", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putInt("Theme", theme)
        editor.apply()
        setDayNight()
    }

    private fun setDayNight() {
        val sp = getSharedPreferences("SP", MODE_PRIVATE)
        val theme = sp.getInt("Theme", 1)

        // Si el tema es claro, marca el interruptor como no seleccionado, de lo contrario, marca el interruptor como seleccionado
        val swi = findViewById<Switch>(R.id.switch_theme)
        swi.isChecked = (theme == 0)

        val nightMode = when (theme) {
            0 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private fun saveLanguage(languageCode: String) {
        val sp = getSharedPreferences("SP", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("Language", languageCode)
        editor.apply()
        setLocale(languageCode)
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}
