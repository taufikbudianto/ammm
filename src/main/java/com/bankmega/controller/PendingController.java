package com.bankmega.controller;

import com.bankmega.model.GenericModel;
import com.bankmega.usecase.PendingAM;
import com.bankmega.util.JogetWF;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : taufik.budiyanto
 * @date : 14/09/2018 8:42
 */
@RestController
public class PendingController {

    @Autowired JogetWF wf;
    @Value("${am.username}")
    private String usernameAdmin;
    @Value("${am.pass}")
    private String passAdmin;
    @Value("${mega.urlpendinglist}")
    private String urlpending;
    @Value("${mega.urlerror}")
    private String url;

    @Autowired
    private PendingAM pendingUsecase;

    @RequestMapping(value = "/save/am", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public ModelAndView saveData(@RequestParam("name") String name, @RequestParam(name = "checkbox2", required = false) String[] NoThck,
            @RequestParam("route") String route, @RequestParam("username") String username, @RequestParam(name = "assignTo", required = false) String assignTo,
            @RequestParam("routedata") String routedata, HttpServletResponse resp) throws SQLException {
        if (NoThck == null) {
            return new ModelAndView("redirect:" + urlpending + "username=" + username + "&name=" + name + "&error=tiketnull&data=" + routedata + "&jumlah=10" + "&route=" + route);
        }
        if (assignTo == null) {
            return new ModelAndView("redirect:" + urlpending + "username=" + username + "&name=" + name + "&error=assigntonull&data=" + routedata + "&jumlah=10" + "&route=" + route);
        }
        GenericModel mod = new GenericModel();
        mod.setName(name);
        mod.setUsernameReq(username);
        mod.setNoTicket(NoThck);
        mod.setRoute(route);
        mod.setUsername(assignTo);
        mod.setRouteto(routedata);
        String nameRoute = "";
        String nameGroup = "";
        if (mod.getRoute().equals("am")) {
            nameRoute = "var_assignToUser";
            nameGroup = "groupAnalystLimitAM";
        } else if (mod.getRoute().equals("dm")) {
            nameRoute = "var_assignToDm";
            nameGroup = "groupDM";
        } else if (mod.getRoute().equals("rmu")) {
            nameRoute = "var_assignToRmu";
            nameGroup = "grpRMULimitAM";
        }
        Map<String, String> map = new HashMap<>();
        map.put("j_username", usernameAdmin);
        map.put("j_password", passAdmin);
        map.put("var_route", mod.getRoute());
        map.put(nameRoute, mod.getUsername());
        map.put("var_" + mod.getRoute(), mod.getRouteto());
        map.put("var_groupAssignTo", nameGroup);
        map.put("data", "am");

        for (Map.Entry m : map.entrySet()) {
            System.out.println(m.getKey() + "----" + m.getValue());
        }
//        String er="";
        String er = pendingUsecase.saveData(mod);
        String projectUrl = urlpending + "username=" + mod.getUsernameReq() + "&name=" + mod.getName() + "&error=" + er + "&data=" + mod.getRouteto() + "&jumlah=10" + "&route=" + route;
        return new ModelAndView("redirect:" + projectUrl);

    }

    @RequestMapping(value = "/changeuser", method = RequestMethod.GET)
    public ModelAndView changeUser(@RequestParam("srchby") String nip, @RequestParam("idPro") String id, @RequestParam("userId") String userID,
            @RequestParam("tiket") String tiket, @RequestParam("route") String route) {
        Map<String, String> map = new HashMap<>();
        map.put("userAfter", nip);
        map.put("tiket", tiket);
        map.put("id", id);
        map.put("userBefore", userID);
        map.put("route", route);

        String err = "";
        if (nip.length() == 0) {
            err = "false";
        } else if (nip.equals(userID)) {
            err = "same";
        } else {
            String change = pendingUsecase.changeUser(map);
            err = change;

        }
        url = url + err + "&nip=" + nip + "&data=" + route;
        return new ModelAndView("redirect:" + err);

    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public ModelAndView complete(@RequestParam("checkbox2") String[] NoThck, @RequestParam("username") String username, @RequestParam("route") String Route,
            @RequestParam("routedata") String routedata, @RequestParam("assignTo") String assignTo,@RequestParam("id") String id) {
        System.out.println(Route + routedata);
        String er = "";
        if (assignTo.length() == 0) {
            er = "assigntonull";
        }

        String actId = "";
        Map<String, String> map = new HashMap<>();
        String nameRoute = "";
        String nameGroup = "";
        if (Route.equals("am")) {
            nameRoute = "var_assignToUser";
            nameGroup = "groupAnalystLimitAM";
        } else if (Route.equals("dm")) {
            nameRoute = "var_assignToDm";
            nameGroup = "groupDM";
        } else if (Route.equals("rmu")) {
            nameRoute = "var_assignToRmu";
            nameGroup = "grpRMULimitAM";
        }

        for (int i = 0; i < NoThck.length; i++) {
            Map<String, String> datakirim = new HashMap<String, String>();
            String param = "";
            String tiket = NoThck[i];
            actId = wf.generateActId(id);
            if (actId.length() <= 0) {
                System.out.println("gagal mendapatkan activity id");
                param = "gagal generate id";
            } else {
                map.put("var_route", Route);
                map.put(nameRoute, assignTo);
                map.put("var_" + Route, routedata);
                map.put("var_groupAssignTo", nameGroup);
                map.put("j_username", usernameAdmin);
                map.put("j_password", passAdmin);
                map.put("actId", actId);
                String sukses = wf.completeId(map, actId);
                if (sukses.equals("completed")) {
                    param = "completed";
                    Map<String, String> update = new HashMap<>();
                    update.put("notiket", tiket);
                    update.put("assignto", assignTo);
                    update.put("route", Route);
                    wf.updateTempFlag(update);
                } else {
                    param = "password incorrect";
                }
            }
            datakirim.put("TiketId", tiket);
            datakirim.put("completeProses", param);
        }
        String projectUrl = urlpending + Route + "caseam.php?username=" + username + "&name=&error=" + er + "&data=";

        return new ModelAndView("redirect:" + projectUrl);
    }

}
