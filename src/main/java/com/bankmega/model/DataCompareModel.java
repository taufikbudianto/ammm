/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankmega.model;

import lombok.Data;

/**
 *
 * @author Taufik
 */
@Data
public class DataCompareModel {

    private String custnbr;
    private String custname;
    private String cardstatus;
    private String cardopen;
    private String CUST_LOCAL_NAME;
    private String CUST_DTE_BIRTH;
    private String CUST_MOM_NAME;
    private String AlamatKantor;
    private String alamatRmah;
    private String CRDACCT_STMT_ADDR_FLG;
    private String CRDACCT_OUTSTD_AUTH_BAL;
    private String CRDACCT_OUTSTD_INSTL;
    private String CRDACCT_OUTSTD_BAL;
    private String CRDACCT_CRLIMIT;
}
