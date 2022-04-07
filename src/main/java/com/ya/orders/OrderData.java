package com.ya.orders;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderData {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
}