package com.tfg.hosthotel

import AddFragment
import HomeFragment
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tfg.hosthotel.fragments.ProfileFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Busca la vista de la navegación inferior en el diseño y la asigna a la propiedad
        bottomNavigationView = findViewById(R.id.bottom_nav)

        // Define un listener de clic para los elementos de la navegación inferior
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Reemplaza el fragmento actual por el HomeFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    true
                }
                R.id.menu_add -> {
                    // Reemplaza el fragmento actual por el AddFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AddFragment())
                        .commit()
                    true
                }
                R.id.menu_profile -> {
                    // Reemplaza el fragmento actual por el ProfileFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        // Establece el elemento seleccionado por defecto
        bottomNavigationView.selectedItemId = R.id.menu_home

        // Agrega el HomeFragment por defecto
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, HomeFragment())
            .commit()
    }

    // Anula el método onBackPressed para evitar salir de la actividad al presionar el botón de atrás
    override fun onBackPressed() {
        return
    }
}
