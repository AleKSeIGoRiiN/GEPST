package com.example.gepst

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gepst.databinding.ActivityMainBinding
import com.example.gepst.databinding.FragmentRotBinding
import com.example.gepst.databinding.SecondActivityBinding


class RotFragment : Fragment(R.layout.fragment_rot) {
    private val binding by viewBinding(FragmentRotBinding::bind, R.id.fragmentRot)
    var butRot: Button = binding.rotLeft

    companion object {
        val TAG = RotFragment::class.java.simpleName

        fun newInstance() = RotFragment()
    }


}