package com.mogun.financeserviceapp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.mogun.financeserviceapp.databinding.ActivityVerifyOtpBinding
import com.mogun.financeserviceapp.util.ViewUtil.showKeyboardDelay
import com.mogun.financeserviceapp.util.ViewUtil.setOnEditorActionListener

class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private var timer: CountDownTimer? = object : CountDownTimer(3 * 60 * 1000, 1000) {
        override fun onTick(p0: Long) {
            val min = (p0 / 1000) / 60
            val sec = (p0 / 1000) % 60

            binding.timeTextView.text = "$min:${String.format("%02d", sec)}"
        }

        override fun onFinish() {
            binding.timeTextView.text = "00:00"
            Toast.makeText(
                this@VerifyOtpActivity,
                "인증 시간이 만료되었습니다.\n다시 시도해 주세요.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.otpCdoeEditText.showKeyboardDelay()
    }

    override fun onDestroy() {
        clearTimer()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.view = this
        initView()
    }

    private fun initView() {
        startTimer()
        with(binding) {
            otpCdoeEditText.doAfterTextChanged {
                if(otpCdoeEditText.length() >= 6) {
                    stopTimer()
                }
            }

            otpCdoeEditText.setOnEditorActionListener(EditorInfo.IME_ACTION_DONE) {

            }
        }
    }


    private fun startTimer() {
        timer?.start()
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun clearTimer() {
        timer = null
    }
}