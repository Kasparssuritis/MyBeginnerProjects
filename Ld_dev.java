import java.util.Scanner;
import java.lang.String;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ld_dev {
    // programma
    // ---------------------------------------------------------------------------
    public static void main(String[] args) throws FileNotFoundException {

        String[] table = new String[6];// ID;vards;uzvards;datums;cena;apmaksats

        Scanner scanInput;// pamat lasitajs

        String operation;// operacijas izvelei

        String fileName = "Pasutijumu Uzskaite";

        boolean isOn = true;// rada vai programma ir ieslegta/darbojas

        String separator = File.separator;

        String path = separator + "Users" + separator + "erike" + separator + "Desktop" + separator;

        File f = new File(path + fileName + ".txt");

        table = iniTable(table);

        while (isOn) {
            scanInput = new Scanner(System.in);

            // programmas sakums
            System.out.println("\n\nChoose an option(Type options name or number)");
            System.out.println("1.Print 2.Add 3.Delete 4.Edit 5.Sort 6.Find 7.Calculate summary price(sum) 8.Exit\n");
            System.out.print("Option: ");
            operation = (scanInput.nextLine()).toLowerCase();// lietotajs izvelas operaciju

            switch (operation.toLowerCase()) {

                case "8":// exit
                case "exit":
                case "e":
                    isOn = exitFile();
                    break;
                // ----------------------

                case "1":// print
                case "print":
                case "show":
                    print(f, table);
                    break;
                // ----------------------

                case "2":// add
                case "add":
                case "adding":
                    System.out.print("Add(ID;name;surename;date;price;paid): ");
                    addItem(f, scanInput.nextLine().toLowerCase());
                    break;
                // ----------------------

                case "3":// delete
                case "delete":
                case "del":
                    System.out.print("Delete(ID): ");
                    delItem(f, scanInput.nextLine().toLowerCase());
                    break;
                // ----------------------

                case "4":// edit
                case "edit":
                case "editing":
                    System.out.print("Edit(ID changes;changes;changes;changes;changes): ");
                    editItem(f, scanInput.nextLine().toLowerCase());
                    break;
                // ----------------------

                case "5":// sort
                case "sort":
                case "sorting":
                    System.out.println("Sort(sorting by 1.Date or 2.Surname): ");
                    sort(f, scanInput.nextLine().toLowerCase());
                    break;
                // ----------------------

                case "6":// find
                case "find":
                case "search":
                    print(f, table);
                    findItem(f);
                    break;
                // ----------------------

                case "7":// avarage
                case "calc sum":
                case "sum":
                case "summ":
                    System.out.print("Sum of prices of all orders: ");
                    System.out.printf("%.4f", calcSum(f));
                    break;
                // ----------------------

                default:// nepareiza ievade
                    System.out.println("There is no such command/operation! Try again!");
                    break;
                // ----------------------
            }
        }

        return;

    }
    // -----------------------------------------------------------------

    public static String[] iniTable(String[] table) {// tabulas header-a nosaukumi

        // tabulas konstrukcijas pamats
        // | ID | Vards | Uzvards | Datums | Cena | Apmaksats |
        table[0] = "ID";
        table[1] = "Vards";
        table[2] = "Uzvards";
        table[3] = "Sanemsana";
        table[4] = "Cena";
        table[5] = "Apmaksats";

        return table;
    }

    // iziet no faila exit
    // ------------------------------------------------------------------------
    public static boolean exitFile() {
        return false;
    }
    // -----------------------------------------------------------------------

    // izvade print
    // ---------------------------------------------------------------------------
    public static void print(File f, String[] table) throws FileNotFoundException {

        Scanner sc = new Scanner(f);

        // headers(ID, Vards, Uzvards, Sanemsanas datums, Cena, Samaksataa summa)

        String border = "---------------------------------------------------------------------";
        System.out.println(border);
        System.out.printf("%-5s %-14s %-14s %-12s %-10s %-10s\n",
                table[0], table[1], table[2], table[3], table[4], table[5]);
        System.out.println(border);

        String[] line;

        // izmantojot konstrukcijas pamatu table sadalam katru rindu
        // un korekti izvadam tas elementus
        while (sc.hasNextLine()) {

            line = sc.nextLine().split(";");

            System.out.printf("%-5s %-14s %-14s %-12s %-10s %-10s\n",
                    line[0], line[1], line[2], line[3], line[4], line[5]);
        }

        System.out.println(border);

        sc.close();
    }
    // -----------------------------------------------------------------

    // PAMAT-FUNKCIJAS!!!

    // pievienot elementu add
    // -----------------------------------------------------------------
    public static void addItem(File f, String input) throws FileNotFoundException {

        if (!checkValidInputAdd(input)) {// parbaudam ievadi
            System.out.println("Wrong input!");
            return;
        }

        // try catch parbaudam vai sanacas atver failu kura rakstisim.
        // parbaudam vai nav tada pasa ID ievadei un ierakstam datus faila
        try (FileWriter writer = new FileWriter(f, true)) {
            Scanner sc = new Scanner(f);

            String[] temps = input.split(";");

            while (sc.hasNextLine()) {
                String[] tempstr = sc.nextLine().split(";");

                if (tempstr[0].equals(temps[0])) {// ja id sakrit izejam no add funkcijas
                    System.out.println("There already is item with such ID! Try again!");
                    sc.close();
                    return;
                }
            }

            sc.close();

            // parveidojam varda un uzvarda pirmos burtus
            // no mazajiem uz lielajiem
            char[] tempc = temps[1].toCharArray();
            tempc[0] = Character.toUpperCase(tempc[0]);

            temps[1] = new String(tempc);

            tempc = temps[2].toCharArray();
            tempc[0] = Character.toUpperCase(tempc[0]);

            temps[2] = new String(tempc);

            // konstruejam rindu ierakstisanai faila
            String output = "\n" + temps[0] + ";" + temps[1] + ";" + temps[2] + ";" + temps[3] + ";" + temps[4] + ";"
                    + temps[5];
            writer.write(output);

        } catch (IOException ex) {
            System.out.println("Unable to open file");
            return;
        }

        System.out.println("New item was added successfully!");
        return;
    }
    // -----------------------------------------------------------------

    // dzest elementu delete
    // -----------------------------------------------------------------
    public static void delItem(File f, String input) throws FileNotFoundException {
        boolean isThereSuchElem = false;

        // id ievades parbaude
        if (!checkValidID(input)) {
            System.out.println("Wrong input!");
            return;
        }

        // izveidojam laikus failu programas mape
        File ftemp = new File("tempFile.txt");

        try (FileWriter cwriter = new FileWriter(ftemp, true)) {
            Scanner sc = new Scanner(f);
            int lineCounter = 0;

            // parrakstam visu informaciju no musu original faila uz jauno failu
            // nerakstam to elementu kuru gribam izdzest
            while (sc.hasNextLine()) {
                String temps = sc.nextLine();
                String[] smas = temps.split(";");

                if ((smas[0].equals(input))) {
                    isThereSuchElem = true;
                } else {
                    cwriter.write(temps + "\n");
                    lineCounter++;
                }
            }
            // aizveram visu pec darba izpildes
            cwriter.close();
            sc.close();

            // attiram originalo failu
            PrintWriter deleter = new PrintWriter(f);
            deleter.print("");
            deleter.close();

            FileWriter writer = new FileWriter(f, true);

            Scanner sctemp = new Scanner(ftemp);

            // rakstam original faila to kas mums ir musu laikus faila
            // visi dati iznemot tos kurus gribejam izdzest
            while (sctemp.hasNextLine()) {
                String line = sctemp.nextLine();
                lineCounter--;
                if (lineCounter == 0) {
                    writer.write(line);
                } else {
                    writer.write(line + "\n");
                }

            }

            // aizveram visu pec darba izpildes
            sctemp.close();
            ftemp.delete();// dzesam laikus failu
            writer.close();

        } catch (IOException ex) {
            System.out.println("Unable to open file");
            return;
        }

        if (isThereSuchElem) {
            System.out.println("Item " + input + " was deleted successfully!");

        } else {
            System.out.println("There is no element with id " + input);

        }

        return;
    }
    // -----------------------------------------------------------------

    // mainit elementa saturu edit
    // -----------------------------------------------------------------
    public static void editItem(File f, String input) throws FileNotFoundException {
        String[] str = input.split(" ");
        boolean isThereSuchElem = false;
        String editString = "";

        if (str.length != 2) {
            System.out.println("Wrong input!");
            return;
        }

        String id = str[0];// ievaditais id
        String changes = str[1].toLowerCase();// ievaditas izmainas

        if (!checkValidID(id)) {// id
            System.out.println("Wrong input!");
            return;
        }

        Scanner sc = new Scanner(f);

        while (sc.hasNextLine()) {
            String temps = sc.nextLine();
            String[] line = temps.split(";");

            if (id.equals(line[0])) {
                isThereSuchElem = true;
                editString = temps;
            }
        }

        if (!isThereSuchElem) {// ja nav elementa ar tadu ID
            System.out.println("There is no element with such ID!");
            sc.close();
            return;
        }

        // parbaudam ievadi izmainam
        String[] strChanges = changes.split(";", -1);
        String[] strEditString = editString.split(";");

        // ievadito izmainu parbaude
        if (strChanges.length != 5) {
            System.out.println("Wrong input!");
            sc.close();
            return;
        }

        // parbaude
        if (!strChanges[0].equals("")) {// vards
            if (!checkValidName(strChanges[0])) {
                System.out.println("Wrong input!");
                sc.close();
                return;
            }
        }

        if (!strChanges[1].equals("")) {// uzvards
            if (!checkValidName(strChanges[1])) {
                System.out.println("Wrong input!");
                sc.close();
                return;
            }
        }

        if (!strChanges[2].equals("")) {// datums
            if (!checkValidDate(strChanges[2])) {
                System.out.println("Wrong input!");
                sc.close();
                return;
            }
        }

        if (!strChanges[3].equals("")) {// cena
            if (!checkValidPrice(strChanges[3])) {
                System.out.println("Wrong input!");
                sc.close();
                return;
            }
        }

        if (!strChanges[4].equals("")) {// apmaksats
            if (!checkValidPrice(strChanges[4])) {
                System.out.println("Wrong input!");
                sc.close();
                return;
            }
        }

        if (!strChanges[3].equals("")) {// apmaksats nevar but lielaks par cenu
            if (Double.parseDouble(strChanges[3]) < Double.parseDouble(strEditString[5])) {
                System.out.println("Wrong input!");
                sc.close();
                return;
            }
        }

        if (!strChanges[4].equals("")) {// apmaksats nevar but lielaks par cenu
            if (Double.parseDouble(strEditString[4]) < Double.parseDouble(strChanges[4])) {
                System.out.println("Wrong input!");
                sc.close();
                return;
            }
        }

        // konstruejam redigeto rindu/elementu
        editString = strEditString[0];// id

        for (int i = 0; i < strChanges.length; i++) {

            editString = editString + ";";

            if (!strChanges[i].equals("")) {

                if (i == 0 || i == 1) {// parveidojam varda un uzvarda pirmos burtus par lielajiem
                    char[] cmas = strChanges[i].toCharArray();
                    cmas[0] = Character.toUpperCase(cmas[0]);

                    strChanges[i] = new String(cmas);
                }

                strEditString[i + 1] = strChanges[i];

            }
            editString = editString + strEditString[i + 1];
        }

        File ftemp = new File("temp.txt");
        Scanner scan = new Scanner(f);

        try (FileWriter cwriter = new FileWriter(ftemp, true)) {
            int lineCounter = 0;

            // parrakstam visu informaciju no musu original faila uz jauno failu
            // sakotnejas rindas vieta rakstam redakteto rindu
            while (scan.hasNextLine()) {
                String temps = scan.nextLine();
                String[] smas = temps.split(";");

                if ((smas[0].equals(id))) {
                    cwriter.write(editString + "\n");
                } else {
                    cwriter.write(temps + "\n");
                }

                lineCounter++;
            }

            // aizveram visu pec darba izpildes
            cwriter.close();
            scan.close();

            // attiram originalo failu
            PrintWriter deleter = new PrintWriter(f);
            deleter.print("");
            deleter.close();

            FileWriter writer = new FileWriter(f, true);

            Scanner sctemp = new Scanner(ftemp);

            // rakstam original faila to kas mums ir musu laikus faila
            // visi dati iznemot tos kurus gribejam izdzest
            while (sctemp.hasNextLine()) {
                String line = sctemp.nextLine();
                lineCounter--;

                if (lineCounter == 0) {
                    writer.write(line);
                } else {
                    writer.write(line + "\n");
                }

            }

            // aizveram visu pec darba izpildes
            sctemp.close();
            ftemp.delete();// dzesam laikus failu
            writer.close();

        } catch (IOException ex) {
            System.out.println("Unable to open file");
            sc.close();
            return;
        }

        System.out.println("Item " + id + " was edited successfully!");
        sc.close();
        return;
    }
    // -----------------------------------------------------------------

    // skirosana sort
    // -----------------------------------------------------------------
    public static void sort(File f, String op) throws FileNotFoundException {

        switch (op) {
            case "1":
            case "date":
                sortByDate(f);
                break;
            case "2":
            case "surname":
                sortBySurname(f);
                break;
            default:
                System.out.println("Wrong input!");
                return;
        }

        return;
    }
    // -----------------------------------------------------------------

    // atrast find
    // elementu-----------------------------------------------------------------
    public static void findItem(File f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);

        System.out.println("\nAre not paid fully:\n");

        while (sc.hasNextLine()) {

            String line = sc.nextLine();
            String[] parts = line.split(";");

            if (Double.parseDouble(parts[4]) > Double.parseDouble(parts[5])) {
                System.out.println(line);
            }

        }

        sc.close();
        return;
    }
    // -----------------------------------------------------------------

    // aprekinat vid.
    // aritmetisko-----------------------------------------------------------------
    public static double calcSum(File f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        double sum = 0;

        while (sc.hasNextLine()) {
            String[] smas = sc.nextLine().split(";");
            sum += Double.parseDouble(smas[4]);

        }

        sc.close();
        return sum;

    }
    // ------------------------------------------------------------------------

    // PALIG-FUNKCIJAS!!!

    // parbauda ievadi id;vards;uzvards;datums;cena;apmaksats
    // funkcijai add
    // ------------------------------------------------------------------------
    public static boolean checkValidInputAdd(String str) {
        char[] cmas = str.toCharArray();
        int counter = 0;

        // skaitam ; simbolu skaitu
        for (int i = 0; i < cmas.length; i++) {
            if (cmas[i] == ';') {
                counter++;
            }
        }

        // ja ; simboli nav pieci ievade ir nepareiza
        if (counter != 5)
            return false;

        // parbaude vairaku ; simbolu pec kartas
        for (int i = 0; i < cmas.length; i++) {
            if (cmas[i] == ';') {
                if (cmas[i] == cmas[i + 1])
                    return false;
            }
        }

        // sadalam elementus pa input masivu
        String[] input = str.split(";");

        if (!checkValidID(input[0])) {// id
            return false;
        }

        if (!checkValidName(input[1])) {// vards
            return false;
        }

        if (!checkValidName(input[2])) {// uzvards
            return false;
        }

        if (!checkValidDate(input[3])) {// datums
            return false;
        }

        if (!checkValidPrice(input[4])) {// cena
            return false;
        }

        if (!checkValidPrice(input[5])) {// apmaksats
            return false;
        }

        // nevar but apmaksats vairak par cenu
        if (Double.parseDouble(input[4]) < Double.parseDouble(input[5])) {
            return false;
        }

        return true;// true case
    }
    // -----------------------------------------------------------------

    // id
    // check-----------------------------------------------------------------------------
    public static boolean checkValidID(String input) {
        char[] cmas = input.toCharArray();

        if (input.length() != 3) {// id nevar but garaks par 3 un ievadei jabut 001, 011 un 111
            return false;
        }

        for (int i = 0; i < cmas.length; i++) {// parbauda vai ievaditi tikai burti
            if (cmas[i] - '0' < 0 || cmas[i] - '0' > 9) {
                return false;
            }
        }

        return true;
    }
    // -------------------------------------------------------------------------------------

    // vards/uzvards
    // check-----------------------------------------------------------------------------
    public static boolean checkValidName(String input) {
        char[] cmas = input.toCharArray();

        for (int i = 0; i < cmas.length; i++) {

            if (cmas[i] - 'a' < 0 || cmas[i] - 'a' > 25) {// parbaude vai ievaditi tiaki burti
                return false;
            }
        }

        return true;
    }
    // -------------------------------------------------------------------------------------

    // datums
    // check-----------------------------------------------------------------------------
    public static boolean checkValidDate(String input) {
        char[] cmas = input.toCharArray();

        for (int i = 0; i < cmas.length; i++) {// sadalam datuma vertibas starp / simboliem

            if (i == 2 || i == 5) {
                if (cmas[i] - '/' != 0) {
                    return false;
                }

            } else {

                if (cmas[i] - '0' < 0 || cmas[i] - '0' > 9) {
                    // vertibu parbaude vai ievaditi ciparu simboli
                    // ASCII 48 = 0 un ta lidz 57 = 9
                    return false;
                }
            }
        }

        String[] smas = input.split("/");

        if (Integer.parseInt(smas[1]) > 12) {// nevar but vairak par 12 menesiem
            return false;
        }

        if (Integer.parseInt(smas[2]) != 2024) {// nevar but nakamais vai ieprieksejais gads
            return false;
        }

        switch (Integer.parseInt(smas[1])) {// menesu dienu skaits parbaude
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (Integer.parseInt(smas[0]) < 1 || Integer.parseInt(smas[0]) > 31) {// 31 dienas
                    return false;
                }
                break;

            case 2:
                if (Integer.parseInt(smas[0]) < 1 || Integer.parseInt(smas[0]) > 28) {// feb
                    return false;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (Integer.parseInt(smas[0]) < 1 || Integer.parseInt(smas[0]) > 30) {// 30 dienas
                    return false;
                }
                break;
        }

        return true;
    }
    // -------------------------------------------------------------------------------------

    // cena/apmaksats
    // check-----------------------------------------------------------------------------
    public static boolean checkValidPrice(String input) {
        char[] cmas = input.toCharArray(); // input rindu sadalam simbolos

        int counter = 0;
        // parbaudam vai rinda atbilst formatam num.num
        for (int i = 0; i < cmas.length; i++) {
            if (cmas[i] - '.' < 0 || cmas[i] - '/' == 0 || cmas[i] - '0' > 9) {
                // parbaudam vai izmantoti tikai ciparu un punkta simboli
                return false;
            }

            if (cmas[i] == '.') {
                counter++;
            }

            if (counter > 0 && cmas.length - i > 3) {
                return false;
            }
        }

        if (counter > 1) {
            // ja ir vairak neka divi punkti jeb
            // num..num tad ta ir nepareiza ievade
            return false;
        }

        return true;
    }
    // -------------------------------------------------------------------------------------

    public static void sortByDate(File f) throws FileNotFoundException {
        List<String[]> parsedData = new ArrayList<>(); // gataviem datiem

        int lineCounter = 0;
        // lasam failu sadalot to dalas
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {

                String line = sc.nextLine(); // rinda

                String[] parts = line.split(";"); // elementi
                String[] dateParts = parts[3].split("/"); // datuma dalas, dienas un menesi

                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);

                lineCounter++;

                parsedData.add(new String[] { line, String.format("%02d%02d", month, day) });
            }
        }

        // sortejam datus
        parsedData.sort(Comparator.comparing(o -> o[1])); // salidzinam izmantojot formatu MMDD

        // rakstam datus atpakal faila
        try (PrintWriter writer = new PrintWriter(f)) {
            for (String[] data : parsedData) {

                lineCounter--;

                if (lineCounter > 0) {
                    writer.write(data[0] + "\n"); // rakstam datus ieksa
                } else {
                    writer.write(data[0]); // rakstam pedejo rindinu
                }

            }
        }

        System.out.println("\nSuccessfully sorted by date!");
        return;
    }

    public static void sortBySurname(File f) throws FileNotFoundException {

        List<String[]> parsedData = new ArrayList<>(); // gataviem datiem
        int lineCounter = 0; // rindinu skaitisanai

        // lasam un sadalam failus
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine(); // rinda
                String[] parts = line.split(";"); // elementi

                parsedData.add(new String[] { line, parts[2] }); // parts[2] = uzvardi
                lineCounter++;

            }

            parsedData.sort(Comparator.comparing(o -> o[1]));

            // rakstam datus faila
            try (PrintWriter writer = new PrintWriter(f)) {
                for (String[] data : parsedData) {

                    lineCounter--;

                    if (lineCounter > 0) {
                        writer.write(data[0] + "\n"); // rakstam datus ieksa
                    } else {
                        writer.write(data[0]); // rakstam pedejo rindinu
                    }
                }
            }

        }

        System.out.println("\nSuccessfully sorted by surnames!");
        return;
    }
}