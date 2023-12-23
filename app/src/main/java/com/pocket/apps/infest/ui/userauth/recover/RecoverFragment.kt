package com.pocket.apps.infest.ui.userauth.recover

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.apps.infest.databinding.FragmentRecoverBinding
import com.pocket.apps.infest.ui.userauth.auth.FirebaseHelper

class RecoverFragment : Fragment() {

    private var _binding: FragmentRecoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            if (email.isNotEmpty()) {
                recover(email)
            } else {
                showSnackbar("Please enter your email", false)
            }
        }
    }

    private fun recover(email: String) {
        binding.progressBar.visibility = View.VISIBLE
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    showSnackbar("Check your email", true)
                } else {
                    showSnackbar(task.exception?.message.toString(), false)
                }
            }
    }

    private fun showSnackbar(message: String, isSuccess: Boolean) {
        val snackbar = Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        )
        snackbar.setBackgroundTint(if (isSuccess) Color.BLUE else Color.RED)
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
