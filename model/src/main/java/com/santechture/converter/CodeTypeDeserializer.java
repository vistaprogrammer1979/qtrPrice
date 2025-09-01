package com.santechture.converter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.santechture.request.CodeType;

import java.io.IOException;

public class CodeTypeDeserializer extends JsonDeserializer<CodeType> {
    @Override
    public CodeType deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.getCurrentToken().isNumeric()) {
            return CodeType.from(p.getIntValue());
        }
        else {
            throw new IOException("Value is not a number");
        }
    }
}
