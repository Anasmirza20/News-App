package com.newsapp.utils

import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

object Validations {

    fun isEmailValidate(email: String?, emailTextInputLayout: TextInputLayout): Boolean {
        var isValidate = true
        val emailPattern = Patterns.EMAIL_ADDRESS
        if (email.isNullOrEmpty()) {
            isValidate = false
            emailTextInputLayout.error = "Please enter email"
        } else if (!email.matches(emailPattern.toRegex())) {
            isValidate = false
            emailTextInputLayout.error = "Please enter a valid email address"
        }
        return isValidate
    }

    fun isPasswordValidate(password: String?, emailTextInputLayout: TextInputLayout) =
        if (password.isNullOrEmpty()) {
            emailTextInputLayout.error = "Please enter password"
            false
        } else true
}