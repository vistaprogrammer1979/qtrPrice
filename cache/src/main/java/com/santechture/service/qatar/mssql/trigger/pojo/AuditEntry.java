package com.santechture.service.qatar.mssql.trigger.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class AuditEntry {
    private Long id;
    private String tableName;
    private String operation;
    private Long recordId;
    private String oldData;
    private String newData;
    private OffsetDateTime changedAt;
    private String changedBy;
    private Integer oldDataSize;
    private Integer newDataSize;
}