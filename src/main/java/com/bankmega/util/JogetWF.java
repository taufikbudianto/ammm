/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankmega.util;

import com.bankmega.model.AccountMaintenanceData;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Taufik
 */
public class JogetWF extends AbstractManagedBean {

    @Value("${var.complete}")
    private String urlComplete;

    public String generateActId(String id) {
        Connection con = getKoneksiMysql();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("select id from shkactivities where ProcessId=? ORDER by id desc limit 1");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String complete(Map<String, String> map, String actId) {
        String param = "";
        for (Map.Entry<String, String> pair : map.entrySet()) {
            param = param + pair.getKey() + "=" + pair.getValue() + "&";
        }
        String parameters = param.substring(0, param.length() - 1);
        String url = urlComplete + actId + "?" + parameters;
        System.out.println(url);
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json;charset=utf-8")
                    .header("Content-Length", "199")
                    .header("Server", "Apache-Coyote/1.1")
                    .asJson();
        } catch (UnirestException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return "sukses";
    }

    public String completeId(Map<String, String> map, String actId) {
        String param = "";
        String status = "";
        String response = "";
        for (Map.Entry<String, String> pair : map.entrySet()) {
            param = param + pair.getKey() + "=" + pair.getValue() + "&";
        }
        String parameters = param.substring(0, param.length() - 1);
        String compUrl = urlComplete + actId + "?" + parameters;
        URL url;
        System.out.println("url nya" + compUrl + "act ID" + actId + "parameter" + parameters);
        try {
            url = new URL(compUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            System.out.println(con.getErrorStream());
            int responseCode = con.getResponseCode();
            JSONParser parser = new JSONParser();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = br.readLine()) != null) {
                    JSONObject obj = (JSONObject) parser.parse(line.toString());
                    status = (String) obj.get("status");
                    System.out.println(status);
                    response += line;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception;
            e.printStackTrace();
        }
        return status;
    }

    public boolean updateTempFlag(Map<String, String> map) {
        // TODO Auto-generated method stub
        String noTiket = map.get("notiket");
        String route = map.get("route");
        String assignto = map.get("assignto");
        String fieldUpdate = "";
        Connection con = getKoneksiMysql();
        if (route.equals("am")) {
            AccountMaintenanceData da = data(noTiket);
            fieldUpdate = "c_assigntoAnalyst";
            String updateAM = "update app_fd_accountmaint set c_pendingAnalyst='true', c_txtCardNo=?,"
                    + "c_cardLimit=?,c_txtCardHolderNm=?,c_dtTglReq=?,"
                    + "c_fldJenisLaporan=?,c_fldNamaNasabah=?,c_fldMemoCCBM=?,c_fldCommentCCBM=?,"
                    + "c_subJenisLaporan=? where c_ticketNo=?";

            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(updateAM);
                ps.setString(1, da.getC_txtCardNo());
                ps.setString(2, da.getC_cardLimit());
                ps.setString(3, da.getC_txtCardHolderNm());
                ps.setString(4, da.getC_dtTglReq());
                ps.setString(5, da.getC_fldJenisLaporan());
                ps.setString(6, da.getC_fldNamaNasabah());
                ps.setString(7, da.getC_fldMemoCCBM());
                ps.setString(8, da.getC_fldCommentCCBM());
                ps.setString(9, da.getC_subJenisLaporan());
                ps.setString(10, noTiket);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } else if (route.equals("dm")) {
            fieldUpdate = "c_assigntoDm";
        } else if (route.equals("rmu")) {
            fieldUpdate = "c_assigntoRMU";
        }
        String updateAppfd = "update app_fd_accountmaint set " + fieldUpdate + "=?,c_flagWfControl=?,c_pendingAfterAdmin=? where c_ticketNo=?";
        boolean data = false;
        String sql = "update tbl_acctmaintenancetemp  set flagWF=? where ticketNo=?";
        try {
            PreparedStatement ps1 = con.prepareStatement(updateAppfd);
            ps1.setString(1, assignto);
            ps1.setString(2, route);
            ps1.setString(3, "true");
            ps1.setString(4, noTiket);
            ps1.executeUpdate();
            PreparedStatement ps2 = con.prepareStatement(sql);
            ps2.setString(1, "open");
            ps2.setString(2, noTiket);
            ps2.executeUpdate();
            data = true;
        } catch (Exception e) {
            data = false;
            // TODO: handle exception
            log.error(e.getMessage());
        }
        return data;
    }

    public AccountMaintenanceData data(String tiket) {
        String sql = "select * from tbl_acctmaintenancetemp where ticketNo = '" + tiket + "'";
        Connection con = getKoneksiMysql();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AccountMaintenanceData dataAmsave = new AccountMaintenanceData();
                dataAmsave.setC_assignToUser("true");
                dataAmsave.setC_txtCardNo(rs.getString("cardNum"));
                dataAmsave.setC_cardLimit(rs.getString("cardLimit"));
                dataAmsave.setC_txtNoTicket(rs.getString("ticketNo"));
                dataAmsave.setC_txtCardHolderNm(rs.getString("custName"));
                dataAmsave.setC_dtTglReq(rs.getString("tanggalLapor"));
                dataAmsave.setC_fldJenisLaporan(rs.getString("jnsLaporan"));
                dataAmsave.setC_fldNamaNasabah(rs.getString("custName"));
                dataAmsave.setC_fldMemoCCBM(rs.getString("memoCCBM"));
                dataAmsave.setC_fldCommentCCBM(rs.getString("commentCCBM"));
                dataAmsave.setC_subJenisLaporan(rs.getString("subJnsLaporan"));

                return dataAmsave;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
