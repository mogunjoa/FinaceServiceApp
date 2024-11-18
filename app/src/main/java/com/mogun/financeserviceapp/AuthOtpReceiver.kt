package com.mogun.financeserviceapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Base64
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 * 1. 문자 내용이 140바이트를 초과하면 안된다.
 * 2. 문자 맨 앞에 <#> 가 포함이 되어야한다.
 * 3. 맨 마지막 앱을 식별하는 11글자 해시코드가 존재해야한다.
 */
class AuthOtpReceiver: BroadcastReceiver() {
    private var listener: OtpReceiveListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            intent.extras?.let {bundle ->
                val status = bundle.get(SmsRetriever.EXTRA_STATUS) as Status
                when(status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val otpSms = bundle.getString(SmsRetriever.EXTRA_SMS_MESSAGE, "")
                        if(listener != null && otpSms.isNotEmpty()) {
                            val otp = PATTERN.toRegex().find(otpSms)?.destructured?.component1()
                            if(otp.isNullOrBlank()) {
                                listener!!.onOtpReceived(otpSms)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setOtpListener(receiveListener: OtpReceiveListener) {
        this.listener = receiveListener
    }

    fun doFilter() = IntentFilter().apply {
        addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
    }

    interface OtpReceiveListener {
        fun onOtpReceived(otp: String)
    }

    companion object {
        private const val PATTERN = "^<#>.*\\[Sample]\\].+\\[(\\d{6})\\].+\$"


        fun generateHash(packageName: String, sha256Key: String): String {
            val combinedString = "$packageName $sha256Key"

            // Generate SHA-256 hash
            val hash = MessageDigest.getInstance("SHA-256").digest(combinedString.toByteArray(StandardCharsets.UTF_8))

            // Convert to Base64 and extract first 11 characters
            return Base64.encodeToString(hash, Base64.NO_WRAP).substring(0, 11)
        }
    }
}