package com.rdbac.rdbac;

import com.rdbac.rdbac.Pojos.App_User;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class test {
    @Autowired
    private TestRepo testRepo;

    @GetMapping("/")
    public String tes() {

        return MDC.get("correlationId");
    }
}
