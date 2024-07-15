package com.example.project5g.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project5g.R

class PhoneFragment : Fragment(R.layout.fragment_phone) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_phone, container, false)

        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        emailTextView.setOnClickListener {
            // Replace current fragment with PhoneFragment
            val emailFragment = EmailFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, emailFragment)
                .addToBackStack(null)  // Optional: Adds transaction to back stack
                .commit()
        }

        return view
    }

}
