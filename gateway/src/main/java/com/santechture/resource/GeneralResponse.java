package com.santechture.resource;



import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
    private Integer code;
    private String message;
    private Integer total;
    private Object data;
    private String logId = UUID.randomUUID().toString();
    private String action;
}
