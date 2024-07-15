package com.example.project5g.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project5g.R

class EmailFragment : Fragment(R.layout.fragment_email) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_email, container, false)

        val phoneTextView = view.findViewById<TextView>(R.id.phoneTextView)
        phoneTextView.setOnClickListener {
            // Replace current fragment with PhoneFragment
            val phoneFragment = PhoneFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, phoneFragment)
                .addToBackStack(null)  // Optional: Adds transaction to back stack
                .commit()
        }

        return view
    }
}
