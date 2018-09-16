/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankmega.util;

import com.bankmega.model.GenericModel;
import com.bankmega.model.VariablePatch;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Taufik
 */
@Component
public class TempSaveRMU extends AbstractManagedBean {

    public Map<String, String> setDataVariablePatch(GenericModel mod, String tiket, String id) throws SQLException {
        Map<String, String> dataTemp = new HashMap<String, String>();
        Map<String, String> dataSend = new HashMap<>();
        String dataAssignTo = "";
        if (mod.getRoute().equals("dm")) {
            dataAssignTo = "c_assigntoDm";
        } else {
            dataAssignTo = "c_assigntoRMU";
        }
        StringBuffer sb;
        StringBuffer sbVal;
        
        Connection con = getKoneksiMysql();
        String sql = "select * from tbl_acctmaintenancetemp where ticketNo = '" + tiket+"'" ;
//        System.out.println(sql);
        PreparedStatement ps = con.prepareStatement(sql);
        List<Map<String, Object>> listdata = resultSetToArrayList(ps.executeQuery());
//        System.out.println(String.valueOf(listdata));
        for (Map<String, Object> m : listdata) {
//            String val = m.get("ticketno").toString();
            for (Map.Entry<String, Object> pair : m.entrySet()) {
                //iterate over the pairs
                String valData = "";
                try {
                    valData = pair.getValue().toString();
                    dataTemp.put(pair.getKey(), valData);
                } catch (NullPointerException e) {
                    // TODO: handle exception
                    valData = "-NA-";
                    dataTemp.put(pair.getKey(), valData);
                }
                // System.out.println("Key : "+pair.getKey()+"---->Value : "+valData);
            }
            sb = new StringBuffer();
            sbVal = new StringBuffer();
            VariablePatch var = new VariablePatch();
            var.ticketno = dataTemp.get("ticketno"); //Status Kartu
            var.cardstatus = dataTemp.get("cardStatus"); //Status Kartu
            var.custEmail = dataTemp.get("custEmail"); //CH Email
            var.custName = dataTemp.get("custName"); //CH Name
            var.tanggalLapor = dataTemp.get("tanggalLapor"); //Tanggal Lapor
            var.cardno = dataTemp.get("cardNum");//Nomor Kartu
            var.cardLimit = dataTemp.get("cardLimit"); //Limit Kartu
            var.memoCCBM = dataTemp.get("memoCCBM").replaceAll("'", "");//Memo CCBM
            //Patch Perubahan Data
            var.VPDRalamatrumah = dataTemp.get("Form3_Addr1_retur"); //Retur - Identifikasi Bahwa ini perubahan data alamat rumah
            var.VPDRalamatkantor = dataTemp.get("Form4_Addr1_retur"); //Retur - Identifikasi Bahwa ini perubahan data alamat kantor
            var.VPDRalamatrelasi = dataTemp.get("Form5_Addr1_retur"); //Retur - Identifikasi Bahwa ini perubahan data alamat relasi
            var.PDalamatrumah = dataTemp.get("Form3_Addr1"); //Regular - Identifikasi Bahwa ini perubahan data alamat rumah
            var.PDalamatkantor = dataTemp.get("Form4_Addr1"); //Regular - Identifikasi Bahwa ini perubahan data alamat kantor
            var.PDalamatrelasi = dataTemp.get("Form5_Addr1"); //Regular - Identifikasi Bahwa ini perubahan data alamat relasi
            var.VPDrumahkantor = dataTemp.get("Form1_TelHom");//Identifikasi Bahwa ini validasi perubahan data telp rumah
            var.VPDhp = dataTemp.get("Form2_TelMob"); //Identifikasi Bahwa ini validasi perubahan data telp hp
            var.VPNamaLengkap = dataTemp.get("Form6_CustName"); //Identifikasi Bahwa ini perubahan nama lengkap
            var.VPKTP = dataTemp.get("Form6_IDNbr"); //Identifikasi Bahwa ini perubahan ID/KTP
            var.VPDOB = dataTemp.get("Form6_DOB"); //Identifikasi Bahwa ini perubahan Tanggal Lahir
            var.VPNamaIbuKandung = dataTemp.get("Form6_MomName"); //Identifikasi Bahwa ini perubahan nama ibu kandung   		
            var.finaldatecardDtOpen = dataTemp.get("cardDtOpen");
            var.subJnsAll = dataTemp.get("subJnsLaporan");// Jenis Laporan
            var.jns = dataTemp.get("jnsLaporan");
            var.subjns = dataTemp.get("subJnsLaporan");

            List<String> vartemp = new ArrayList<String>();
            List<String> varMandat = new ArrayList<String>();
            List<String> varRMU = new ArrayList<String>();
            List<String> varPDAlamat = new ArrayList<String>();
            List<String> varPDRetur = new ArrayList<String>();
            List<String> varPDTelp = new ArrayList<String>();
            System.out.println("test var VPDRalamatrumah " + var.VPDRalamatrumah);
            if (var.VPDRalamatrumah != "-NA-") {
                vartemp.add("dataReturn");
                varPDRetur.add("rumah");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan alamat return rumah)");
                } else {
                    varRMU.add("Validasi RMU (Perubahan alamat return rumah)");
                }
            }
            if (var.VPDRalamatkantor != "-NA-") {
                vartemp.add("dataReturn");
                varPDRetur.add("kantor");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan alamat return kantor)");
                } else {
                    varRMU.add("Validasi RMU (Perubahan alamat return kantor)");
                }
            }
            if (var.VPDRalamatrelasi != "-NA-") {
                vartemp.add("dataReturn");
                varPDRetur.add("relasi");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan alamat Return relasi)");
                } else {
                    varRMU.add("Validasi RMU (Perubahan alamat Return relasi)");
                }
            }
            if (var.PDalamatrumah != "-NA-") {
                vartemp.add("alamat");
                varPDAlamat.add("rumah");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan alamat rumah)");
                } else {
                    varRMU.add("Validasi RMU (Perubahan alamat rumah)");
                }
            }
            if (var.PDalamatkantor != "-NA-") {
                vartemp.add("alamat");
                varPDAlamat.add("kantor");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan alamat kantor)");
                } else {
                    System.out.println(mod.getRoute());
                    varRMU.add("Validasi RMU (Perubahan alamat kantor)");
                }
            }
            if (var.PDalamatrelasi != "-NA-") {
                vartemp.add("alamat");
                varPDAlamat.add("relasi");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan alamat relasi)");
                } else {
                    varRMU.add("Validasi RMU (Perubahan alamat relasi)");
                }

            }
            if (var.VPDrumahkantor != "-NA-") {
                vartemp.add("telp");
                varPDTelp.add("rumah_kantor");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan Nomor Telepon Rumah/Kantor)");
                } else {
                    varRMU.add("Validasi RMU (Perubahan Nomor Telepon Rumah/Kantor)");
                }
            }
            if (var.VPDhp != "-NA-") {
                vartemp.add("telp");
                varPDTelp.add("hp");
                if (mod.getRoute().equals("dm")) {
                    varRMU.add("Validasi DM (Perubahan Nomor HP)");
                } else {
                    varRMU.add("Validasi RMU (Perubahan Nomor HP)");
                }

            }
            if (var.VPNamaLengkap != "-NA-") {
                vartemp.add("mandatoryData");
                varMandat.add("namaLengkap");
                varRMU.add("Direct To RMU (Perubahan Nama Lengkap)");
            }
            if (var.VPKTP != "-NA-") {
                vartemp.add("mandatoryData");
                varMandat.add("idNumber");
                varRMU.add("Direct To RMU (Perubahan Nomor ID/KTP)");
            }
            if (var.VPDOB != "-NA-") {
                vartemp.add("mandatoryData");
                varMandat.add("dob");
                varRMU.add("Direct To RMU (Perubahan Tanggal Lahir)");
            }
            if (var.VPNamaIbuKandung != "-NA-") {
                vartemp.add("mandatoryData");
                varMandat.add("ibu");
                varRMU.add("Direct To RMU (Perubahan Nama Ibu Kandung)");
            }
            String varData = "";
            String varData1 = "";
            String varData2 = "";
            String varData3 = "";
            String varData4 = "";
            String varData5 = "";
            for (String data : vartemp) {
                varData = varData + data + ";";
            }
            System.out.println("test var data cuk " + varData);
            System.out.println("test var data cuk " + varData.length());
            for (String data : varPDAlamat) {
                varData1 = varData1 + data + ";";
            }
            for (String data : varPDRetur) {
                varData2 = varData2 + data + ";";
            }
            for (String data : varRMU) {
                varData3 = varData3 + data + ";";
            }
            for (String data : varMandat) {
                varData4 = varData4 + data + ";";
            }
            for (String data : varPDTelp) {
                varData5 = varData5 + data + ";";
            }
            try {
                var.jnsLap = varData.substring(0, varData.length() - 1);
                var.jnsLap = varData.substring(0, varData.length() - 1);
            } catch (Exception e) {
                var.jnsLap = " ";
            }

            if (varData1.length() > 0) {
                var.subJnsLapAlamt = varData1.substring(0, varData1.length() - 1);
            } else {
                var.subJnsLapAlamt = "-NA-";
            }
            if (varData2.length() > 0) {
                var.subJnsLapdataReturn = varData2.substring(0, varData2.length() - 1);
            } else {
                var.subJnsLapdataReturn = "-NA-";
            }
            if (varData3.length() > 0) {
                var.var_rmu = varData3.substring(0, varData3.length() - 1);
            } else {
                var.var_rmu = "-NA-";
            }
            if (varData4.length() > 0) {
                var.subJnsLapmandatoryData = varData4.substring(0, varData4.length() - 1);
            } else {
                var.subJnsLapmandatoryData = "-NA-";
            }
            if (varData5.length() > 0) {
                var.subJnsLapNotelp = varData5.substring(0, varData5.length() - 1);
            } else {
                var.subJnsLapNotelp = "-NA-";
            }
            //Cek Apakah ada unsur (Data Retur, Mandatory Daya dan Replace Card) jika ada maka akan di override ke bagian RMU
            /*
	             * File Checklist Mega Workflow
             */
            sb.append("c_fldSelectReq,c_fldTypeOfChange,c_fldChangeTelp,c_fldChangeDataRetur,c_fldSelectMandatory,c_acctCardStatus,"
                    + "c_acctCHEmail,c_acctCusdName,c_txtCardHolderNm,c_fldNamaNasabah,c_acctTglLapor,c_acctCardNumber,c_acctCardLimit,"
                    + "c_fldMemoCCBM,c_txtJenisKasus,c_fldJenisLaporan,c_subJenisLaporan,c_fldAssignTo,").append(dataAssignTo)
                    .append(",");
            sbVal.append("'").append(var.jnsLap.trim()).append("'").append(",")
                    .append("'").append(var.subJnsLapAlamt.trim()).append("'").append(",")
                    .append("'").append(var.subJnsLapNotelp.trim()).append("'").append(",")
                    .append("'").append(var.subJnsLapdataReturn.trim()).append("'").append(",")
                    .append("'").append(var.subJnsLapmandatoryData.trim()).append("'").append(",")
                    .append("'").append(var.cardstatus.trim()).append("'").append(",")
                    .append("'").append(var.custEmail.trim()).append("'").append(",")
                    .append("'").append(var.custName.trim()).append("'").append(",")
                    .append("'").append(var.custName.trim()).append("'").append(",")
                    .append("'").append(var.custName.trim()).append("'").append(",")
                    .append("'").append(var.tanggalLapor.trim()).append("'").append(",")
                    .append("'").append(var.cardno.trim()).append("'").append(",")
                    .append("'").append(var.cardLimit.trim()).append("'").append(",")
                    .append("'").append(var.memoCCBM).append("'").append(",")
                    .append("'").append(var.subJnsAll.trim()).append("'").append(",")
                    .append("'").append(var.jns.trim()).append("'").append(",")
                    .append("'").append(var.subjns.trim()).append("'").append(",")
                    .append("'").append(mod.getUsername().trim()).append("'").append(",")
                    .append("'").append(mod.getUsername().trim()).append("'").append(",");
            /*
    			 * **************************************************PERUBAHAN DATA RETUR******************************************************************************
             */
 /*
	    		 * File Alamat Rumah Retur
             */
            sb.append("c_fldReturnAddr1,c_fldReturnAddr2,c_fldReturnAddr3,c_fldReturnAddr4,c_fldReturnAddr5,"
                    + "c_fldReturnAddr6,c_fldNewReturnAddr1,c_fldNewReturnAddr2,c_fldNewReturnAddr3,c_fldNewReturnAddr4,c_fldNewReturnAddr5,"
                    + "c_fldNewReturnAddr6,");
            sbVal.append("'").append(dataTemp.get("Form3_CurrAddr1_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrAddr2_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrAddr3_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrAddr4_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrCity_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrZip_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr1_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr2_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr3_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr4_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_City_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Zip_retur").trim()).append("'").append(",");
            /*
	    		 * File Alamat Kantor Retur
             */
            sb.append("c_fldReturOfficeAddr1,c_fldReturOfficeAddr2,c_fldReturOfficeAddr3,c_fldReturOfficeAddr4,c_fldReturOfficeAddr5,"
                    + "c_fldReturOfficeAddr6,c_fldReturOfficeAddr7,c_fldNewReturOfficeAddr1,c_fldNewReturOfficeAddr2,c_fldNewReturOfficeAddr3,"
                    + "c_fldNewReturOfficeAddr4,c_fldNewReturOfficeAddr5,c_fldNewReturOfficeAddr6,c_fldNewReturOfficeAddr7,");
            sbVal.append("'").append(dataTemp.get("Form4_CurrOfficeName_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr1_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr2_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr3_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr4_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrCity_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrZip_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_OfficeName_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr1_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr2_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr3_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr4_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_City_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Zip_retur").trim()).append("'").append(",");
            /*
    			 * File Alamat Relasi Retur
             */
            sb.append("c_fldNamaRelasiAddr,c_fldRelasiAddr1,c_fldRelasiAddr2,c_fldRelasiAddr3,c_fldRelasiAddr4,c_fldRelasiAddr5,"
                    + "c_fldRelasiAddr6,c_fldNewNamaRelasiAddr,c_fldNewRelasiAddr1,c_fldNewRelasiAddr2,c_fldNewRelasiAddr3,c_fldNewRelasiAddr4,"
                    + "c_fldNewRelasiAddr5,c_fldNewRelasiAddr6,");
            sbVal.append("'").append(dataTemp.get("Form5_CurrRelationName_retur")).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr1_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr2_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr3_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr4_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrCity_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrZip_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_RelationName_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr1_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr2_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr3_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr4_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_City_retur").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Zip_retur").trim()).append("'").append(",");
            /*
    			 * **************************************************PERUBAHAN DATA ALAMAT REGULER******************************************************************************
             */
 /*
	    		 * File Alamat Rumah Reguler
             */
            sb.append("c_fldCurrentHomeAddr1,c_fldCurrentHomeAddr2,c_fldCurrentHomeAddr3,c_fldCurrentHomeAddr4,c_fldCurrentHomeKota,"
                    + "c_fldCurrentHomePostCd,c_fldNewHomeAddr1,c_fldNewHomeAddr2,c_fldNowHomeAddr3,c_fldNowHomeAddr4,c_fldNowHomeKota,"
                    + "c_fldNowHomePostCd,");
            sbVal.append("'").append(dataTemp.get("Form3_CurrAddr1").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrAddr2").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrAddr3").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrAddr4").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrCity").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_CurrZip").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr1").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr2").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr3").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Addr4").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_City").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form3_Zip").trim()).append("'").append(",");
            /*
	    		 * File Alamat Kantor Reguler
             */
            sb.append("c_fldCurrentOffice,c_fldCurrentOfficeAddr1,c_fldCurrentOfficeAddr2,c_fldCurrentOfficeAddr3,c_fldCurrentOfficeAddr4,"
                    + "c_fldCurrentOfficeKota,c_fldCurrentOfficePostCd,c_fldNowOffice,c_fldNowOfficeAddr1,c_fldNowOfficeAddr2,c_fldNowOfficeAddr3,"
                    + "c_fldNowOfficeAddr4,c_fldNowOfficeKota,c_fldNowOfficePostCd,");
            sbVal.append("'").append(dataTemp.get("Form4_CurrOfficeName").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr1").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr2").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr3").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrAddr4").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrCity").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_CurrZip")).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_OfficeName").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr1").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr2").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr3").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Addr4").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_City").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form4_Zip").trim()).append("'").append(",");
            /*	    			 * File Alamat Relasi Reguler
             */
            sb.append("c_fldNamaRelasi,c_fldCurrentMailAddr,c_fldCurrentMailAddr2,c_fldCurrentMailAddr3,c_fldCurrentMailAddr4,"
                    + "c_fldCurrentMailAddr5,c_fldCurrentMailAddr6,c_fldNewNamaRelasi,c_fldNewMailAddr,c_fldNewMailAddr2,c_fldNewMailAddr3,c_fldNewMailAddr4,"
                    + "c_fldNewMailAddr5,c_fldNewMailAddr6,");
            sbVal.append("'").append(dataTemp.get("Form5_CurrRelationName").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr1").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr2").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr3").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrAddr4").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrCity").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_CurrZip").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_RelationName").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr1").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr2").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr3").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Addr4").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_City").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form5_Zip").trim()).append("'").append(",");
            /*
    			 * **************************************************PERUBAHAN NO TELP******************************************************************************
             */

 /*
    			 * File No Telp Rumah Kantor
             */
            sb.append("c_fldNoTelpRmh,c_fldNoTelpkantor,c_fldNoFax,c_fldOtherPhone,c_fldNewNoTelpRmh,c_fldNewNoTelpkantor,c_fldNewNoFax,c_fldNewOtherPhone,");
            sbVal.append("'").append(dataTemp.get("Form1_CurrTelHom").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form1_CurrTelOff").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form1_CurrTelFax").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form1_CurrTelOth").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form1_TelHom").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form1_TelOff").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form1_TelFax").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form1_TelOth").trim()).append("'").append(",");
            /*
	 		     * File No HP
             */
            sb.append("c_fldNoHp,c_fldNewNoHp,");
            sbVal.append("'").append(dataTemp.get("Form2_CurrTelMob").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form2_TelMob").trim()).append("'").append(",");

            /*
    			 * **************************************************PERUBAHAN Mandatory Data******************************************************************************
             */
 /*
    			 * File Nama Lengkap
             */
            sb.append("c_fldNamaCH,c_fldNewNamaCH,");
            sbVal.append("'").append(dataTemp.get("Form6_CurrCustName").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form6_CustName").trim()).append("'").append(",");

            /* 
    			 * File ID Number/KTP
             */
            sb.append("c_fldIdNumber,c_fldNewIdNumber,");
            sbVal.append("'").append(dataTemp.get("Form6_CurrIDNbr").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form6_IDNbr").trim()).append("'").append(",");

            /* 
    			 * File DOB/Tanggal Lahir
             */
            sb.append("c_fldDataDOB,c_fldNewDataDOB,");
            sbVal.append("'").append(dataTemp.get("Form6_CurrDOB").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form6_DOB").trim()).append("'").append(",");

            /* 
    			 * File Nama Ibu Kandung
             */
            sb.append("c_fldNamaibu,c_fldNewNamaibu,");
            sbVal.append("'").append(dataTemp.get("Form6_CurrMomName").trim()).append("'").append(",")
                    .append("'").append(dataTemp.get("Form6_MomName").trim()).append("'").append(",");

            String Nilaisb = sb.toString();
            String NilaisbVal = sbVal.toString();
            dataSend.put("tiket", tiket);
            dataSend.put("query", Nilaisb);
            dataSend.put("value", NilaisbVal);
            dataSend.put("rmu", var.var_rmu);

        }

        return dataSend;

    }

    public List resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList list = new ArrayList(50);
        while (rs.next()) {
            HashMap row = new HashMap(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }
}
