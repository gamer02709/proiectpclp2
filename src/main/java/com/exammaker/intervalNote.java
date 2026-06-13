package com.exammaker;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class intervalNote {
    private int note; // nota
    private double procentMin; // 0.0
    private double procentMax; // 9.99
}