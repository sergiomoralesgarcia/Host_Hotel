package com.tfg.hosthotel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BottomSheetActivity : AppCompatActivity() {

    private lateinit var btnConfig: Button
    private lateinit var btnListReviews: Button
    private lateinit var btnSave: Button
    private lateinit var btnInfo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_layout)

        initializeButtons()
        setOnClickListeners()
    }

    private fun initializeButtons() {
        btnConfig = findViewById(R.id.btn_config)
        btnListReviews = findViewById(R.id.btn_saves)
        btnSave = findViewById(R.id.btn_list)
        btnInfo = findViewById(R.id.btn_info)
    }

    private fun setOnClickListeners() {
        btnConfig.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }

        btnListReviews.setOnClickListener {
            startActivity(Intent(this, ListReviewsAvtivity::class.java))
        }

        btnSave.setOnClickListener {
            startActivity(Intent(this, SavesActivity::class.java))
        }

        btnInfo.setOnClickListener {
            startActivity(Intent(this, InformationActivity::class.java))
        }
    }
}


