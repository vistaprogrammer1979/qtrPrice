package com.santechture.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.entity.PriceActivity;
import com.santechture.entity.PriceAlert;
import com.santechture.entity.PriceBody;
import com.santechture.entity.PriceRequest;
import com.santechture.pojo.RequestMetaData;
import com.santechture.pojo.ResponseMetaData;
import com.santechture.pojo.StatusEnum;
import com.santechture.pojo.TypeEnum;
import com.santechture.repository.RequestRepository;
import com.santechture.request.Activity;
import com.santechture.request.Claim;
import io.agroal.api.AgroalDataSource;
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
    AgroalDataSource dataSource;
    @Inject
    ExecutorService executorService;
    @Inject
    ObjectMapper objectMapper;

    private PriceRequest getPriceRequest(UUID requestId, StatusEnum status, Timestamp createdAt, String receiverId, String claimId, String traceId, TypeEnum typeEnum) {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setPriceRequestId(requestId);
        priceRequest.setStatus(status);
        priceRequest.setCreatedAt(createdAt);
        priceRequest.setFacility(receiverId);
        priceRequest.setClaimId(claimId);
        priceRequest.setTraceId(traceId);
        priceRequest.setRequestType(typeEnum);
        return priceRequest;
    }

    private void saveActivities(PriceRequest priceRequest, List<Activity> activities) {
        if (activities != null) {
            priceRequest.setActivities(new ArrayList<>());
            for (var activity : activities) {
                var act = new PriceActivity();
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

    @ActivateRequestContext
    @Transactional
    public void save(RequestMetaData requestMetaData) {
        log.info("Saving request ");
        try {
            PriceRequest priceRequest = repository.findByPriceRequestId(requestMetaData.getRequestId());
            if (priceRequest != null) {
                log.info("Request Exist");
                priceRequest.setCreatedAt(requestMetaData.getCreatedAt());
                priceRequest.setStatus(StatusEnum.FINISHED);
                priceRequest.setFacility(requestMetaData.getClaim().getReceiverID());
                priceRequest.setClaimId(requestMetaData.getClaim().getId() != null ? requestMetaData.getClaim().getId().toString() : "");
                priceRequest.setTraceId(span.getSpanContext().getTraceId());
                priceRequest.setRequestType(requestMetaData.getType());
                PriceBody priceBody = priceRequest.getBody();
                if (priceBody != null) {
                    priceBody.setRequest(objectMapper.writeValueAsString(requestMetaData.getClaim()));
                }

            } else {
                log.info("Request Not Exist");
                priceRequest = getPriceRequest(requestMetaData.getRequestId(),
                        StatusEnum.IN_PROGRESS,
                        requestMetaData.getCreatedAt(),
                        requestMetaData.getClaim().getReceiverID(),
                        requestMetaData.getClaim().getId() != null ? requestMetaData.getClaim().getId().toString() : "",
                        span.getSpanContext().getTraceId(),
                        requestMetaData.getType());
                PriceBody priceBody = new PriceBody(priceRequest, objectMapper.writeValueAsString(requestMetaData.getClaim()), null, null);
                priceRequest.setBody(priceBody);
            }


//            log.info(objectMapper.writeValueAsString(requestMetaData.getClaim()));

            var activities = requestMetaData.getClaim().getActivity();
            saveActivities(priceRequest, activities);
            repository.persist(priceRequest);
        } catch (Exception e) {
            log.error("Error saving request", e);
            log.error(e.getMessage());

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
                    alert.setPriceObjectId(activity.getPriceActivityId());
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

    private void updateActivities(PriceRequest priceRequest, List<Activity> activities) {
        var priceActivities = priceRequest.getActivities();
        for (var activity : activities) {
            var priceActivity = priceActivities.stream().filter(pa -> pa.getPriceActivityId().equals(activity.getPriceActivityId())).findFirst().orElse(null);
            if (priceActivity != null) {
                priceActivity.setNet(activity.getNet() != null ? Double.toString(activity.getNet()) : "");
                priceActivity.setCopayment(activity.getCopayment());
                priceActivity.setProviderPatientShare(activity.getProviderPatientShare());
                priceActivity.setDeductible(activity.getDeductible());
                priceActivity.setDiscountPercentage(activity.getDiscountPercentage());
                priceActivity.setDiscount(activity.getDiscount());
                priceActivity.setSpcFactor(activity.getSPCFactor());
                priceActivity.setGross(activity.getGross());
                priceActivity.setList(activity.getList());
                priceActivity.setListPricePredefined(activity.getListPricePredifined());
                priceActivity.setAnaesthesiaBaseUnits(activity.getAnaesthesiaBaseUnits());
                priceActivity.setPatientShare(activity.getPatientShare());
                priceActivity.setHike(activity.getHike());
                priceActivity.setHikeAmount(activity.getHikeAmount());

            }
        }
    }

    private void saveSuccessResponse(ResponseMetaData responseMetaData, PriceRequest priceRequest) {
        priceRequest.setStatus(StatusEnum.FINISHED);
        if (priceRequest.getBody() != null) {
            if (responseMetaData.getClaim() != null) {
                try {
                    var claim = responseMetaData.getClaim();
                    priceRequest.setGross(claim.getGross());
                    priceRequest.setPatientShare(claim.getPatientShare());
                    priceRequest.setNet(claim.getNet());
                    priceRequest.setMultipleProcedures(claim.getMultipleProcedures());
                    priceRequest.setPrimaryProc(claim.getPrimaryProc());
                    priceRequest.setSecondaryProc(claim.getSecondaryProc());
                    priceRequest.setThirdProc(claim.getThirdProc());
                    priceRequest.setForthProc(claim.getForthProc());
                    priceRequest.setCusDentalId(claim.getCUS_DENTAL_ID());
                    priceRequest.setSpcId(claim.getSPC_ID());
                    priceRequest.setCusId(claim.getCUS_ID());
                    priceRequest.setMemberId(claim.getMemberID());
                    priceRequest.getBody().setResponse(objectMapper.writeValueAsString(claim));
                } catch (JsonProcessingException e) {
                    priceRequest.getBody().setResponse(null);
                    log.error("Couldn't serialize response body", e);
                }
            }
        }
        var claim = responseMetaData.getClaim();
        if (claim != null) {
            var alerts = new ArrayList<PriceAlert>();
            saveAlertsBasedClaim(priceRequest, claim, alerts);
            var activities = claim.getActivity();
            if (activities != null) {
                updateActivities(priceRequest, activities);
                saveAlertsBasedActivity(priceRequest, activities, alerts);
            }
            priceRequest.setAlerts(alerts);
        }

    }

    private void saveFailureResponse(ResponseMetaData responseMetaData, PriceRequest priceRequest) {
        priceRequest.setStatus(StatusEnum.FAILED);
        if (responseMetaData.getResponse() != null) {
            priceRequest.getBody().setResponse(responseMetaData.getResponse());
        }

    }

    @ActivateRequestContext
    @Transactional
    public void saveResponse(ResponseMetaData responseMetaData) {
        log.info("Saving response ");
        try {
            PriceRequest priceRequest = repository.findByPriceRequestId(responseMetaData.getRequestId());
            priceRequest.setFinishedAt(Timestamp.from(Instant.now()));
            priceRequest.setProcessTime(responseMetaData.getProcessTime());

            if (responseMetaData.getStatus() < 400) {
                priceRequest.setVisitId(responseMetaData.getClaim().getVisitId());
                saveSuccessResponse(responseMetaData, priceRequest);
            } else {
                saveFailureResponse(responseMetaData, priceRequest);
            }
            repository.persist(priceRequest);
        } catch (Exception e) {
            log.error("Error saving response", e);
        }
    }

    @ActivateRequestContext
    @Transactional
    public void saveFacts(UUID uuid, String facts) {
        log.info("Saving facts for request " + uuid.toString());
        try {
            PriceRequest priceRequest = repository.findByPriceRequestId(uuid);
            priceRequest.getBody().setFacts(facts);

//            log.info("facts "+facts);
            repository.persist(priceRequest);
            log.info(String.valueOf(repository.isPersistent(priceRequest)));
//            log.info("facts "+priceRequest.getBody().getFacts());
            log.info("Finished saving facts for request " + uuid.toString());
        } catch (Exception e) {
            log.error(String.format("Error while saving facts for request %s : %s", uuid.toString(), e.getMessage()));
            for (var stackTrace : e.getStackTrace()) {
                log.error(stackTrace.toString());
            }
        }
    }
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
}
