package com.example.deliverylist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.deliverylist.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var clMain: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clMain = findViewById(R.id.cl_main)
        replaceContentWithFragment(DeliveryListFragment.newInstance(), false)
    }

    fun replaceContentWithFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fg_content, fragment)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
    }

    fun showSnackBarMessage(message: String) {
        Snackbar.make(clMain, message, Snackbar.LENGTH_LONG).show()
    }
}
