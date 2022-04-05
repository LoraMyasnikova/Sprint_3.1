package com.ya.courier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Courier {

    private String login;
    private String password;
    private String firstName;
}