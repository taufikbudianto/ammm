package com.bankmega.usecase;

import com.bankmega.model.AccountMaintenanceData;
import com.bankmega.model.GenericModel;
import com.bankmega.util.AbstractManagedBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.hibernate.QueryException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : taufik.budiyanto
 * @date : 14/09/2018 9:17
 */
@Component
public class PendingAmImpl extends AbstractManagedBean {

    @Value("${app.table.temp}")
    private String tableTemp;
    protected String getDataTempByTiket(String id, GenericModel mod, String tiket) {
        AccountMaintenanceData dataAmsave = new AccountMaintenanceData();
        Connection con = getKoneksiMysql();
        String sql = "select * from " + tableTemp + " where ticketNo = '" + tiket + "'";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataAmsave.setC_dob(rs.getString("tanggalLahir"));
                dataAmsave.setIdjoget(id);
                dataAmsave.setC_fldNamaReq(mod.getName());
                dataAmsave.setC_fldNipRequester(mod.getUsernameReq());
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
                dataAmsave.setC_fldAssignTo(mod.getUsername());
            }
            return saveData(dataAmsave, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "gagal";
    }

    private String saveData(AccountMaintenanceData da, Connection con) {
        String balikan = "gagal";
        log.info("Assign To User  : " + da.getC_fldAssignTo());
        String tglLahir = da.getC_dob().replace("-", "");
        String ins = "Insert into app_fd_accountmaint (c_pendingBy,c_pendingAnalyst,c_pendingAfterAdmin,status_routing,dateCreated,dateModified,c_fldNamaReq,c_fldNipRequester,"
                + "c_txtCardNo,c_cardLimit,c_txtNoTicket,c_ticketNo,c_txtCardHolderNm,c_dtTglReq,"
                + "c_fldJenisLaporan,c_fldNamaNasabah,c_fldMemoCCBM,c_fldCommentCCBM,c_fldAssignTo,id,c_flagWFcontrol,c_subJenisLaporan,c_acctTglLapor,c_txtJenisKasus,c_assigntoAnalyst,c_dob)"
                + "values('AnalystAM','true','true','Case AM',NOW(),NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String update = "update tbl_acctmaintenancetemp set flagCCBM=?,procid=? where ticketNo=?";
        try {
            PreparedStatement psupdate = con.prepareStatement(update);
            PreparedStatement ps = con.prepareStatement(ins);
            ps.setString(1, da.getC_fldNamaReq());
            ps.setString(2, da.getC_fldNipRequester());
            ps.setString(3, da.getC_txtCardNo());
            ps.setString(4, da.getC_cardLimit());
            ps.setString(5, da.getC_txtNoTicket());
            ps.setString(6, da.getC_txtNoTicket());
            ps.setString(7, da.getC_txtCardHolderNm());
            ps.setString(8, da.getC_dtTglReq());
            ps.setString(9, da.getC_fldJenisLaporan());
            ps.setString(10, da.getC_fldNamaNasabah());
            ps.setString(11, da.getC_fldMemoCCBM());
            ps.setString(12, da.getC_fldCommentCCBM());
            ps.setString(13, da.getC_fldAssignTo());
            ps.setString(14, da.getIdjoget());
            ps.setString(15, "am");
            ps.setString(16, da.getC_subJenisLaporan());
            ps.setString(17, da.getC_dtTglReq());
            ps.setString(18, da.getC_fldJenisLaporan());
            ps.setString(19, da.getC_fldAssignTo());
            ps.setString(20, tglLahir);
            ps.executeUpdate();
            psupdate.setString(1, "onprogress");
            psupdate.setString(2, da.getIdjoget());
            psupdate.setString(3, da.getC_txtNoTicket());
            psupdate.executeUpdate();
            balikan = "sukses";
            System.out.println(ins);
            System.out.println(update);
        } catch (Exception e) {
            // TODO: handle exception
            log.info(e.getMessage());
            e.printStackTrace();
            balikan = "gagal";
        }
        return balikan;
    }

    public String dMrmuSave(Map<String, String> map) {
        String key = map.get("query");
        String value = map.get("value");
        String tiket = map.get("tiket");
        System.out.println("tiket "+tiket);
        String rmu = map.get("rmu");
        String id = map.get("id");
        String route = map.get("route");
        String data = "(c_pendingBy,id,c_pendingAfterAdmin,dateCreated,c_flagWFcontrol,dateModified,c_fldFollowUpRMU,status_routing,c_ticketNo,c_txtNoTicket," + key.toString().substring(0, key.length() - 1) + ") values ('UserDM','" + id + "','true',NOW(),'" + route + "',NOW(),'" + route + "','" + rmu + "','" + tiket + "'" + ",'" + tiket + "'," + value.toString().substring(0, value.length() - 1) + ")";
        String insert = "insert into app_fd_accountmaint " + data;
        String update = "update tbl_acctmaintenancetemp set flagCCBM=?,procid=? where ticketNo=?";
        String err = "";
        Connection con = getKoneksiMysql();
        try {
            PreparedStatement ps = con.prepareStatement(insert);
            ps.executeUpdate();
            PreparedStatement psUpdate = con.prepareStatement(update);
            psUpdate.setString(1, "onprogress");
            psUpdate.setString(2, id);
            psUpdate.setString(3, tiket);
            psUpdate.executeUpdate();
            err = "sukses";
        } catch (QueryException e) {
            // TODO: handle exception
            log.error(e.getMessage());
            err = "gagal";
        } catch (SQLException ex) {
            err = "gagal";
        }

        return err;
    }
}
