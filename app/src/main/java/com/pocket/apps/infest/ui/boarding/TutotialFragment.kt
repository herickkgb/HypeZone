package com.pocket.apps.infest.ui.boarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pocket.apps.infest.R
import com.pocket.apps.infest.databinding.FragmentTutotialBinding
import com.pocket.apps.infest.model.tips.Tip

class TutotialFragment : Fragment() {

    private var _binding: FragmentTutotialBinding? = null
    private val binding get() = _binding!!

    private val prefsFileName = "MeuAppPrefs"
    private val chaveNumero = "chaveNumero"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTutotialBinding.inflate(inflater, container, false)
        val view = binding.root

        val tips = arrayOf(
            Tip(getString(R.string.texto_1), R.drawable.lugar2),
            Tip(getString(R.string.texto2), R.drawable.pessoas),
            Tip(getString(R.string.texto3), R.drawable.cerveja),
            Tip(getString(R.string.texto4), R.drawable.mapa),
            Tip(getString(R.string.bemvindo), R.drawable.infestlogo)
        )

        addDots(tips.size)

        binding.viewPager.adapter = OnBoardingAdapter(tips)

        binding.viewPager.setPageTransformer(true) { page: View, position: Float ->
            page.alpha = 1 - kotlin.math.abs(position)
            page.translationX = -position * page.width
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                addDots(tips.size, position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.next.setOnClickListener {
            val ONBOARDING = 1

            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            if (binding.viewPager.currentItem == binding.viewPager.adapter?.count?.minus(1)) {
                findNavController().navigate(R.id.action_tutotialFragment_to_userLoginFragment)
            }

            salvarNumero(ONBOARDING)
        }

        binding.pull.setOnClickListener {
            findNavController().navigate(R.id.action_tutotialFragment_to_userLoginFragment)
        }

        return view
    }

    private fun addDots(size: Int, position: Int = 0) {
        binding.dots.removeAllViews()
        Array(size) {
            val textView = TextView(requireContext()).apply {
                text = getString(R.string.dotted)
                textSize = 35f
                setTextColor(
                    if (position == it) {
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.laranja_1000
                        )
                    } else {
                        ContextCompat.getColor(requireContext(), android.R.color.darker_gray)
                    }
                )
            }
            binding.dots.addView(textView)
        }
    }

    private inner class OnBoardingAdapter(val tips: Array<Tip>) : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(requireActivity())
            val view: View = inflater.inflate(R.layout.tip_content, container, false)

            val textViewTitle = view.findViewById<TextView>(R.id.tip_title)
            val imgView = view.findViewById<ImageView>(R.id.animationView)

            with(tips[position]) {
                textViewTitle.text = title
                imgView.setImageResource(logo)
            }

            container.addView(view)
            return view
        }

        override fun getCount(): Int = tips.size

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    private fun salvarNumero(numero: Int) {
        val prefs: SharedPreferences =
            requireContext().getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt(chaveNumero, numero)
        editor.apply()
    }

    private fun recuperarNumero(): Int {
        val prefs: SharedPreferences =
            requireContext().getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        return prefs.getInt(
            chaveNumero,
            0
        ) // O segundo parâmetro é o valor padrão se a chave não existir
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
