/*

Copyright 2015 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package com.apps.freeroadingdriver.googleDirection.network;


import com.apps.freeroadingdriver.googleDirection.constant.DirectionUrl;
import com.apps.freeroadingdriver.googleDirection.model.Direction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionService {

    @GET(DirectionUrl.DIRECTION_API_URL)
    Call<Direction> getDirection(@Query("origin") String origin,
                                 @Query("destination") String destination,
                                 @Query("waypoints") String waypoints,
                                 @Query("mode") String transportMode,
                                 @Query("departure_time") String departureTime,
                                 @Query("language") String language,
                                 @Query("units") String units,
                                 @Query("avoid") String avoid,
                                 @Query("transit_mode") String transitMode,
                                 @Query("alternatives") boolean alternatives,
                                 @Query("key") String apiKey);
}
