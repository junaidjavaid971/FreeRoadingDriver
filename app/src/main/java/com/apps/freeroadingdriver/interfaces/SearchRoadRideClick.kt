package com.apps.freeroadingdriver.interfaces

import com.apps.freeroadingdriver.model.responseModel.Road_trips


interface SearchRoadRideClick {
    fun takeDatatoFragment(road_trips: Road_trips)
}