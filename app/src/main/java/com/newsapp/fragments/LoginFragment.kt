package com.newsapp.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.newsapp.R
import com.newsapp.databinding.FragmentLoginBinding
import com.newsapp.utils.Constants.LOGIN_KEY
import com.newsapp.utils.SharedPref
import com.newsapp.utils.Validations
import com.newsapp.utils.Validations.isEmailValidate
import com.newsapp.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: HomeViewModel by activityViewModels()

    private var email: String? = null
    private var password: String? = null

    @Inject
    lateinit var session: SharedPref
    private lateinit var savedStateHandle: SavedStateHandle


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_KEY, false)
        binding = FragmentLoginBinding.bind(view)
        setListeners()
        handleBackPress()
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }

    private fun setListeners() {
        binding.apply {
            email.doAfterTextChanged {
                emailTextInput.error = null
            }
            password.doAfterTextChanged {
                passwordTextInput.error = null
            }

            login.setOnClickListener {
                signIn()
            }
        }

    }

    private fun getDataFormUser() {
        password = binding.password.text.toString().trim()
        email = binding.email.text.toString().trim()
    }

    private fun signIn() {
        getDataFormUser()
        if (validateValues(email, password)) {
            CoroutineScope(Main).launch {
                kotlin.runCatching { viewModel.login() }.onSuccess {
                    session.putBoolean(LOGIN_KEY, true)
                    savedStateHandle.set(LOGIN_KEY, true)
                    findNavController().popBackStack()
                }.onFailure {
                    //Todo : Handle Exceptions Here
                }
            }
        }
    }

    private fun validateValues(email: String?, password: String?): Boolean {
        val isEmailValidated = isEmailValidate(email, binding.emailTextInput)
        val isPasswordValidated =
            Validations.isPasswordValidate(password, binding.passwordTextInput)
        return isEmailValidated && isPasswordValidated
    }
}