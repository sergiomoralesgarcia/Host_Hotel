package com.tfg.hosthotel.menus

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import com.tfg.hosthotel.MainActivity
import com.tfg.hosthotel.R
import java.util.*

class ConfigActivity : AppCompatActivity() {
    private var selectedLanguage: String = "en" // idioma por defecto: inglés

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        setLocale(selectedLanguage)

        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            val sp = getSharedPreferences("SP", Context.MODE_PRIVATE)
            val editor = sp.edit()
            val swi = findViewById<Switch>(R.id.switch_theme)

            if (swi.isChecked) {
                editor.putInt("Theme", 0)
            } else {
                editor.putInt("Theme", 1)
            }
            editor.apply()
            setDayNight()
            setLocale(selectedLanguage) // aplica el idioma seleccionado

            Toast.makeText(this, "Cambios aplicados", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val languageSpinner = findViewById<Spinner>(R.id.spinner_language)
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

        // Llama a setDayNight() para establecer el tema correcto al iniciar la actividad
        setDayNight()
    }

    fun setDayNight() {
        val sp = getSharedPreferences("SP", MODE_PRIVATE)
        val theme = sp.getInt("Theme", 1)
        val editor = sp.edit()

        // Si el tema es claro, marca el interruptor como no seleccionado, de lo contrario, marca el interruptor como seleccionado
        val swi = findViewById<Switch>(R.id.switch_theme)
        swi.isChecked = (theme == 0)

        if (theme == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        editor.putInt("Theme", theme)
        editor.apply()
    }

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}

