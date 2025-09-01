package com.santechture.price.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.santechture.request.Claim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMetaData {
    Claim claim;
    UUID requestId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    Timestamp createdAt;
    String hostname;
    String endpoint;
    String name;
    String request;
    TypeEnum type;
//    String traceId;


}
