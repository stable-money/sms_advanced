package com.elyudde.sms_advanced.status.sim

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.flutter.plugin.common.EventChannel.EventSink

class SimChangeListener(private val eventSink: EventSink) : BroadcastReceiver() {
    private val TAG = "SimChangeReceivers"

    // This is where you'll be receiving the SIM_STATE_CHANGE intent.
    override fun onReceive(p0: Context?, p1: Intent?) {
        var state = ""
        if (p1 != null) {
            state = p1.extras?.getString("ss").toString()
        }
        Log.i(TAG, "SIM State Change Detected $state")
        eventSink.success(state)
    }
}