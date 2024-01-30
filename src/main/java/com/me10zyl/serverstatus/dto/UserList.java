package com.me10zyl.serverstatus.dto;

import lombok.Data;

@Data
public class UserList {
    private String format;
    private Integer playerCount = 0;
    private Integer maxPlayerCount = 12;
}
