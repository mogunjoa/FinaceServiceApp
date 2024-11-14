package com.mogun.financeserviceapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.children
import com.mogun.financeserviceapp.databinding.WidgetShuffleNumberKeyboardBinding
import kotlin.random.Random

class ShuffleNumberKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var _binding: WidgetShuffleNumberKeyboardBinding? = null
    private val binding get() = _binding!!

    private var listener: KeyPadListener? = null

    init {
        _binding =
            WidgetShuffleNumberKeyboardBinding.inflate(LayoutInflater.from(context), this, true)
        binding.view = this
        binding.clickListener = this
        shuffle()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        _binding = null
    }

    fun shuffle() {
        val keyNumberArray = ArrayList<String>()
        for (i in 0..9) {
            keyNumberArray.add(i.toString())
        }

        binding.gridLayout.children.forEach { view ->
            if(view is TextView && view.tag != null) {
                val randomIndex = Random.nextInt(keyNumberArray.size)
                view.text = keyNumberArray[randomIndex]
                keyNumberArray.removeAt(randomIndex)
            }
        }
    }

    fun setKeyPadListener(listener: KeyPadListener) {
        this.listener = listener
    }

    fun onClickDelete() {
        listener?.onClickDelete()
    }

    fun onClickDone() {
        listener?.onClickDone()
    }

    interface KeyPadListener {
        fun onClickNum(num: String)
        fun onClickDelete()
        fun onClickDone()
    }

    override fun onClick(view: View?) {
        if(view is TextView && view.tag != null) {
            listener?.onClickNum(view.text.toString())
        }
    }
}