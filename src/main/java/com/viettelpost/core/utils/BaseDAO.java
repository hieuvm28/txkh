package com.viettelpost.core.utils;

import com.viettelpost.core.base.VtException;
import oracle.jdbc.internal.OracleTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional
public class BaseDAO {
    protected Session getSession(SessionFactory sf) {
        return sf.getCurrentSession();
    }

    protected Connection getConnection(SessionFactory sf) {
        return ((SessionImpl) sf.getCurrentSession()).connection();
    }

    protected ResultSet getResultSet(SessionFactory em, String procedureNameNoParam, List<Object> params, int outType) throws Exception {
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, outType, 1);
        statement.setQueryTimeout(25000);
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
        }
        statement.setFetchSize(10000);
        statement.execute();
        if (outType != 0) {
            ResultSet resultSet = (ResultSet) statement.getObject(outIndex);
            return resultSet;
        } else {
            return null;
        }
    }

    protected Object getResultObject(SessionFactory em, String procedureNameNoParam, List<Object> params, int outType) throws Exception {
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, outType, 1);
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
        }
        statement.setFetchSize(10000);
        statement.execute();
        if (outType != 0) {
            return statement.getObject(outIndex);
        } else {
            return null;
        }
    }

    protected List<ResultSet> getResultSet2(SessionFactory em, String procedureNameNoParam, List<Object> params, int count) throws Exception {
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, OracleTypes.CURSOR, count);
        statement.setQueryTimeout(120);
        List<ResultSet> result = new ArrayList<>();
        int outIndex = (params == null ? 0 : params.size());
        statement.setFetchSize(10000);
        statement.execute();
        while (outIndex < params.size() + count) {
            ResultSet resultSet = (ResultSet) statement.getObject(++outIndex);
            result.add(resultSet);
        }
        return result;
    }

    protected Object[] getListResult(SessionFactory em, String procedureNameNoParam, List<Object> params, int[] listOut) throws Exception {
        int count = listOut.length;
        Object[] arr = new Object[count];
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, count, listOut);
        statement.setQueryTimeout(2500);
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
        }
        statement.setFetchSize(10000);
        statement.execute();
        for (int i = 0; i < count; i++) {
            arr[i] = statement.getObject(outIndex++);
        }
        return arr;
    }

    private CallableStatement buildStatement(SessionFactory em, String procedureNameNoParam, List<Object> params, int count, int[] lstOut) throws Exception {
        Connection connection = getConnection(em);
        StringBuilder sql = new StringBuilder("{call ").append(procedureNameNoParam);
        sql.append("(");
        if (params != null) {
            int lenth = params.size() + count;
            if (lstOut == null || lstOut.length == 0) {
                lenth--;
            }
            for (int i = 0; i < lenth; i++) {
                if (i == (lenth - 1)) {
                    sql.append("?");
                } else {
                    sql.append("?, ");

                }
            }
        } else {
            if (lstOut != null && lstOut.length > 0) {
                sql.append("?");
                if (count > 1) {
                    sql.append(", ?");
                }
            }
        }
        sql.append(")}");
        CallableStatement statement = connection.prepareCall(sql.toString());
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
            for (int i = 0; i < params.size(); i++) {
                int j = i + 1;
                Object b = params.get(i);
                if (b != null) {
                    if (b instanceof java.util.Date) {
                        statement.setTimestamp(j, new java.sql.Timestamp(((java.util.Date) b).getTime()));
                    } else {
                        if (b instanceof String) {
                            statement.setString(j, (String) b);
                        } else if (b instanceof Long) {
                            statement.setLong(j, (Long) b);
                        } else if (b instanceof Double) {
                            statement.setDouble(j, (Double) b);
                        } else {
                            statement.setInt(j, Integer.valueOf(b.toString()));
                        }
                    }
                } else {
                    statement.setObject(j, b);
                }
            }
        }
        if (lstOut != null && lstOut.length > 0) {
            for (int i = 0; i < lstOut.length; i++) {
                statement.registerOutParameter(outIndex + i, lstOut[i]);
            }
        }
        return statement;
    }

    private CallableStatement buildStatement(SessionFactory em, String procedureNameNoParam, List<Object> params, int outType, int count) throws Exception {
        Connection connection = getConnection(em);
        StringBuilder sql = new StringBuilder("{call ").append(procedureNameNoParam);
        sql.append("(");
        if (params != null) {
            int lenth = params.size() + count;
            if (outType == 0) {
                lenth--;
            }
            for (int i = 0; i < lenth; i++) {
                if (i == (lenth - 1)) {
                    sql.append("?");
                } else {
                    sql.append("?, ");

                }
            }
        } else {
            if (outType != 0) {
                sql.append("?");
                if (count > 1) {
                    sql.append(", ?");
                }
            }
        }
        sql.append(")}");
        CallableStatement statement = connection.prepareCall(sql.toString());
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
            for (int i = 0; i < params.size(); i++) {
                int j = i + 1;
                Object b = params.get(i);
                if (b != null) {
                    if (b instanceof Date) {
                        statement.setTimestamp(j, new java.sql.Timestamp(((java.util.Date) b).getTime()));
                    } else {
                        if (b instanceof String) {
                            statement.setString(j, (String) b);
                        } else if (b instanceof Long) {
                            statement.setLong(j, (Long) b);
                        } else if (b instanceof Double) {
                            statement.setDouble(j, (Double) b);
                        } else {
                            statement.setInt(j, Integer.valueOf(b.toString()));
                        }
                    }
                } else {
                    statement.setObject(j, b);
                }
            }
        }
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                statement.registerOutParameter(outIndex + i, outType);
            }
        }
        return statement;
    }

    protected ResultSet[] getListResultSet(SessionFactory em, String procedureNameNoParam, List<Object> params, int count) throws Exception {
        ResultSet[] arr = new ResultSet[count];
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, count > 0 ? OracleTypes.CURSOR : OracleTypes.NULL, count);
        statement.setQueryTimeout(2500);
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
        }
        statement.setFetchSize(10000);
        statement.execute();
        for (int i = 0; i < count; i++) {
            arr[i] = (ResultSet) statement.getObject(outIndex++);
        }
        return arr;
    }

    protected BigDecimal getBigDecimal(SessionFactory em, String procedureNameNoParam, List<Object> params, int outType) throws Exception {
        CallableStatement statement = buildStatement(em, procedureNameNoParam, params, outType, 1);
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
        }
        statement.setFetchSize(10000);
        statement.execute();
        if (outType != 0) {
            BigDecimal result = (BigDecimal) statement.getObject(outIndex);
            return result;
        } else {
            return null;
        }
    }

    protected List toJsonArray(ResultSet rs) throws Exception {
        List jA = new ArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int total = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> jO = new HashMap<>();
            for (int i = 0; i < total; i++) {
                buidObject(rs, metaData, jO, format, i);
            }
            jA.add(jO);
        }
        return jA;
    }

    protected Map<String, Object> toJsonObject(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int total = metaData.getColumnCount();
        if (rs.next()) {
            Map<String, Object> jO = new HashMap<>();
            for (int i = 0; i < total; i++) {
                buidObject(rs, metaData, jO, format, i);
            }
            return jO;
        }
        return null;
    }


    private void buidObject(ResultSet rs, ResultSetMetaData metaData, Map<String, Object> jO, DateFormat format, int i) throws Exception {
        String colName = metaData.getColumnName(i + 1);
        if (metaData.getColumnType(i + 1) == OracleTypes.TIMESTAMP || metaData.getColumnType(i + 1) == OracleTypes.DATE) {
            if (rs.getTimestamp(colName) == null) {
                jO.put(colName, null);
            } else {
                jO.put(colName, format.format(rs.getTimestamp(colName)));
            }
        } else {
            jO.put(colName, rs.getObject(colName));
        }
    }

    protected Object throwException(Exception e) throws Exception {
        if (e.getLocalizedMessage() != null) {
            if (e.getLocalizedMessage().contains("ORA-20001")
                    || e.getLocalizedMessage().contains("ORA-20100")
                    || e.getLocalizedMessage().contains("ORA-20000")
                    || e.getLocalizedMessage().contains("ORA-20006")
                    || e.getLocalizedMessage().contains("ORA-20007")
                    || e.getLocalizedMessage().contains("ORA-20002")
                    || e.getLocalizedMessage().contains("ORA-20580")
                    || e.getLocalizedMessage().contains("ORA-20")
                    || e.getLocalizedMessage().contains("ORA-01403")
            ) {
                throw new VtException(e.getLocalizedMessage());
            }
            if (e.getLocalizedMessage().contains("unique constraint")) {
                throw new VtException("ORA-EXCEPTION: Đã tồn tại");
            }
        }
        throw e;
    }
}