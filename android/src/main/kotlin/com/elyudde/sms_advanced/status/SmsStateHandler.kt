package com.elyudde.sms_advanced.status

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.elyudde.sms_advanced.permisions.Permissions
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.PluginRegistry.RequestPermissionsResultListener
import org.json.JSONObject


/**
 * Created by crazygenius on 1/08/21.
 */
class SmsStateHandler(val context: Context, private val binding: ActivityPluginBinding) :
    EventChannel.StreamHandler {
    var eventSink: EventSink? = null

    private val sentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val stateChange = JSONObject()
            stateChange.put("sentId", intent.getIntExtra("sentId", -1))
            when (resultCode) {
                Activity.RESULT_OK -> {
                    stateChange.put("state", "sent")
                    //Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show()
                }

                SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                    stateChange.put("state", "RESULT_ERROR_GENERIC_FAILURE")
//                    Toast.makeText(
//                        context,
//                        "Generic failure",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                SmsManager.RESULT_ERROR_NO_SERVICE -> {
                    stateChange.put("state", "RESULT_ERROR_NO_SERVICE")
//                    Toast.makeText(
//                        context,
//                        "No service",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                SmsManager.RESULT_ERROR_NULL_PDU -> {
                    stateChange.put("state", "RESULT_ERROR_NULL_PDU")
//                    Toast.makeText(
//                        context,
//                        "Null PDU",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                SmsManager.RESULT_ERROR_RADIO_OFF -> {
                    stateChange.put("state", "RESULT_ERROR_RADIO_OFF")
//                    Toast.makeText(
//                        context,
//                        "Radio off",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                else -> {
                    stateChange.put("state", "fail")
                }
            }
            eventSink?.success(stateChange)
        }
    }

    private val deliveredReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val stateChange = JSONObject()
            stateChange.put("sentId", intent.getIntExtra("sentId", -1))
            when (resultCode) {
                Activity.RESULT_OK -> {
                    stateChange.put("state", "DELIVERED")
//                    Toast.makeText(context, "SMS Delivered", Toast.LENGTH_SHORT)
//                        .show()
                }
                Activity.RESULT_CANCELED -> {
                    stateChange.put("state", "DELIVERY_CANCELLED")
//                    Toast.makeText(
//                        context,
//                        "SMS not delivered",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
                else -> {
                    stateChange.put("state", "UNKNOWN")
                }
            }
            eventSink?.success(stateChange)
        }
    }

    override fun onListen(argument: Any?, eventSink: EventSink) {
        this.eventSink = eventSink
        registerDeliveredReceiver()
        registerSentReceiver()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun registerDeliveredReceiver() {
        ContextCompat.registerReceiver(
            context,
            deliveredReceiver,
            IntentFilter("SMS_DELIVERED"),
            ContextCompat.RECEIVER_EXPORTED,
        )
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun registerSentReceiver() {
        ContextCompat.registerReceiver(
            context,
            sentReceiver,
            IntentFilter("SMS_SENT"),
            ContextCompat.RECEIVER_EXPORTED,
        )
    }

    override fun onCancel(argument: Any?) {
        context.unregisterReceiver(sentReceiver)
        context.unregisterReceiver(deliveredReceiver)
    }
}