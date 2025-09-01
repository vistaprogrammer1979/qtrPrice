package com.santechture.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.price.pojo.RequestMetaData;
import com.santechture.price.pojo.ResponseMetaData;
import com.santechture.price.pojo.StatusEnum;
import com.santechture.price.pojo.TypeEnum;
import com.santechture.request.Activity;
import com.santechture.request.Claim;
import com.santechture.request.Request;
import io.opentelemetry.api.trace.Span;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RequestService {

    private static final Logger log = LoggerFactory.getLogger(RequestService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Inject
    Span span;
    @Inject
    ObjectMapper objectMapper;












}
