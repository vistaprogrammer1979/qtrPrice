package com.santechture.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.entity.*;
import com.santechture.pojo.*;
import com.santechture.repository.RequestRepository;
import com.santechture.request.Activity;
import com.santechture.request.Claim;
import com.santechture.request.Request;
import io.agroal.api.AgroalDataSource;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
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
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class RequestService {

    private static final Logger log = LoggerFactory.getLogger(RequestService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Inject
    Span span;
    @Inject
    RequestRepository repository;
    @Inject
    ObjectMapper objectMapper;

    private PriceRequest getPriceRequest(UUID requestId, StatusEnum status, Timestamp createdAt, String providerId, String claimId, String traceId, TypeEnum typeEnum, PriceMarket market, String SubBillingGroup) {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setPriceRequestId(requestId);
        priceRequest.setStatus(status);
        priceRequest.setCreatedAt(createdAt);
        priceRequest.setFacility(providerId);
        priceRequest.setClaimId(claimId);
        priceRequest.setTraceId(traceId);
        priceRequest.setRequestType(typeEnum);
        priceRequest.setMarket(market);
        priceRequest.setSubBillingGroup(SubBillingGroup);
        return priceRequest;
    }

    private void saveActivities(PriceRequest priceRequest, List<Activity> activities) {
        if (activities != null) {
            priceRequest.setActivities(new ArrayList<>());
            for (var activity : activities) {
                var act = new PriceActivity();
                act.setPriceActivityId(UUID.randomUUID());
                activity.setPriceActivityId(act.getPriceActivityId());
                act.setPriceRequest(priceRequest);
                act.setNet(activity.getNet() != null ? activity.getNet().toString() : "");
                act.setActivityId(activity.getActivityID() != null ? activity.getActivityID() : "");
                act.setClinician(activity.getClinician() != null ? activity.getClinician() : "");
                act.setCode(activity.getCode() != null ? activity.getCode() : "");
                act.setOrderingClinician(activity.getOrderingClinician() != null ? activity.getOrderingClinician() : "");
                act.setPriorAuthorizationId(activity.getPriorAuthorizationID() != null ? activity.getPriorAuthorizationID() : "");
                act.setQuantity(activity.getQuantity() != null ? activity.getQuantity().toString() : "");
                act.setStart(activity.getStart() != null ? dateFormat.format(activity.getStart()) : "");
                act.setType(activity.getType() != null ? activity.getType().name() : "");
                priceRequest.getActivities().add(act);
            }
        }
    }

    public void saveRequest(UUID requestId, Timestamp createdAt, String receiverId, String claimId, String traceId, TypeEnum typeEnum, PriceMarket market, String subBillingGroup) {
        PriceRequest priceRequest = getPriceRequest(requestId,
                StatusEnum.PENDING,
                createdAt,
                receiverId,
                claimId,
                traceId,
                typeEnum,
                market,
                subBillingGroup);
        repository.persist(priceRequest);
    }
    @WithSpan("Save in DB")
    @ActivateRequestContext
    @Transactional
    public boolean save(RequestMetaData requestMetaData) {
        log.info("Saving request ");
        try {
            PriceRequest priceRequest = repository.findByPriceRequestId(requestMetaData.getRequestId());
            priceRequest = getPriceRequest(requestMetaData.getRequestId(),
                    StatusEnum.IN_PROGRESS,
                    requestMetaData.getCreatedAt(),
                    requestMetaData.getClaim().getProviderID(),
                    requestMetaData.getClaim().getId() != null ? requestMetaData.getClaim().getId().toString() : "",
                    span.getSpanContext().getTraceId(),
                    requestMetaData.getType(),
                    requestMetaData.getMarket(),
                    requestMetaData.getClaim().getPatient() == null? "" : requestMetaData.getClaim().getPatient().getSubBillingGroup() == null ? "" : requestMetaData.getClaim().getPatient().getSubBillingGroup()
                    );
            PriceBody priceBody = new PriceBody(priceRequest, requestMetaData.getRequest(), null, null);
            priceRequest.setBody(priceBody);
//            priceRequest.setLog(new PriceLog());
            var activities = requestMetaData.getClaim().getActivity();
            saveActivities(priceRequest, activities);
            repository.persist(priceRequest);
            return true;
        } catch (Exception e) {
            log.error("Error saving request", e);
            log.error(e.getMessage());
            return false;
        }
    }

    private void saveAlertsBasedClaim(PriceRequest priceRequest, Claim claim, List<PriceAlert> alerts) {
        var outcomes = claim.getOutcome();
        if (outcomes != null) {
            for (var outcome : outcomes) {
                var alert = new PriceAlert();
                alert.setPriceRequest(priceRequest);
                alert.setShortMessage(outcome.getShortMsg());
                alert.setLongMessage(outcome.getLongMsg());
                alert.setCreatedAt(Timestamp.from(Instant.now()));
                alert.setSeverity(outcome.getSeverity());
                alert.setRuleId(outcome.getRuleName());
                alert.setObjectId(claim.getRootID() != null ? Integer.toString(claim.getRootID()) : "22");
                alert.setObjectType("CLAIM");
                alerts.add(alert);
            }
        }
    }

    private void saveAlertsBasedActivity(PriceRequest priceRequest, List<Activity> activities, List<PriceAlert> alerts) {
        for (var activity : activities) {
            if (activity.getOutcome() != null) {
                for (var outcome : activity.getOutcome()) {
                    var alert = new PriceAlert();
                    alert.setPriceRequest(priceRequest);
                    alert.setShortMessage(outcome.getShortMsg());
                    alert.setLongMessage(outcome.getLongMsg());
                    alert.setCreatedAt(Timestamp.from(Instant.now()));
                    alert.setSeverity(outcome.getSeverity());
                    alert.setRuleId(outcome.getRuleName());

                    alert.setObjectId(activity.getActivityID());
                    alert.setObjectType("ACTIVITY");
                    alerts.add(alert);
                }
            }
        }
    }

//    @ActivateRequestContext
//    @Transactional
//    public void saveResponse(ResponseMetaData responseMetaData) {
//        log.info("Saving response ");
//        PriceRequest priceRequest = repository.findByPriceRequestId(responseMetaData.getRequestId());
//        if (priceRequest == null) {
//            log.info("Request Not Exist");
//            priceRequest = getPriceRequest(responseMetaData.getRequestId(),
//                    StatusEnum.IN_PROGRESS,
//                    responseMetaData.getFinishedAt(),
//                    "",
//                    "",
//                    span.getSpanContext().getTraceId(),
//                    TypeEnum.DHA,);
//
//            if (responseMetaData.getResponse() != null) {
//                priceRequest.setBody(new PriceBody(priceRequest, null, responseMetaData.getResponse(), null));
//            } else if (responseMetaData.getClaim() != null) {
//                try {
//                    priceRequest.setBody(new PriceBody(priceRequest, null, objectMapper.writeValueAsString(responseMetaData.getClaim()), null));
//                } catch (JsonProcessingException e) {
//                    priceRequest.setBody(new PriceBody(priceRequest, null, null, null));
//                }
//            }
//
//        } else {
//            log.info("Request Exist");
//            priceRequest.setStatus(StatusEnum.FINISHED);
//            if (priceRequest.getBody() != null) {
//                if (responseMetaData.getResponse() != null) {
//                    priceRequest.getBody().setResponse(responseMetaData.getResponse());
//                } else if (responseMetaData.getClaim() != null) {
//                    try {
//                        priceRequest.getBody().setResponse(objectMapper.writeValueAsString(responseMetaData.getClaim()));
//                    } catch (JsonProcessingException e) {
//                        priceRequest.getBody().setResponse(null);
//                    }
//                }
//            }
//
//
//        }
//        priceRequest.setFinishedAt(responseMetaData.getFinishedAt());
//        priceRequest.setProcessTime(responseMetaData.getProcessTime());
//
//        if (responseMetaData.getResponse() != null) {
//            try {
//                Request response = objectMapper.readValue(responseMetaData.getResponse(), Request.class);
//                var alerts = new ArrayList<PriceAlert>();
//                if (response.getOutcome() != null) {
//                    var claims = response.getClaim();
//                    if (claims != null) {
//                        var claim = claims.get(0);
//                        if (claim != null) {
//                            saveAlertsBasedClaim(priceRequest, claim, alerts);
//                            var activities = claim.getActivity();
//                            if (activities != null) {
//                                saveAlertsBasedActivity(priceRequest, activities, alerts);
//                            }
//                            priceRequest.setAlerts(alerts);
//                            log.info("Alerts numbers {}", alerts.size());
//                        }
//                    }
//                }
//
//            } catch (JsonProcessingException e) {
//                log.error("Error saving response ", e);
//            }
//        } else if (responseMetaData.getClaim() != null) {
//            var claim = responseMetaData.getClaim();
//            var alerts = new ArrayList<PriceAlert>();
//            saveAlertsBasedClaim(priceRequest, claim, alerts);
//            var activities = claim.getActivity();
//            if (activities != null) {
//                saveAlertsBasedActivity(priceRequest, activities, alerts);
//            }
//            priceRequest.setAlerts(alerts);
//        }
//        repository.persist(priceRequest);
//
//    }
//    public void saveAsync(RequestMetaData requestMetaData) {
//        executorService.submit(() -> save(requestMetaData));
//    }
//    public void saveResponseAsync(ResponseMetaData responseMetaData) {
//        executorService.submit(() -> saveResponse(responseMetaData));
//    }

//    public boolean save2(RequestMetaData requestMetaData){
//
//        String sql = String.format("insert into price.price_request (%s, %s,%s, %s, %s, %s) values ();",
//                requestMetaData.getRequestId(),
//                requestMetaData.getClaim().getId(),
//                requestMetaData.getClaim().getProviderID(),
//                "DHA",
//                StatusEnum.IN_PROGRESS.name(),
//                span.getSpanContext().getTraceId());
//        try (Connection connection = dataSource.getConnection()) {
//            try (PreparedStatement statement = connection.prepareStatement(sql)) {
//                try (ResultSet resultSet = statement.executeQuery()) {
//
//                }
//
//    }

//    @ActivateRequestContext
//    @Transactional
//    public void saveFacts(Data data){
//        log.info("Saving facts ");
//        PriceRequest priceRequest = repository.findByPriceRequestId(responseMetaData.getRequestId());
//    }

    @ActivateRequestContext
    @Transactional
    public void saveErrorMessage(UUID uuid, String errorMessage) {
        log.info("Saving error message for request {}", uuid.toString());
        try {
            PriceRequest priceRequest = repository.findByPriceRequestId(uuid);
            priceRequest.setErrorMessage(errorMessage);
            repository.persist(priceRequest);
        } catch (Exception e){
            log.error("could not save error message for request {}", uuid.toString());
            log.error("exception message: {}", e.getMessage());
        }
    }
}
