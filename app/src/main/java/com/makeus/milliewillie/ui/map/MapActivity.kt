package com.makeus.milliewillie.ui.map

import android.view.ViewGroup
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMapBinding
import net.daum.mf.map.api.MapView
import org.koin.android.viewmodel.ext.android.viewModel

class MapActivity: BaseDataBindingActivity<ActivityMapBinding>(R.layout.activity_map) {

    private val viewModel by viewModel<MapViewModel>()

    override fun ActivityMapBinding.onBind() {
        vi = this@MapActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MapActivity)

        val mapView = MapView(this@MapActivity)

        val mapviewContainer = binding.mapLayoutMapView as ViewGroup
        mapviewContainer.addView(mapView)

    }


}