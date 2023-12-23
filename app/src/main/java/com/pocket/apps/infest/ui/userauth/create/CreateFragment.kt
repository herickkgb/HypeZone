package com.pocket.apps.infest.ui.userauth.create

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
import com.pocket.apps.infest.databinding.FragmentCreateBinding
import com.pocket.apps.infest.ui.userauth.auth.FirebaseHelper

class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        setupCreateAccountButton()
        return binding.root
    }

    private fun setupCreateAccountButton() {
        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val nome = binding.editNome.text.trim().toString()
            val email = binding.editEmail.text.trim().toString()
            val telefone = binding.editTelefone.text.trim().toString()
            val senha = binding.editSenha.text.trim().toString()

            if (nome.isNotEmpty() || email.isNotEmpty() || telefone.isNotEmpty() || senha.isNotEmpty()) {
                FirebaseHelper.getAuth().createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            binding.progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_createFragment_to_userLoginFragment)
                            val snackbar = Snackbar.make(
                                requireView(),
                                "Login feito com sucesso",
                                Snackbar.LENGTH_SHORT
                            )
                            snackbar.setBackgroundTint(Color.GREEN)
                            snackbar.setActionTextColor(Color.WHITE)
                            snackbar.show()
                        } else {
                            binding.progressBar.visibility = View.GONE
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
                    "Preencha todos os campos...",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
