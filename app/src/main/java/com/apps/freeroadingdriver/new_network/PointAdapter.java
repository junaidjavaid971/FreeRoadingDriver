package com.apps.freeroadingdriver.new_network;

import com.apps.freeroadingdriver.model.dataModel.Profile;
import com.apps.freeroadingdriver.newModel.Response_data;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class PointAdapter extends TypeAdapter<Response_data> {
     public Response_data read(JsonReader reader) throws IOException {
       if (reader.peek() == JsonToken.NULL) {
         reader.nextNull();
         return null;
       }

       return new Response_data();
     }
     public void write(JsonWriter writer, Response_data value) throws IOException {
       if (value == null) {
         writer.nullValue();
         return;
       }

       writer.value(String.valueOf(value.getProfile()));
     }
   }