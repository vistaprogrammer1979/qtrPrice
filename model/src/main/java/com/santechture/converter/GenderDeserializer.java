package com.santechture.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class GenderDeserializer extends StdDeserializer<String> {
    public GenderDeserializer() {
        this(null);
    }

    protected GenderDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("9")) {
            return value;
        } else if (value.equalsIgnoreCase("M")) {
            return "1";
        } else if (value.equalsIgnoreCase("F")) {
            return "0";
        } else if (value.equalsIgnoreCase("U")) {
            return "9";
        } else {
            return "9";
        }
    }
}