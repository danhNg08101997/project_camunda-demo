package org.camunda.bpm.getstarted.loanapproval.utils;

//import com.service.process.utils.helper.SQLHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
public class Utility {
	public static final String saltSign = "a8909b39-af41-49a7-8785-ea889cfd0219";
	public static final String SHA256 = "SHA-256";
//	@Autowired
//    private SQLHelper sqlHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Dong cac statement duoc mo
     *
     * @param cs
     */
    public void closeStatement(CallableStatement cs) {
        try {
            if (cs != null)
                cs.close();
            cs = null;
        } catch (SQLException e) {
            // throw new JboException(e);
        }
    }
    
//    public String getSysPara(String CDNAME) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("p_CDNAME", CDNAME);
//        return sqlHelper.callFunction("GET_SYSPARA", params, String.class);
//    }
    
    public void setSysPara(String cdName, String cdVal, String userId) {
        CallableStatement statement = null;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            statement = connection.prepareCall("{ call SP_SET_SYSPARA(?,?,?) }");
            statement.setString(1, cdName);
            statement.setString(2, cdVal);
            statement.setString(3, userId);
            statement.execute();
        } catch (SQLException ex) {
        	ex.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }
    
    public static List<String> ImmutableList(String ...value) {
        return Collections.unmodifiableList(Arrays.asList(value));
    }
    
    public String signByUserId(String userId, String data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(SHA256);
            md.update((saltSign + userId + data).getBytes(StandardCharsets.UTF_8));
            return org.apache.xerces.impl.dv.util.Base64.encode(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[] getStartEndPaging(int pageNumber, int pageSize) {
        int[] paging = new int[2];
        if (pageNumber < 1) {
            paging[0] = 0;
        } else {
            paging[0] = (pageNumber - 1) * pageSize;
        }
        if (pageSize < 1) {
            paging[1] = 9;
        } else {
            paging[1] = pageNumber * pageSize + 1;
        }
        return paging;
    }
    
    public static String encryptPassword(String userName, String plainText) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update((userName.toUpperCase() + plainText).getBytes(StandardCharsets.UTF_8));
            byte[] raw = md.digest();
            String hash = Base64.getEncoder().encodeToString(raw);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }
    }
    
    public static String generatePassword(Integer length) {
        byte[] random = new byte[length];
        Random randomGenerator = new Random();
        StringBuffer buffer = new StringBuffer();

        randomGenerator.nextBytes(random);

        for (int j = 0; j < random.length; j++) {
            byte b1 = (byte) ((random[j] & 0xf0) >> 4);
            byte b2 = (byte) (random[j] & 0x0f);
            if (b1 < 10)
                buffer.append((char) ('0' + b1));
            else
                buffer.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                buffer.append((char) ('0' + b2));
            else
                buffer.append((char) ('A' + (b2 - 10)));
        }

        return (buffer.toString());
    }
    
    public void logSendmailStatus(String userID, String email, String type, String status, String errorDesc, String creater) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement statement = connection
                     .prepareCall("BEGIN " + "SYS_INSERTLOGSENDMAIL(?,?,?,?,?,?); " + "END;")) {
            statement.setString(1, userID);
            statement.setString(2, email);
            statement.setString(3, type);
            statement.setString(4, status);
            statement.setString(5, errorDesc);
            statement.setString(6, creater);
            statement.execute();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
}
