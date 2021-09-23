import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class MainClass {

    public static void main(String[] args) throws IOException, InterruptedException {


        while(!false){
            runProgramm();
            TimeUnit.SECONDS.sleep(10);
        }


    }

    /**get all headlines from orf.at; save in output.txt*/
    public static void runProgramm() throws IOException {

        System.out.println(System.currentTimeMillis());

        String content = getStringFromURL("https://www.orf.at");

        String[] headlines = extractheadline(content);

        writeBufferedWriterNoDupl("C:\\Users\\codersbay\\Intellij-workspace\\FileIOExceptions\\src\\output.txt", headlines);
    }

    /**get full page with html as string*/
    public static String getStringFromURL(String requestURL) throws IOException {

        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }


    /**get all headlines from html string; return as string array*/
    public static String[] extractheadline(String text){

        // nocht nicht fertig; soll das html im String nach den Überschriften splitten; dann return die gesplitteten strings

        String[] parts = text.split("<h3 class=\"ticker-story-headline\">");        // parts[] zum halbieren vor der headline
        String[][] parts2 = new String[parts.length][2];


        for(int i = 0; i < parts.length; i++){
            parts2[i] = parts[i].split("<div class=\"ticker-story-text\"");     // jeder string in parts[] wird erneut halbiert; parts2[i][0] enthält überschrift + html; parts2[0][0] enthält html header ohne überschrift
        }



        String[] ueberschrift = new String[parts2.length-1];          // ueberschrift[] für ueberschriften mit html


        for (int i = 1; i < parts2.length; i++){
            ueberschrift[i-1] = parts2[i][0];
        }

        // leerzeichen entfernen:

        for(int i = 0; i < ueberschrift.length; i++){
            ueberschrift[i] = ueberschrift[i].replaceAll("\\s+", " ");
        }


        // noch mehr html raussplitten

        String[][] ueberschrift2 = new String[ueberschrift.length][];


        for (int i = 0; i < ueberschrift.length; i++){
            ueberschrift2[i] = ueberschrift[i].split("aria-expanded=\"false\"> ");
        }

        // ueberschrift reuse -> "ueberschrift ___ html"

        for (int i = 0; i < ueberschrift2.length; i++){
            ueberschrift[i] = ueberschrift2[i][1];
        }


        // letztes html raussplitten

        for (int i = 0; i < ueberschrift.length; i++){
            ueberschrift2[i] = ueberschrift[i].split(" </a> </h3> ");
        }

        for (int i = 0; i < ueberschrift2.length; i++){
            ueberschrift[i] = ueberschrift2[i][0];
        }

        return ueberschrift;

    }





    /**read file, print in console*/
    public static void readScanner(String filepath) throws IOException {

        File libraryFile = new File(filepath);
        Scanner fileScanner = new Scanner (libraryFile);
        while (fileScanner.hasNextLine()){
            System.out.println(fileScanner.nextLine());
        }
    }

    /**read all lines from file; return as single string*/
    public static String readBufferedReader(String filepath) throws IOException {

        BufferedReader reader = new BufferedReader(
                new FileReader(filepath)
        );

        String currentLine = reader.readLine();
        String allLines = currentLine;
        while(currentLine != null) {
            currentLine = reader.readLine();
            if(currentLine != null){
                allLines = allLines + "\n" + currentLine;
            }
        }


        reader.close();

        return allLines;
    }


    public static List FileClass(String filepath) throws IOException {

        List<String> allLines = Files.readAllLines(Path.of(filepath));
        return allLines;
    }

    /**save string in file*/
    public static void writeBufferedWriter(String filepath, String text) throws IOException{

        BufferedWriter writer = new BufferedWriter(
                new FileWriter(filepath)
        );

        writer.append(text);
        writer.close();

    }

    /**save string array in file; does not save strings which are already in the file; break after each string; println new strings*/
    public static void writeBufferedWriterNoDupl(String filepath, String[] textarray) throws IOException{

        String file = readBufferedReader(filepath);

        BufferedWriter writer = new BufferedWriter(
                new FileWriter(filepath)
        );

        if(file != null){
            writer.append(file);
            writer.append(("\n"));
        }

        // if file == null, write text; if file != null, check if headline is already in file
        for(int i = 0; i < textarray.length; i++) {
            if (file != null) {
                if (!file.contains(textarray[i])) {
                    writer.append(textarray[i]);
                    System.out.println(textarray[i]);
                    writer.newLine();
                }
            }
            else{
                writer.append(textarray[i]);
                System.out.println(textarray[i]);
                writer.newLine();
            }
        }


        writer.close();

    }





}
