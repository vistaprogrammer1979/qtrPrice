package com.santechture.service.ksa.mssql;

import com.santechture.CusPriceListItem;
import com.santechture.request.CodeType;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Singleton
public class CusPriceListItemDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusPriceListItemDBClient.class);
    private static final String model = "CusPriceListItem";
    @Inject
    Logger logger;

    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;
    private static final String sql ="SELECT [ID] "
            + ", [PriceList_id]"
            + " , PL_NEW_CUS_PriceListItem.[type] AS [Type] "
            + " , PL_NEW_CUS_PriceListItem.code AS [Code]  "
            + " , [price] "
            + " , [discount] "
            + " , [startDate] "
            + " , [endDate] "
            + " , [SAUDI_VAT] "
            + " , [NONE_SAUDI_VAT]"
            + " ,PriceList_Id "
            + " , covered"
            + " , need_approval"
            + " FROM "
            + " [PL_NEW_CUS_PriceListItem] "
            + " where deleted = 0 ";
    private static final List<String> tables = List.of("PL_NEW_CUS_PriceListItem");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Integer type = resultSet.getInt("Type");
                        String code = resultSet.getString("Code");
                        Double price = resultSet.getDouble("price");
                        Long cusPricListId = resultSet.getLong("PriceList_Id");
                        Double discount = null;
                        if (resultSet.getObject("discount") != null) {
                            discount = resultSet.getDouble("discount");
                        }
                        Double saudiVat = null;
                        if (resultSet.getObject("SAUDI_VAT") != null) {
                            saudiVat = resultSet.getDouble("SAUDI_VAT");
                        }
                        Double noneSaudiVat = null;
                        if (resultSet.getObject("NONE_SAUDI_VAT") != null) {
                            noneSaudiVat = resultSet.getDouble("NONE_SAUDI_VAT");
                        }
                        boolean covered = resultSet.getInt("covered") == 1;
                        boolean need_Approval = resultSet.getInt("need_approval") == 1;

                        /*if(resultSet.wasNull()){
                         discount = null;
                         }*/
                        Date startDate = resultSet.getTimestamp("startDate");
                        Date endDate = resultSet.getTimestamp("endDate");
                        if (code == null) {
                            continue;
                        }
                        /*if (code.equalsIgnoreCase("92222")) {
                         Logger.getLogger(RepoUtils.class.getName()).log(Level.INFO, "test " + discount + " " + master.getID());
                         }
                         if ("17-03".equals(code)) {
                         Logger.getLogger(RepoUtils.class.getName()).log(Level.INFO, " priceListID=" + master.getID() + " code=" + code);
                         }*/
                        CusPriceListItem item = new CusPriceListItem(ID, cusPricListId, code,
                                CodeType.fromKSACodeType(com.santechture.request.ksa.CodeType.from(type)),
                                price, discount, startDate, endDate);
                        item.setSaudiVat(saudiVat);
                        item.setNoneSaudiVat(noneSaudiVat);
                        item.setCovered(covered);
                        item.setNeedApproval(need_Approval);
                        item.setStatus(com.santechture.request.Status.VALID);
                        result.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return result;
    }

    @Override
    public String getKey(Map<String, Object> map) {
        return String.format("%s:%s:%s", model, map.get("code"), map.get("pricListId"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }

    @Override
    public double calculateSize() {
        return 0;
    }

}
