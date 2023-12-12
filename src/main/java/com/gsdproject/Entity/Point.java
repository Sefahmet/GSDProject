package com.gsdproject.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor

public class Point {
    @Getter @Setter double x;
    @Getter @Setter double y;
    @Getter @Setter double h;
}
