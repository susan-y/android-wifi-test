package com.innoaus.test.wifisuggestion

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var wifiManager: WifiManager
    var suggestionsList = ArrayList<WifiNetworkSuggestion>()

    val ssid = "TP-5G"
    val password = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedInstanceState1 = savedInstanceState
        super.onCreate(savedInstanceState1)
        setContentView(R.layout.activity_main)

        wifiManager = getSystemService(WIFI_SERVICE) as WifiManager

        val item1 = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password)
            .setIsAppInteractionRequired(true)
            .setPriority(1000)
            .build()
        suggestionsList.add(item1)

        val suggestionButton = findViewById<Button>(R.id.button_suggestion)
        suggestionButton.setOnClickListener {
            if (!wifiManager.isWifiEnabled) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Wi-Fi Settings")
                builder.setMessage("Please Turn Wi-Fi Setting On.")
                    .setPositiveButton("OK") { dialog, which ->
                        val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
                        startActivityForResult(panelIntent, 1000)
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                    }
                builder.create().show()
            } else {
                connectUsingNetworkSuggestion()
            }
        }

        val intentFilter = IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                log("================= onReceive")
                toast("Connection Suggestion Succeeded")
                log("Connection Suggestion Succeeded")
                // do post connect processing here
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        log("resultCode: $resultCode")
        if (requestCode == 2000) {
        } else if (requestCode == 1000) {
            connectUsingNetworkSuggestion()
        }
    }

    private fun connectUsingNetworkSuggestion() {
        // Optional (Wait for post connection broadcast to one of your suggestions)
        var status = wifiManager.addNetworkSuggestions(suggestionsList)
        log("Adding Network suggestions status is $status")
        if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE) {
            toast("Suggestion Update Needed")
            status = wifiManager.removeNetworkSuggestions(suggestionsList)
            log("Removing Network suggestions status is $status")
            status = wifiManager.addNetworkSuggestions(suggestionsList)
        }
        if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            toast("Suggestion Added")
            log("Suggestion Added.")
            log("============== get network suggestions")
            log("" + wifiManager.connectionInfo.ssid)
        }
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun log(message: String) {
        Log.d("wifisuggestion", "[WIFI-SUGGESTION] $message")
    }
}