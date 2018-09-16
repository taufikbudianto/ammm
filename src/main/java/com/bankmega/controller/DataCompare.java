/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankmega.controller;

import com.bankmega.model.DataCompareModel;
import com.bankmega.util.AbstractManagedBean;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Taufik
 */
@RestController
public class DataCompare extends AbstractManagedBean {

    @CrossOrigin
    @RequestMapping(value = "/dataCompare", method = RequestMethod.GET)
    public List<DataCompareModel> listAllEmpMap(@RequestParam("name") String nama,
            @RequestParam("dob") String dob, @RequestParam("callback") String callback) {
        Map<String, Object> map = new HashMap<String, Object>();
        nama = URLDecoder.decode(nama);
        List<DataCompareModel> data = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(callback + "(");
        try {
            data = getAllData(nama, dob);
            sb.append(data);
            sb.append(");");
            if (data.isEmpty()) {
//                return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
            }
            map.put("datacompare", data);
//            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
//            return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
        }
        return data;

    }

    @CrossOrigin
    @RequestMapping(value = "/dataCompare/id", method = RequestMethod.GET)
    public List<DataCompareModel> listAllEmpMapId(@RequestParam("id") String id, @RequestParam("callback") String callback) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DataCompareModel> data = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(callback + "(");
        try {
            data = getAllDataId(id);
            sb.append(data);
            sb.append(");");
            if (data.isEmpty()) {
            }
            map.put("datacompare", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

    @CrossOrigin
    @RequestMapping(value = "/dataCompare/getdata", method = RequestMethod.GET)
    public Map listAllEmpMap(@RequestParam("kartu") String kartu, @RequestParam("callback") String callback) {
        Connection con = null;
        con = getKoneksi();
        Map<String, String> map = new HashMap<>();
        String query = "select c.card_dte_open,b.crdacct_blk_code,c.card_blk_code,d.bscr_scoring_nbr,b.CRDACCT_OUTSTD_AUTH_BAL, "
                + " b.CRDACCT_OUTSTD_INSTL,b.CRDACCT_OUTSTD_BAL,e.cust_mobile_phone from CURRENT_CC_SCMCACCP b join CURRENT_CC_SCMCARDP c on b.CRDACCT_NBR =c.CARD_ACCT_NBR "
                + " join CURRENT_CC_BMGBSCRP d on b.CRDACCT_NBR=d.bscr_acct_nbr join CURRENT_CC_SCMCUSTP e on e.cust_NBR=b.CRDACCT_CUST_NBR "
                + " WHERE c.CARD_NBR='" + kartu + "'";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put("os_bal", rs.getString("CRDACCT_OUTSTD_BAL"));
                map.put("os_ops", rs.getString("CRDACCT_OUTSTD_INSTL"));
                map.put("os_aut", rs.getString("CRDACCT_OUTSTD_AUTH_BAL"));
                map.put("acctxpac", rs.getString("crdacct_blk_code"));
                map.put("card", rs.getString("card_blk_code"));
                map.put("scoring", rs.getString("bscr_scoring_nbr"));
                map.put("phone", rs.getString("cust_mobile_phone"));
                map.put("dt_open", rs.getString("card_dte_open"));
            }
        } catch (Exception e) {

        }

        return map;
    }

    public List<DataCompareModel> getAllData(String name, String dob) {
        // TODO Auto-generated method stub
        String sql = "select c.CARD_STATUS,c.CARD_NBR,c.CARD_SHORT_NAME,c.CARD_DTE_OPEN,c.CARD_SHORT_NAME,b.CRDACCT_STMT_ADDR_FLG,b.CRDACCT_DTE_OPEN,b.CRDACCT_OUTSTD_AUTH_BAL,"
                + " b.CRDACCT_OUTSTD_INSTL,b.CRDACCT_OUTSTD_BAL,b.CRDACCT_CRLIMIT,"
                + " a.CUST_LOCAL_NAME, a.CUST_DTE_BIRTH,a.CUST_MOM_NAME,"
                + " ISNULL(RTRIM(a.CUST_EMP_ADDR1),'')+',    '+ISNULL(RTRIM(a.CUST_EMP_ADDR2),'') + isnull(RTRIM(a.CUST_EMP_ADDR3),'') + "
                + " ISNULL(RTRIM(a.CUST_EMP_ADDR4),'') as alamatKantor,"
                + " ISNULL(Rtrim(a.CUST_ADDR1),'')+',  ' + ISNULL(rtrim(a.CUST_ADDR2),'')+', '+ isnull(rtrim(a.CUST_ADDR3),'')+',  '+"
                + " isnull(rtrim(a.CUST_ADDR4),'') as alamatrumah from CURRENT_CC_SCMCACCP b join CURRENT_CC_SCMCUSTP a"
                + " on b.CRDACCT_CUST_NBR = a.CUST_NBR "
                + " join CURRENT_CC_SCMCARDP c on b.CRDACCT_NBR =c.CARD_ACCT_NBR WHERE UPPER(a.CUST_LOCAL_NAME) LIKE UPPER('%" + name + "%') and a.CUST_DTE_BIRTH=" + dob;

        List<DataCompareModel> listData = new ArrayList<>();
        Connection con = getKoneksi(); // TODO Auto-generated catch block
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DataCompareModel dta = new DataCompareModel();
                dta.setCustnbr(rs.getString("CARD_NBR"));
                dta.setCardstatus(rs.getString("CARD_STATUS"));
                dta.setCardopen(rs.getString("CARD_DTE_OPEN"));
                dta.setCustname(rs.getString("CARD_SHORT_NAME"));
                dta.setCUST_LOCAL_NAME(rs.getString("CUST_LOCAL_NAME"));
                dta.setCUST_DTE_BIRTH(rs.getString("CUST_DTE_BIRTH"));
                dta.setCUST_MOM_NAME(rs.getString("CUST_MOM_NAME"));
                dta.setAlamatKantor(rs.getString("alamatKantor"));
                dta.setAlamatRmah(rs.getString("alamatrumah"));
                dta.setCRDACCT_CRLIMIT(rs.getString("CRDACCT_CRLIMIT"));
                dta.setCRDACCT_OUTSTD_AUTH_BAL(rs.getString("CRDACCT_OUTSTD_AUTH_BAL"));
                dta.setCRDACCT_STMT_ADDR_FLG(rs.getString("CRDACCT_STMT_ADDR_FLG"));
                dta.setCRDACCT_OUTSTD_BAL(rs.getString("CRDACCT_OUTSTD_BAL"));
                dta.setCRDACCT_OUTSTD_INSTL(rs.getString("CRDACCT_OUTSTD_INSTL"));
                listData.add(dta);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listData;
    }
    public List<DataCompareModel> getAllDataId(String id) {
        String sql = "select c.CARD_STATUS,c.CARD_NBR,c.CARD_SHORT_NAME,c.CARD_DTE_OPEN,c.CARD_SHORT_NAME,b.CRDACCT_STMT_ADDR_FLG,b.CRDACCT_DTE_OPEN,b.CRDACCT_OUTSTD_AUTH_BAL,"
                + " b.CRDACCT_OUTSTD_INSTL,b.CRDACCT_OUTSTD_BAL,b.CRDACCT_CRLIMIT,"
                + " a.CUST_LOCAL_NAME, a.CUST_DTE_BIRTH,a.CUST_MOM_NAME,"
                + " ISNULL(RTRIM(a.CUST_EMP_ADDR1),'')+',    '+ISNULL(RTRIM(a.CUST_EMP_ADDR2),'') + isnull(RTRIM(a.CUST_EMP_ADDR3),'') + "
                + " ISNULL(RTRIM(a.CUST_EMP_ADDR4),'') as alamatKantor,"
                + " ISNULL(Rtrim(a.CUST_ADDR1),'')+',  ' + ISNULL(rtrim(a.CUST_ADDR2),'')+', '+ isnull(rtrim(a.CUST_ADDR3),'')+',  '+"
                + " isnull(rtrim(a.CUST_ADDR4),'') as alamatrumah from CURRENT_CC_SCMCACCP b join CURRENT_CC_SCMCUSTP a"
                + " on b.CRDACCT_CUST_NBR = a.CUST_NBR "
                + " join CURRENT_CC_SCMCARDP c on b.CRDACCT_NBR =c.CARD_ACCT_NBR WHERE TRIM(a.CUST_NBR)=" + id;

        List<DataCompareModel> listData = new ArrayList<>();
        Connection con = getKoneksi(); // TODO Auto-generated catch block
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DataCompareModel dta = new DataCompareModel();
                dta.setCustnbr(rs.getString("CARD_NBR"));
                dta.setCardstatus(rs.getString("CARD_STATUS"));
                dta.setCardopen(rs.getString("CARD_DTE_OPEN"));
                dta.setCustname(rs.getString("CARD_SHORT_NAME"));
                dta.setCUST_LOCAL_NAME(rs.getString("CUST_LOCAL_NAME"));
                dta.setCUST_DTE_BIRTH(rs.getString("CUST_DTE_BIRTH"));
                dta.setCUST_MOM_NAME(rs.getString("CUST_MOM_NAME"));
                dta.setAlamatKantor(rs.getString("alamatKantor"));
                dta.setAlamatRmah(rs.getString("alamatrumah"));
                dta.setCRDACCT_CRLIMIT(rs.getString("CRDACCT_CRLIMIT"));
                dta.setCRDACCT_OUTSTD_AUTH_BAL(rs.getString("CRDACCT_OUTSTD_AUTH_BAL"));
                dta.setCRDACCT_STMT_ADDR_FLG(rs.getString("CRDACCT_STMT_ADDR_FLG"));
                dta.setCRDACCT_OUTSTD_BAL(rs.getString("CRDACCT_OUTSTD_BAL"));
                dta.setCRDACCT_OUTSTD_INSTL(rs.getString("CRDACCT_OUTSTD_INSTL"));
                listData.add(dta);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listData;
    }

}
