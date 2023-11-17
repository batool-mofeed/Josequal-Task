package com.batool.josequaltask.ui.main.placedetails

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.batool.josequaltask.R
import com.batool.josequaltask.databinding.DialogPlaceDetailsBinding
import com.batool.josequaltask.model.PlaceModel

/**
 * Created By Batool Mofeed - Vibes Solutions on 11/18/2023.
 **/
class PlaceDetailsDialog(
    private val place: PlaceModel
) : DialogFragment() {

    lateinit var binding: DialogPlaceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_place_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
    }

    private fun initClicks() {
        with(binding) {
            closeBtn.setOnClickListener {
                dismiss()
            }
        }
    }


    companion object {
        fun newInstance(
            place: PlaceModel
        ) = PlaceDetailsDialog(place)
    }
}