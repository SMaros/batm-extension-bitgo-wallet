package com.localcoin.batm.extension.bitgo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class ErrorResponseException extends HttpStatusExceptionSupport {
    @JsonProperty("error")
    public String error;

    @Override
    public String getMessage() {
        return error;
    }

}
