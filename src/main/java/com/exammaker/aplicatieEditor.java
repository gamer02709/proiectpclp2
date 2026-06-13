package com.exammaker;

import java.util.Scanner;

public class aplicatieEditor {
    private final Scanner scanner;
    private final managerTeste managerTeste;

    public aplicatieEditor() {
        this.scanner = new Scanner(System.in);
        this.managerTeste = new managerTeste();
    }

    public void start() {
        boolean ruleaza = true;

        while (ruleaza) {
            System.out.println("\n===== Meniu editare teste =====");
            System.out.println("1. Crearea unui test nou");
            System.out.println("2. Incarcarea unui test existent");
            System.out.println("0. Iesire");
            System.out.print("Alege o optiune: ");

            String optiune = scanner.nextLine();

            switch (optiune) {
                case "1":
                    creeazaTestNou();
                    break;
                case "2":
                    incarcaTestExistent();
                    break;
                case "0":
                    ruleaza = false;
                    System.out.println("Iesire din aplicatia de editare...");
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private void creeazaTestNou() {
        System.out.println("\n!!! Creare test nou !!!");

        System.out.print("Introdu numele testului: ");
        String nume = scanner.nextLine();

        // verificam daca mai exista alte teste cu acelasi nume
        String numeFisier = nume.replaceAll("\\s+", "_") + ".json";
        java.io.File file = new java.io.File("teste", numeFisier);
        if (file.exists()) {
            System.out.println("Un test cu acest nume exista.");
            return;
        }

        System.out.print("Introdu disciplina: ");
        String disciplina = scanner.nextLine();

        Metadata metadata = new Metadata();
        metadata.setNumeTest(nume);
        metadata.setSubiect(disciplina);
        // initializam o lista goala pentru intervalele de note
        metadata.setIntervaleNote(new java.util.ArrayList<>());

        // cream testul cu o lista goala de intrebari
        testGrila testNou = new testGrila(metadata, new java.util.ArrayList<>());

        managerTeste.salveazaTest(testNou);
        System.out.println("Acum poti incarca testul din meniul principal pentru a adauga intervale de note si intrebari.");
    }

    private void incarcaTestExistent() {
        System.out.println("\n!!! Incarcare test !!!");
        System.out.print("Introdu numele testului (fara .json): ");
        String nume = scanner.nextLine();

        testGrila testIncarcat = managerTeste.incarcaTest(nume + ".json");

        if (testIncarcat != null) {
            System.out.println("Testul '" + testIncarcat.getMetadata().getNumeTest() + "' a fost incarcat.");
            meniuEditareTest(testIncarcat); // trimitem testul încărcat la noul meniu
        }
    }

    private void meniuEditareTest(testGrila test) {
        boolean editeaza = true;

        while (editeaza) {
            System.out.println("\n=== Editare test " + test.getMetadata().getNumeTest() + " ===");
            System.out.println("1. Modificare metadata (Intervale note, disciplina, nume)");
            System.out.println("2. Adaugare intrebare noua");
            System.out.println("3. Afisare intrebari curente");
            System.out.println("4. Modificare intrebari");
            System.out.println("5. Stergere intrebare");
            System.out.println("0. Salveare modificari");
            System.out.print("Alege o opțiune: ");

            String optiune = scanner.nextLine();

            switch (optiune) {
                case "1":
                    modificaMetadate(test);
                    break;
                case "2":
                    adaugaIntrebare(test);
                    break;
                case "3":
                    System.out.println("\nTestul are " + test.getIntrebari().size() + " de intrebari.");
                    for (Intrebare q : test.getIntrebari()) {
                        System.out.println("ID " + q.getId() + ": " + q.getText());
                    }
                    break;
                case "4":
                    modificaIntrebare(test);
                    break;
                case "5":
                    stergeIntrebare(test);
                    break;
                case "0":
                    // salvare automata
                    managerTeste.salveazaTest(test);
                    System.out.println("Modificarile au fost salvate.");
                    editeaza = false;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private void adaugaIntrebare(testGrila test) {
        System.out.println("\n!!! Adaugare intrebari !!!");

        System.out.print("Introdu enuntul intrebarii: ");
        String text = scanner.nextLine();
        if (text.trim().isEmpty()) {
            System.out.println("Enuntul nu poate fi gol.");
            return;
        }

        int numarVariante = 0;
        while (numarVariante < 2) {
            System.out.print("Cate variante de raspuns va avea? (minim 2): ");
            try {
                numarVariante = Integer.parseInt(scanner.nextLine());
                if (numarVariante < 2) {
                    System.out.println("Trebuie sa existe cel putin 2 variante de raspuns.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Introdu un numar intreg.");
            }
        }

        java.util.List<String> optiuni = new java.util.ArrayList<>();
        for (int i = 1; i <= numarVariante; i++) {
            System.out.print("Varianta " + i + ": ");
            String optiune = scanner.nextLine();
            optiuni.add(optiune);
        }

        int raspunsCorect = 0;
        while (raspunsCorect < 1 || raspunsCorect > numarVariante) {
            System.out.print("Care este varianta corecta? (introdu numarul de la 1 la " + numarVariante + "): ");
            try {
                raspunsCorect = Integer.parseInt(scanner.nextLine());
                if (raspunsCorect < 1 || raspunsCorect > numarVariante) {
                    System.out.println("Optiunea trebuie să fie între 1 și " + numarVariante + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Te rog introdu un numar valid.");
            }
        }

        // setarea punctajului
        double punctaj = 0;
        while (punctaj <= 0) {
            System.out.print("Introdu punctajul intrebarii: ");
            try {
                punctaj = Double.parseDouble(scanner.nextLine());
                if (punctaj <= 0) {
                    System.out.println("Punctajul trebuie sa fie mai mare decat 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Introdu un numar valid (foloseste punct pentru zecimale).");
            }
        }

        // id bazat pe numarul de intrebari
        int idNou = test.getIntrebari().size() + 1;

        Intrebare intrebareNoua = new Intrebare(idNou, text, optiuni, raspunsCorect, punctaj);
        test.getIntrebari().add(intrebareNoua);

        System.out.println("Intrebarea a fost adaugata.");
    }

    private void stergeIntrebare(testGrila test) {
        System.out.println("\n!!! Stergere intrebare !!!");
        if (test.getIntrebari().isEmpty()) {
            System.out.println("Nu exista nicio intrebare in acest test.");
            return;
        }

        for (Intrebare q : test.getIntrebari()) {
            System.out.println("ID " + q.getId() + ": " + q.getText());
        }

        System.out.print("Introdu IDul intrebarii pe care vrei sa o ștergi (sau 0 pentru anulare): ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (id == 0) return;

            // folosim removeIf pentru a sterge direct intrebarea.
            boolean stearsa = test.getIntrebari().removeIf(q -> q.getId() == id);

            if (stearsa) {
                System.out.println("✅ Întrebarea a fost ștearsă!");
                // reindexam IDurile
                for (int i = 0; i < test.getIntrebari().size(); i++) {
                    test.getIntrebari().get(i).setId(i + 1);
                }
            } else {
                System.out.println("Eroare: Nu s-a găsit nicio întrebare cu acest ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Eroare: Te rog introdu un ID valid (număr întreg).");
        }
    }

    private void modificaIntrebare(testGrila test) {
        System.out.println("\n!!! Modificare intrebare !!!");
        if (test.getIntrebari().isEmpty()) {
            System.out.println("Nu exista nicio intrebare de modificat.");
            return;
        }

        System.out.print("Introdu IDul intrebarii pe care vrei sa o modifici: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Intrebare intrebareDeModificat = null;

            for (Intrebare q : test.getIntrebari()) {
                if (q.getId() == id) {
                    intrebareDeModificat = q;
                    break;
                }
            }

            if (intrebareDeModificat == null) {
                System.out.println("Nu s-a gasit nicio intrebare cu acest ID.");
                return;
            }

            System.out.println("Apasa enter daca nu vrei sa modifici valoarea curenta.");

            // modificarea textului
            System.out.print("Enunț curent [" + intrebareDeModificat.getText() + "]: ");
            String textNou = scanner.nextLine();
            if (!textNou.trim().isEmpty()) {
                intrebareDeModificat.setText(textNou);
            }

            // modificarea punctajului
            System.out.print("Punctaj curent [" + intrebareDeModificat.getPunctaj() + "]: ");
            String punctajNouStr = scanner.nextLine();
            if (!punctajNouStr.trim().isEmpty()) {
                double punctajNou = Double.parseDouble(punctajNouStr);
                if (punctajNou > 0) {
                    intrebareDeModificat.setPunctaj(punctajNou);
                } else {
                    System.out.println("Punctajul trebuie sa fie mai mare decat 0.");
                }
            }

            System.out.println("Intrebarea a fost actualizata.");

        } catch (NumberFormatException e) {
            System.out.println("Format numeric invalid.");
        }
    }

    private void modificaMetadate(testGrila test) {
        System.out.println("\n!!! Modificare metadata !!!");
        System.out.println("1. Schimba numele testului");
        System.out.println("2. Schimba disciplina");
        System.out.println("3. Seteaza/Modifica intervalele pentru note (de la 1 la 10)");
        System.out.print("Alege ce doresti sa modifici: ");

        String optiune = scanner.nextLine();

        switch (optiune) {
            case "1":
                System.out.print("Introdu noul nume: ");
                String numeNou = scanner.nextLine();
                if (!numeNou.trim().isEmpty()) {
                    test.getMetadata().setNumeTest(numeNou);
                    System.out.println("Numele a fost actualizat.");
                } else {
                    System.out.println("Numele nu poate fi gol.");
                }
                break;
            case "2":
                System.out.print("Introdu noua disciplina: ");
                String disciplinaNoua = scanner.nextLine();
                if (!disciplinaNoua.trim().isEmpty()) {
                    test.getMetadata().setSubiect(disciplinaNoua);
                    System.out.println("Disciplina a fost actualizata.");
                } else {
                    System.out.println("Disciplina nu poate fi goala.");
                }
                break;
            case "3":
                seteazaIntervaleNote(test);
                break;
            default:
                System.out.println("Opțiune invalidă.");
        }
    }

    private void seteazaIntervaleNote(testGrila test) {
        System.out.println("\n!!! Setare intervale note !!!");
        System.out.println("Pentru a asigura ca intervalele nu se suprapun, vei introduce doar limita maxima pentru fiecare nota.");

        java.util.List<intervalNote> intervale = new java.util.ArrayList<>();
        double minimCurent = 0.0;

        for (int nota = 1; nota <= 10; nota++) {
            if (nota == 10) {
                // nota 10 acopera automat restul procentajului pana la 100.00
                System.out.println("Nota 10 va acoperi intervalul: " + minimCurent + " -> 100.00%");
                intervale.add(new intervalNote(10, minimCurent, 100.00));
                break;
            }

            double maxim = 0;
            boolean valid = false;
            while (!valid) {
                System.out.print("Introdu procentajul maxim pentru nota " + nota + " (trebuie să fie mai mare decat " + minimCurent + "): ");
                try {
                    maxim = Double.parseDouble(scanner.nextLine());
                    // rotunjim inputul la doua zecimale
                    maxim = Math.round(maxim * 100.0) / 100.0;

                    if (maxim > minimCurent && maxim < 100.0) {
                        valid = true;
                    } else {
                        System.out.println("Valoarea trebuie sa fie strict mai mare decat " + minimCurent + " și mai mică de 100.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Introdu un numar valid (foloseste punctul pentru zecimale, ex: 39.99).");
                }
            }

            intervale.add(new intervalNote(nota, minimCurent, maxim));

            // calculam urmatorul minim adaugand 0.01 la maximul curent pentru a nu avea suprapuneri (ex: 39.99 devine 40.00)
            minimCurent = Math.round((maxim + 0.01) * 100.0) / 100.0;
        }

        test.getMetadata().setIntervaleNote(intervale);
        System.out.println("Intervalele au fost salvate cu succes.");
    }

    public static void main(String[] args) {
        aplicatieEditor app = new aplicatieEditor();
        app.start();
    }
}