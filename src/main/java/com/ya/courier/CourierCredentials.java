package com.ya.courier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierCredentials {

    private String login;
    private String password;
}