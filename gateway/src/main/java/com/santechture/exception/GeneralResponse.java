package com.santechture.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse {
    int statusCode;
    String responseMessage;
    String responseBody;


}
