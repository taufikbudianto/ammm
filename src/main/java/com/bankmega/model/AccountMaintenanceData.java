package com.bankmega.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : taufik.budiyanto
 * @date : 14/09/2018  9:18
 */
@Component
@Data
public class AccountMaintenanceData {
    private String idjoget;
    private Date c_date;
    private String c_fldNamaReq;
    private String c_subJenisLaporan;
    private String c_dob;
    private String c_fldNipRequester;
    private String c_fldPosisiReq;
    private String c_assignToUser;
    private String c_fldTelpReq;
    private String c_fldEmailReq;
    private String c_txtCardNo;
    private String c_cardLimit;
    private String c_txtNoTicket;
    private String c_txtCardHolderNm;
    private String c_dtTglReq;
    private String c_fldJenisLaporan;
    private String c_fldNamaNasabah;
    private String c_fldPrioritas;
    private String c_fldComment;
    private String c_fldMemoCCBM;
    private String c_fldCommentCCBM;
    private String c_date2B;
    private String c_date3B;
    private String c_fldAssignTo;
    private String c_date4B;
    private String c_date3B2;
    private String c_dateB;
}
