package com.cs4520.assignment5.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private var _loginSuccess = MutableLiveData<Boolean>(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(userName: String, password: String) : Boolean {
        if (userName == "admin" && password == "admin") {
            _loginSuccess = MutableLiveData<Boolean>(true)
            return true
        }
        return false
    }
}