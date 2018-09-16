package com.bankmega.model;

import java.util.Map;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : taufik.budiyanto
 * @date : 14/09/2018  8:49
 */
@Component
@Data
public class GenericModel {
    private String username;
    private String name;
    private String usernameReq;
    private String [] NoTicket;
    private String route;
    private String routeto;
    private Map<String,String> map;
}
