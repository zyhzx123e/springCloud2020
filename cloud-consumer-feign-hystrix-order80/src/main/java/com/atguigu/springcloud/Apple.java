package com.atguigu.springcloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Apple {
    private int id;
    private String color;
    private float weight;
    private String origin;
}
