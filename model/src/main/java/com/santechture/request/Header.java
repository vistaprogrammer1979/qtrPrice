package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santechture.converter.DateDeserializer;
import com.santechture.converter.DateSerializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Header implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="Server", required = false)
    private String server;
    @JsonProperty(value ="CallingApplication", required = false)
    private String callingApplication;
    @JsonProperty(value ="CallingApplicationVersion", required = false)
    private String callingApplicationVersion;
    @JsonProperty(value ="UserID")
    private Integer userID;
    @JsonProperty(value ="UserName", required = false)
    private String userName;
    @JsonProperty(value ="SenderID", required = true)
    private String senderID;
    @JsonProperty(value ="ReceiverID", required = true)
    private String receiverID;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="TransactionDate", required = true)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date transactionDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="BatchStartDate", required = false)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date batchStartDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="BatchEndDate", required = false)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date batchEndDate;
    @JsonProperty(value ="RecordCount", required = true)
    @Min(0)
    private Integer recordCount;
    @JsonProperty(value ="DispositionFlag", required = true)
    private String dispositionFlag;
    @JsonProperty(value ="top20", required = false)
    private Integer top20;
    @OneToOne(mappedBy = "headerID", cascade = CascadeType.PERSIST)
    @JsonProperty(value ="Workflow", required = false)
    private Workflow workflow;
    @OneToMany(mappedBy = "headerID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="ExtendedValidationType")
    private List<ExtendedValidationType> extendedValidationType;
    @JsonProperty(value ="ValidationType")
    private String validationType;
    @JoinColumn(name = "requestID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Request requestID;
    @OneToMany(mappedBy = "header", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<HeaderOutcome> outcome;
    public Header() {
    }
    public Header(Integer id) {
        this.id = id;
    }
    public String getServer() {
        return server;
    }
    public void setServer(String server) {
        this.server = server;
    }
    public String getCallingApplication() {
        return callingApplication;
    }
    public void setCallingApplication(String callingApplication) {
        this.callingApplication = callingApplication;
    }
    public String getCallingApplicationVersion() {
        return callingApplicationVersion;
    }
    public void setCallingApplicationVersion(String callingApplicationVersion) {
        this.callingApplicationVersion = callingApplicationVersion;
    }
    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Request getRequestID() {
        return requestID;
    }
    public void setRequestID(Request requestID) {
        this.requestID = requestID;
    }
    public String getSenderID() {
        return senderID;
    }
    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
    public String getReceiverID() {
        return receiverID;
    }
    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    public Date getBatchStartDate() {
        return batchStartDate;
    }
    public void setBatchStartDate(Date batchStartDate) {
        this.batchStartDate = batchStartDate;
    }
    public Date getBatchEndDate() {
        return batchEndDate;
    }
    public void setBatchEndDate(Date batchEndDate) {
        this.batchEndDate = batchEndDate;
    }
    public String getValidationType() {
        return validationType;
    }
    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }
    public List<ExtendedValidationType> getExtendedValidationType() {
        return extendedValidationType;
    }
    public void setExtendedValidationType(List<ExtendedValidationType> extendedValidationType) {
        this.extendedValidationType = extendedValidationType;
    }
    public Integer getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
    public String getDispositionFlag() {
        return dispositionFlag;
    }
    public void setDispositionFlag(String dispositionFlag) {
        this.dispositionFlag = dispositionFlag;
    }
    public Workflow getWorkflow() {
        return workflow;
    }
    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
    public Integer getTop20() {
        return top20;
    }
    public void setTop20(Integer top20) {
        this.top20 = top20;
    }
    public List<HeaderOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<HeaderOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new HeaderOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<HeaderOutcome>();
            this.outcome.add(new HeaderOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
}
