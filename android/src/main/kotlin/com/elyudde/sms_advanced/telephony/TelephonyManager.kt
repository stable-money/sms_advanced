package com.elyudde.sms_advanced.telephony

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import java.lang.reflect.InvocationTargetException


class TelephonyManager(private val context: Context) {
    private var manager: TelephonyManager? = null
        private get() {
            if (field == null) {
                field = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            }
            return field
        }

    private var subScriptionManager: SubscriptionManager? = null
        private get() {
            if (field == null) {
                field = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            }
            return field
        }


    val activeSubscriptionInfoList: List<SubscriptionInfo>
        @SuppressLint("MissingPermission")
        get() = subScriptionManager?.activeSubscriptionInfoList ?: listOf()
}
