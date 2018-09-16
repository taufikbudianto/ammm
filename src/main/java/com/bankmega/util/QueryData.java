/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankmega.util;

import com.bankmega.model.CekTiket;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 *
 * @author Taufik
 */
@Component
public class QueryData extends AbstractManagedBean{

    private HSSFWorkbook wb;

    public Map<String, String> findticket(String tiket) throws ClassNotFoundException {
        Connection con = getKoneksiMysql();
        Map<String, String> map = new HashMap<>();
        String sql = "select count(*)as jumlah,assignedTo from tbl_acctmaintenancetemp  where ticketNo='" + tiket + "'";
        String balikan = "";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int jum = rs.getInt("jumlah");
                String assignTo = rs.getString("assignedTo");

                if (jum == 0) {
                    balikan = "Ticket Not Found";
                    map.put("balikan", balikan);
                    map.put("assignedTo", "null");
                } else {
                    balikan = "Sukses";
                    map.put("balikan", balikan);
                    map.put("assignedTo", assignTo);

                    // PreparedStatement ps2 = con.prepareStatement(sqlUpdate);
                    // ps2.setString(1, tiket);
                    // ps2.executeUpdate();
                }
            }
            ps.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    public List<CekTiket> getUpdate(String qry, String data) throws ClassNotFoundException {
        Connection con = getKoneksiMysql();
        String sql = "select assignedTo,ticketNo from tbl_acctmaintenancetemp where ticketNo in " + data;
        System.out.println(sql);
        List<CekTiket> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(qry);
            ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(sql);
            ResultSet rs = ps2.executeQuery();
            while (rs.next()) {
                CekTiket cek = new CekTiket();
                cek.setAssgnTo(rs.getString("assignedTo"));
                cek.setTiket(rs.getString("ticketNo"));
                list.add(cek);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public void writeExcel(List<CekTiket> tiket, List<CekTiket> tiket2, String val) {
        String sheetName = "To WF";//name of sheet
        String sheetName2 = "To CCBM";
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFSheet sheet2 = wb.createSheet(sheetName2);
        int rw = 1;
        int rw2 = 1;
        HSSFRow row3 = sheet.createRow(0);
        row3.createCell(0).setCellValue("Nomer Tiket");
        row3.createCell(1).setCellValue("Status Database Joget");
        row3.createCell(2).setCellValue("AssignTo");
        HSSFRow row4 = sheet2.createRow(0);
        row4.createCell(0).setCellValue("Nomer Tiket");
        row4.createCell(1).setCellValue("Status Database Joget");
        row4.createCell(2).setCellValue("AssignTo");
        for (CekTiket tik : tiket) {
            HSSFRow row = sheet.createRow(rw);
            if (rw == 0) {
                HSSFCell cell = row.createCell(0);
                cell.setCellValue("Nomer Tiket");
                row.createCell(1).setCellValue("Status Database Joget");
                row.createCell(2).setCellValue("AssignTo");
            } else if (rw > 0) {
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(tik.getTiket());
                row.createCell(1).setCellValue(tik.getBalikan());
                row.createCell(2).setCellValue(tik.getAssgnTo());
            }
            rw++;
        }
        for (CekTiket tik : tiket2) {
            HSSFRow row = sheet2.createRow(rw2);
            if (rw2 == 0) {
                HSSFCell cell = row.createCell(0);
                cell.setCellValue("Nomer Tiket");
                row.createCell(1).setCellValue("Status Database Joget");
                row.createCell(2).setCellValue("AssignTo");
            } else if (rw2 > 0) {
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(tik.getTiket());
                row.createCell(1).setCellValue(tik.getBalikan());
                row.createCell(2).setCellValue(tik.getAssgnTo());
            }
            rw2++;
        }
        FileOutputStream fileOut;
        try {
            Date dnow = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            String name2 = "AccountmaintenanceFeeding" + sf.format(dnow);
            fileOut = new FileOutputStream("D:\\xampp\\htdocs\\UploadAM\\create\\" + name2 + ".xls");
            //write this workbook to an Outputstream.
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
