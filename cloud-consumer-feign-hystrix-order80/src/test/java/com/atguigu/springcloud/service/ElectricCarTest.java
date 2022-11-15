package com.atguigu.springcloud.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
class ElectricCarTest {

    @BeforeEach
    void setUp() {
        System.out.println("mickito BeforeEach ");
        MockitoAnnotations.initMocks(this);//openMocks is latest version, initMocks is deprecated
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void drive() {
    }

    @Mock//must use with  MockitoAnnotations.openMocks //crete obj only no dependency injected
    Random ran;

    //@Spy will run the real method(if no stub defined), @Mock will not
    @InjectMocks//create obj and inject this dependency
    Random ranIOC;

    @Spy
    ComplexMath complexMath;

    @Test
    void testfunc() {


        complexMath.calculate();
        complexMath.calculate();
        Random random = Mockito.mock(Random.class);
        Mockito.when(ran.nextInt()).thenReturn(500);
        int i1 = ran.nextInt();
        int i2 = ranIOC.nextInt();
        System.out.println("mickito ranIOC i "+i2);
        System.out.println("mickito ran i "+i1);
        Mockito.verify(ran, Mockito.times(1)).nextInt();
        int i = Mockito.verify(ran).nextInt();
        System.out.println("mickito verify i "+i);

    }
}