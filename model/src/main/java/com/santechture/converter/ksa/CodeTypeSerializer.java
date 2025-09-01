package com.santechture.converter.ksa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.santechture.request.ksa.CodeType;

import java.io.IOException;

public class CodeTypeSerializer extends JsonSerializer<CodeType> {
    @Override
    public void serialize(CodeType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            gen.writeNumber(value.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

