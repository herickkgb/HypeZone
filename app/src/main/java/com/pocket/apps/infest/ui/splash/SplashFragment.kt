package com.pocket.apps.infest.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pocket.apps.infest.R
import com.pocket.apps.infest.databinding.FragmentSplashBinding
import com.pocket.apps.infest.ui.userauth.auth.FirebaseHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root

    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            val navController = findNavController()
            navController.navigate(R.id.action_splashFragment_to_userLoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseHelper.getAuth().currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }else{
            findNavController().navigate(R.id.action_splashFragment_to_tutotialFragment)
        }

    }

}
