package com.tfg.hosthotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            // Aquí iría el código para guardar los cambios
            Toast.makeText(this, "Cambios aplicados", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}
