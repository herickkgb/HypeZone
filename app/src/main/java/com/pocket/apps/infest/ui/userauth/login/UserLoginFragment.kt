package com.pocket.apps.infest.ui.userauth.login

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pocket.apps.infest.R
import com.pocket.apps.infest.databinding.FragmentUserLoginBinding
import com.pocket.apps.infest.ui.userauth.auth.FirebaseHelper

class UserLoginFragment : Fragment() {
    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun chamarLogin(email: String, senha: String) {
        binding.progressBar.visibility = View.VISIBLE
        if (email.isNotEmpty() || senha.isNotEmpty()) {
            FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener { task ->
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        if (isAdded) {
                            findNavController().navigate(R.id.action_userLoginFragment_to_homeFragment)
                        }
                    } else {
                        val snackbar = Snackbar.make(
                            requireView(),
                            FirebaseHelper.validError(task.exception?.message.toString()),
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.setActionTextColor(Color.WHITE)
                        snackbar.show()
                    }
                }
        } else {
            binding.progressBar.visibility = View.GONE
            val snackbar = Snackbar.make(
                requireView(),
                "Preencha Email e Senha...",
                Snackbar.LENGTH_SHORT
            )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            val senha = binding.editSenha.text.trim().toString()
            chamarLogin(email, senha)
        }

        binding.btnCriarConta.setOnClickListener {
            findNavController().navigate(R.id.action_userLoginFragment_to_createFragment)
        }

        binding.btnRecuperarConta.setOnClickListener {
            findNavController().navigate(R.id.action_userLoginFragment_to_recoverFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
