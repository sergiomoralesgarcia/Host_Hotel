package com.tfg.hosthotel.menus

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.tfg.hosthotel.R

class InformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val btnGithub = findViewById<ImageButton>(R.id.btn_github)
        btnGithub.setOnClickListener {
            val uri = Uri.parse("https://github.com/sergiomoralesgarcia")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val btnTwitter = findViewById<ImageButton>(R.id.btn_twitter)
        btnTwitter.setOnClickListener {
            val uri = Uri.parse("https://twitter.com/?lang=es")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val btnInstagram = findViewById<ImageButton>(R.id.btn_instagram)
        btnInstagram.setOnClickListener {
            val uri = Uri.parse("https://www.instagram.com/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val btnFacebook = findViewById<ImageButton>(R.id.btn_facebook)
        btnFacebook.setOnClickListener {
            val uri = Uri.parse("https://www.facebook.com/?stype=lo&jlou=AfdHo09a0HhQFInr1P9AlN8e8ReXVA_KHazMaF67PvWXHpFr5cn3InpIDpGpUcSJSqS-kmHu0SPAjifRbamOQipKE8_247jduuQASKF95XVArA&smuh=45099&lh=Ac8bVgCUpcm3apKr7ws")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}
