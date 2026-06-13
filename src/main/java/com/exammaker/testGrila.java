package com.exammaker;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class testGrila {
    private Metadata metadata;
    private List<Intrebare> intrebari;
}