package com.webflux.demo.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String id;
    private String name;
}
