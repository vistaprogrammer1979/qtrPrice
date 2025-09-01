package com.santechture.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GenderSerializer extends StdSerializer<String> {
    public GenderSerializer() {
        this(null);
    }

    protected GenderSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("9")) {
            gen.writeString(value);
        } else if (value.equalsIgnoreCase("M")) {
            gen.writeString("1");
        } else if (value.equalsIgnoreCase("F")) {
            gen.writeString("0");
        } else if (value.equalsIgnoreCase("U")) {
            gen.writeString("9");
        } else {
            gen.writeString("9");
        }
    }
}