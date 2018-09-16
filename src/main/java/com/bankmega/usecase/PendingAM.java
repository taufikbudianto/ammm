package com.bankmega.usecase;

import com.bankmega.model.GenericModel;
import com.bankmega.util.AbstractManagedBean;
import com.bankmega.util.TempSaveRMU;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : taufik.budiyanto
 * @date : 14/09/2018 8:50
 */
@Component
public class PendingAM extends AbstractManagedBean {

    @Autowired
    private PendingAmImpl pendingAmImpl;
    @Autowired
    private TempSaveRMU temp;
    @Value("${mega.urlpendinglist}")
    private String urlpending;
    protected Logger log = LoggerFactory.getLogger(getClass());

    public String saveData(GenericModel mod) throws SQLException {
        String er = null;
        String tiket = "";
        if (mod.getUsername().length() == 0) {
            er = "assigntonull";
        } else if (mod.getNoTicket().length == 0) {
            er = "tiketnull";
        } else {
            for (int i = 0; i < mod.getNoTicket().length; i++) {
                tiket = mod.getNoTicket()[i];
                int cekDouble = getData(tiket);
                if (cekDouble == 0) {
//                    String idjoget = generateID(mod.getMap());
                    String idjoget = "taufik---1";
                    if (idjoget.length() <= 0) {
                        log.info("Gagal Generate Id" + tiket);
                        er = "duplicate";
                    } else {
                        if (mod.getRoute().equals("am")) {
                            log.info("Case Dari AM");
                            er = pendingAmImpl.getDataTempByTiket(idjoget, mod, tiket);
                        } else if (mod.getRoute().equals("dm")) {
                            Map<String, String> save = temp.setDataVariablePatch(mod, tiket, idjoget);
                            save.put("id", idjoget);
                            save.put("route", mod.getRoute());
                            er = pendingAmImpl.dMrmuSave(save);
                            log.info("Case Dari DM");
                        } else if (mod.getRoute().equals("rmu")) {
                            Map<String, String> save = temp.setDataVariablePatch(mod, tiket, idjoget);
                            save.put("id", idjoget);
                            save.put("route", mod.getRoute());
                            er = pendingAmImpl.dMrmuSave(save);
                            log.info("Case Dari RMU");
                        }
                    }

                } else {
                    log.info("Gagal Generate Id (Ticket Number Cannot Duplicate)");
                    er = "duplicate";
                }
            }
        }

        String projectUrl = urlpending + mod.getRoute() + ".php?username=" + mod.getUsernameReq() + "&name=" + mod.getName() + "&error=" + er + "&data=" + mod.getRouteto() + "&jumlah=10";

        return er;
    }

    public String changeUser(Map<String, String> map) {
        // TODO Auto-generated method stub
        String route = map.get("route");
        String changeUser = "";
        if (route.equals("am")) {
            changeUser = "c_assigntoAnalyst";
        } else if (route.equals("dm")) {
            changeUser = "c_assigntoDm";
        } else if (route.equals("rmu")) {
            changeUser = "c_assigntoRMU";
        }
        String param = "";
        String data = "";
        String userAfter = "";
        String tiket = "";
        String id = "";
        String userBefore = "";
        String insert = "insert into tbl_assignacctmaintenance(tanggal,procid,sebelum,sesudah,notiket) values(now(),?,?,?,?)";
        String update = "update app_fd_accountmaint set " + changeUser + "=? where id =?";
        for (Map.Entry<String, String> pair : map.entrySet()) {
            //iterate over the pairs
            param = param + pair.getKey() + "=" + pair.getValue() + "&";
            userAfter = map.get("userAfter");
            tiket = map.get("tiket");
            id = map.get("id");
            userBefore = map.get("userBefore");
        }
        try {
            Connection con = getKoneksiMysql();
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, id);
            ps.setString(2, userBefore);
            ps.setString(3, userAfter);
            ps.setString(4, tiket);

            PreparedStatement psUp = con.prepareStatement(update);
            psUp.setString(1, userAfter);
            psUp.setString(2, id);
            ps.executeUpdate();
            psUp.executeUpdate();
            data = "true";
        } catch (Exception e) {
            // TODO: handle exception
            data = "false";
        }
        System.out.println(param);
        return data;
    }
}
