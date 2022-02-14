package com.atguigu.springcloud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeDataController
{
    private static Logger log = LoggerFactory.getLogger(EmployeeDataController.class);

    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    public String getAddresses()
    {
        log.info("get addresses Start");


        {

            log.info("getting addresses...");


        }

        return "address---";
    }

    @RequestMapping(value = "/phones", method = RequestMethod.GET)
    public String getPhoneNumbers()
    {
        log.info("get phones Start");


        {
            log.info("getting phones...");

        }

        return "phones---";
    }

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    public String getEmployeeName()
    {
        log.info("get names Start");



        {
            log.info("getting names...");

        }

        return "names---";
    }
}
