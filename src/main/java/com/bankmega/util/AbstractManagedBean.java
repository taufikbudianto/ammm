package com.bankmega.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : taufik.budiyanto
 * @date : 14/09/2018  8:41
 */
public abstract class AbstractManagedBean extends AbstractManagedBeanKoneksi {

    protected Logger log = LoggerFactory.getLogger(getClass());
    @Value("${app.query.cek}")
    private String cekTiket;
    @Value("${app.name}")
    private String appname;
    @Value("${app.url}")
    private String urlassign;
    @Value("${app.name.id}")
    private String appid;

    protected Integer getData(String tiket){
        Connection con = getKoneksiMysql();
        Integer count = 0;
        try {
            PreparedStatement ps = con.prepareStatement(cekTiket+"'"+tiket+"'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count=rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    protected String generateID(Map<String, String> map) {
        String appname="";
        String urlg="";
        String appid="";
        if(map.get("data").equals("am")) {
            appname=appname;
            urlg=urlassign;
            appid=appid;
        }else {
            appname=map.get("data");
            urlg=urlassign;
            appid=map.get("appid");
        }
        String sql = "select XPDLVersion from shkxpdls where XPDLId='"+appname+"' order by XPDLUploadTime DESC limit 1";
        Connection conDb = getKoneksiMysql();
        String XPDLVersion = "";
        try {
            PreparedStatement ps = conDb.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                XPDLVersion=rs.getString("XPDLVersion");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //URL Json joget Generate ID
        String urlGenId=urlg+appname+":"+XPDLVersion+":"+appid;
        String param = "";
        String procid=null;
        String response="";
        for (Map.Entry<String, String> pair : map.entrySet()){
            //iterate over the pairs
            param=param+pair.getKey()+"="+pair.getValue()+"&";


        }
        int paramLength = param.length()-1;
        String parameters = param.substring(0, paramLength);
        URL url;
        try {
            url = new URL(urlGenId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            JSONParser parser = new JSONParser();
            if(responseCode==HttpURLConnection.HTTP_OK) {
                String line = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = br.readLine()) != null) {
                    JSONObject obj = (JSONObject) parser.parse(line.toString());
                    procid = (String) obj.get("processId");
                    response += line;
                    log.info(response);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage());
        }
        return procid;
    }
    
}
