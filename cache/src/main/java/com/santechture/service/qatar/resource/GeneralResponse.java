package com.santechture.service.qatar.resource;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
    private Integer code;
    private String message;
    private Integer total;
    private Object data;
    private String logId;

}

