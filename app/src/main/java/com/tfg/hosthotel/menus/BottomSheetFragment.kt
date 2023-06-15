package com.tfg.hosthotel.menus

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tfg.hosthotel.R

class BottomSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn_config: Button = view.findViewById(R.id.btn_config)
        val btn_fav: Button = view.findViewById(R.id.btn_saves)
        val btn_list: Button = view.findViewById(R.id.btn_list)
        val btn_info: Button = view.findViewById(R.id.btn_info)

        btn_config.setOnClickListener {
            val intent = Intent(context, ConfigActivity::class.java)
            startActivity(intent)
            // Cerrar el BottomSheet
            dismiss()
        }

        btn_fav.setOnClickListener {
            val intent = Intent(context, SavesActivity::class.java)
            startActivity(intent)
            // Cerrar el BottomSheet
            dismiss()
        }

        btn_list.setOnClickListener {
            val intent = Intent(context, ListReviewsActivity::class.java)
            startActivity(intent)
            // Cerrar el BottomSheet
            dismiss()
        }

        btn_info.setOnClickListener {
            val intent = Intent(context, InformationActivity::class.java)
            startActivity(intent)
            // Cerrar el BottomSheet
            dismiss()
        }

    }
}