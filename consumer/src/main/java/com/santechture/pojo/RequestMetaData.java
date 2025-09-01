package com.santechture.pojo;

import com.santechture.request.Claim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestMetaData {
    Claim claim;
    UUID requestId;
    Timestamp createdAt;
    String hostname;
    String endpoint;
    String name;
    String request;
    String traceId;
    TypeEnum type;


}
