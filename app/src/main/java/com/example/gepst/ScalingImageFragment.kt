package com.example.gepst

import androidx.fragment.app.Fragment

class ScalingImageFragment: Fragment(R.layout.fragment_scaling) {
    companion object{
        val TAG = ScalingImageFragment::class.java.simpleName

        fun newInstance() = ScalingImageFragment()
    }
}