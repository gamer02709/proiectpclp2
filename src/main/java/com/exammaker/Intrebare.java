package com.exammaker;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Intrebare {
    private int id; // un identificator
    private String text; // enuntul intrebarii
    private List<String> optiuni; // lista variantelor de raspuns
    private int optiuneaCorecta; // indexul optiunii corecte
    private double punctaj; // punctajul întrebarii
}