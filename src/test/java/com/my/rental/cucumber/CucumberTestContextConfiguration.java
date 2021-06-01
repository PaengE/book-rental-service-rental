package com.my.rental.cucumber;

import com.my.rental.RentalApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = RentalApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
