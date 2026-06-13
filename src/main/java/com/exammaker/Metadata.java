package com.exammaker;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    private String numeTest; // trebuie sa fie unic
    private String subiect; // disciplina
    private List<intervalNote> intervaleNote; // intervalele pentru note
}