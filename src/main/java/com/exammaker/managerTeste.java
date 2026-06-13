package com.exammaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class managerTeste {

    // directorul dedicat in care vom salva fisierele
    private static final String TESTE_DIR = "teste";

    // obiectul din Jackson care face transformarea java -> json
    private final ObjectMapper objectMapper;

    public managerTeste() {
        this.objectMapper = new ObjectMapper();
        asiguraExistentaDirectorului();
    }

    // tratam validarea obligatorie a existentei directorului
    private void asiguraExistentaDirectorului() {
        File dir = new File(TESTE_DIR);
        if (!dir.exists()) {
            dir.mkdirs(); // creeaza directorul daca nu exista
            System.out.println("Directorul '" + TESTE_DIR + "' a fost creat.");
        }
    }

    // metoda pentru salvarea unui test in json
    public void salveazaTest(testGrila test) {
        try {
            // generam un nume de fisier pe baza numelui testului, inlocuind spațiile cu underscore
            String numeFisier = test.getMetadata().getNumeTest().replaceAll("\\s+", "_") + ".json";
            File file = new File(TESTE_DIR, numeFisier);

            // writerWithDefaultPrettyPrinter() aliniaza frumos textul in json
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, test);
            System.out.println("Testul a fost salvat cu succes in: " + file.getPath());

        } catch (IOException e) {
            System.err.println("Eroare la salvarea testului: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Metadata testului este incompleta.");
        }
    }

    // metoda pentru incarcarea unui test in json
    public testGrila incarcaTest(String numeFisier) {
        try {
            File file = new File(TESTE_DIR, numeFisier);

            if (!file.exists()) {
                System.err.println("Fisierul " + numeFisier + " nu exista.");
                return null;
            }

            // transforma automat continutul din json direct in obiectul testGrila
            return objectMapper.readValue(file, testGrila.class);

        } catch (IOException e) {
            // aici prindem validarea daca fișierul este invalid
            System.err.println("Eroare la citirea testului (fisier invalid sau corupt): " + e.getMessage());
            return null;
        }
    }
}