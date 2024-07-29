package com.elyudde.sms_advanced.status.sim

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.elyudde.sms_advanced.permisions.Permissions
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink


/**
 * Created by crazygenius on 1/08/21.
 */
class SimStateChangeHandler(val context: Context, private val binding: ActivityPluginBinding) : EventChannel.StreamHandler {
    private var simChangeListener: BroadcastReceiver? = null
    var eventSink: EventSink? = null

    override fun onListen(argument: Any?, eventSink: EventSink) {
        this.eventSink = eventSink
        simChangeListener = SimChangeListener(eventSink)
        registerSimChangeListener()
    }

    private fun registerSimChangeListener() {
        ContextCompat.registerReceiver(
            context,
            simChangeListener,
            IntentFilter("SIM_STATE_CHANGED"),
            ContextCompat.RECEIVER_EXPORTED,
        )
    }

    override fun onCancel(argument: Any?) {
        context.unregisterReceiver(simChangeListener)
        simChangeListener = null
    }
}