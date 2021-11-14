package com.newsapp.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.newsapp.R
import com.newsapp.utils.Constants.IS_CONNECTED


object Utils {

    private fun showMessage(context: Context?, view: View?, msg: String?) {
        try {
            val snackBar = Snackbar.make(view!!, msg!!, Snackbar.LENGTH_LONG)
            snackBar.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    // Network Check
    @Suppress("DEPRECATION")
    fun registerNetworkConnections(context: Context, view: View?) {
        kotlin.runCatching {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        IS_CONNECTED = true
                    }

                    override fun onLost(network: Network) {
                        IS_CONNECTED = false // Global Static Variable
                    }
                }
                )
            } else {
                val activeNetwork = connectivityManager.activeNetworkInfo
                IS_CONNECTED = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting
            }
        }.onFailure {
            IS_CONNECTED = false
        }
    }

    fun isDarkMode(context: Context): Boolean {
        val nightModeFlags: Int = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        var isDarkMode = false
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> isDarkMode = true
            Configuration.UI_MODE_NIGHT_NO -> isDarkMode = false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> isDarkMode = false
        }
        return isDarkMode
    }

    fun openDialPad(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(context.getString(R.string.my_no))
        context.startActivity(intent)
    }

    fun openBrowser(context: Context, url: String) {
        val result = kotlin.runCatching {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
        result.onFailure {
            Log.i("TAG", "openBrowser: ${it.message}")
        }
    }

}

