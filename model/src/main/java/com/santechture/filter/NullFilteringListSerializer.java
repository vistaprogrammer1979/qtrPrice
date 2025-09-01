package com.santechture.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public  class NullFilteringListSerializer extends JsonSerializer<List<?>> {
    @Override
    public void serialize(List<?> value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

        if (value == null) {
            gen.writeNull();
            return;
        }

        // Filter out null values
        List<?> filteredList = value.stream()
                .filter(Objects::nonNull)
                .toList();

        gen.writeStartArray();
        for (Object item : filteredList) {
            gen.writeObject(item);
        }
        gen.writeEndArray();
    }
}