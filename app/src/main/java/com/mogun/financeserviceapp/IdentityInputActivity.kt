package com.mogun.financeserviceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mogun.financeserviceapp.databinding.ActivityIdentityInputBinding

class IdentityInputActivity: AppCompatActivity() {
    private lateinit var binding: ActivityIdentityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityIdentityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.view = this


    }
}