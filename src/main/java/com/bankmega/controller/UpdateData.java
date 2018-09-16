/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankmega.controller;

import com.bankmega.model.CekTiket;
import com.bankmega.util.Filename2;
import com.bankmega.util.QueryData;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Taufik
 */
@RestController
public class UpdateData {

    @Autowired
    QueryData qry;
    @Value("${mega.pendinglist}")
    private String projectUrl;
    @Value("${mega.urlerror}")
    private String projectUrlError;
    ModelAndView modelAndView;
    private XSSFWorkbook myWorkBook;
    private HSSFWorkbook wb;
    private HSSFWorkbook wb2;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFileHandler(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File dir = new File("D:\\UploadAM");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                List<CekTiket> list1 = new ArrayList<>();
                List<CekTiket> list2 = new ArrayList<>();
                File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Filename2 homepage = new Filename2(serverFile.getPath(), '/', '.');
                String extension = homepage.extension();
                System.out.println(extension);
                if (extension.equals("xls")) {
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    InputStream ExcelFileToRead = new FileInputStream(
                            new File("D:\\UploadAM\\" + file.getOriginalFilename()));
                    wb = new HSSFWorkbook(ExcelFileToRead);
                    Map<String, Object> map = new HashMap<String, Object>();
                    List<CekTiket> data = new ArrayList<>();
                    HSSFSheet sheet = wb.getSheetAt(0);
                    HSSFRow row;
                    HSSFCell cell;

                    Iterator<?> rows = sheet.rowIterator();

                    while (rows.hasNext()) {
                        row = (HSSFRow) rows.next();
                        Iterator<?> cells = row.cellIterator();
                        CekTiket cek = new CekTiket();
                        while (cells.hasNext()) {
                            cell = (HSSFCell) cells.next();

                            if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                cek.setTiket(cell.getStringCellValue());
                                Map<String, String> map222 = qry.findticket(cell.getStringCellValue());
                                String val = map222.get("balikan");
                                String assignTo = map222.get("assignedTo");
                                cek.setAssgnTo(assignTo);
                                cek.setBalikan(val);
                                if (val.equals("Sukses")) {
                                    cek.setDatabase("found");
                                } else {
                                    cek.setDatabase("Not Found In Database");
                                }
                            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                System.out.print(cell.getNumericCellValue() + " ");
                            } else {

                            }
                        }
                        data.add(cek);
                    }
                    for (CekTiket tiket : data) {
                        String tiket1 = tiket.getTiket();
                        String found = tiket.getBalikan();
                        String assignto = tiket.getAssgnTo();
                        System.out.println("test=====" + found);
                        CekTiket update = new CekTiket();
                        if (found.equals("Sukses")) {

                            update.setAssgnTo(assignto);
                            update.setBalikan(found);
                            update.setTiket(tiket1);
                            list1.add(tiket);
                        } else {
                            update.setAssgnTo(assignto);
                            update.setBalikan(found);
                            update.setTiket(tiket1);
                            list2.add(tiket);
                        }
                    }
                    qry.writeExcel(list1, list2, "found");
                    map.put("data", data);
                    modelAndView = new ModelAndView("viewdata");
                    modelAndView.addObject("lists", data);
                } else if (extension.equals("xlsx")) {
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    FileInputStream fis = new FileInputStream(serverFile);
                    myWorkBook = new XSSFWorkbook(fis);
                    XSSFSheet mySheet = myWorkBook.getSheetAt(0);
                    Iterator<Row> rowIterator = mySheet.iterator();

                    List<CekTiket> data = new ArrayList<>();
                    while (rowIterator.hasNext()) {
                        CekTiket cek = new CekTiket();
                        Row row = rowIterator.next();
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_STRING:
                                    cek.setTiket(cell.getStringCellValue());
                                    Map<String, String> map222 = qry.findticket(cell.getStringCellValue());
                                    String val = map222.get("balikan");
                                    String assignTo = map222.get("assignedTo");
                                    cek.setAssgnTo(assignTo);
                                    cek.setBalikan(val);
                                    if (val.equals("Sukses")) {
                                        cek.setDatabase("Found");
                                    } else {
                                        cek.setDatabase("Not Found In Database");
                                    }
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    System.out.print(cell.getNumericCellValue() + "\t");
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                    System.out.print(cell.getBooleanCellValue() + "\t");
                                    break;

                                default:
                            }

                        }
                        data.add(cek);
                    }
                    for (CekTiket tiket : data) {
                        String tiket1 = tiket.getTiket();
                        String found = tiket.getBalikan();
                        String assignto = tiket.getAssgnTo();
                        System.out.println("test=====" + found);
                        CekTiket update = new CekTiket();
                        if (found.equals("Sukses")) {
                            update.setAssgnTo(assignto);
                            update.setBalikan(found);
                            update.setTiket(tiket1);
                            list1.add(tiket);
                        } else {
                            update.setAssgnTo(assignto);
                            update.setBalikan(found);
                            update.setTiket(tiket1);
                            list2.add(tiket);
                        }
                    }
                    qry.writeExcel(list1, list2, "found");
                    modelAndView = new ModelAndView("viewdata");
                    modelAndView.addObject("lists", data);

                } else {
                    modelAndView = new ModelAndView("redirect:" + projectUrlError + "format");
                }
                return modelAndView;
            } catch (Exception e) {
                return new ModelAndView("redirect:" + projectUrl);
            }
        } else {
            return new ModelAndView("redirect:" + projectUrlError + "kosong");
        }
    }

    @RequestMapping(value = "/updateWF")
    public ModelAndView updateData() throws ClassNotFoundException {
        Date dnow = new Date();//AccountmaintenanceFeeding20170919
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String name = "AccountmaintenanceFeeding" + sf.format(dnow) + ".xls";
        System.out.println(name);
        StringBuilder sb = new StringBuilder();
        /**/
        InputStream ExcelFileToRead;
        try {
            HSSFRow row;
            //HSSFCell cell;
            ExcelFileToRead = new FileInputStream("D:\\xampp\\htdocs\\UploadAM\\create\\" + name);
            wb2 = new HSSFWorkbook(ExcelFileToRead);
            HSSFSheet sheet = wb2.getSheetAt(0);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    Cell celll = row.getCell(0);
                    if (celll != null) {
                        System.out.println("test" + celll.getStringCellValue());
                        sb.append("'").append(celll.getStringCellValue()).append("'").append(",");
                    }
                }
            }
            /*Iterator<?> rows = sheet.rowIterator();
			while (rows.hasNext())
			{
				row=(HSSFRow) rows.next();
				Iterator<?> cells = row.cellIterator();
				cell=(HSSFCell) cells.next();
				System.out.println(cell.getStringCellValue()+" ");
				sb.append("'").append(cell.getStringCellValue()).append("'").append(",");
			}*/
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int pjg = sb.length();
        String test = sb.toString();
        String data = "(" + test.substring(14, pjg - 1) + ")";
        String sqlUpdate = "update tbl_acctmaintenancetemp set assignedTo='KK-Account Maintenance',"
                + " updateManual='Update Manual' where ticketNo in  " + data
                + " and flagCCBM in ('Open','In Progress')";
        List<CekTiket> cek = qry.getUpdate(sqlUpdate, data);
        ModelAndView model = new ModelAndView("viewupdate");
        model.addObject("lists", cek);
        return model;
    }

}
