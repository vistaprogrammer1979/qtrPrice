package com.santechture.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class StringLengthDeserializer extends StdDeserializer<String> {
    public StringLengthDeserializer() {
        this(null);
    }

    protected StringLengthDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (value != null && value.length() > 150) {
            return value.substring(0, 150) + "...";
        }
        return value;
    }
}