package SmartFilePackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileManager implements SmartFileInterfaces{
    BufferedReader bfReader;
    BufferedWriter bfAppender;
    Scanner terminalScanner;
    String path;
    
    public LocalFileManager(String pat, Scanner terminalScanner){
        try {
            path = pat;
            Path path = Paths.get(pat);
            if(Files.notExists(path)){
                Files.createFile(path);
            }
            bfReader = new BufferedReader(new FileReader(pat));
            bfAppender = new BufferedWriter(new FileWriter(pat, true));
            this.terminalScanner = terminalScanner;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void FileReader(){
        String st;
        try {
            while ((st = bfReader.readLine()) != null) {
                System.out.println(st);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void completeFileReader() {
        String st;
        try(BufferedReader bfComplete = new BufferedReader(new FileReader(path))){
            while((st = bfComplete.readLine()) != null){
                System.out.println(st);
            }
            bfComplete.close();
        }catch(IOException e){
            System.out.println("Failed to Read : " + e);
        }
        
    }

    @Override
    public void FileWriter(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            while (terminalScanner.hasNextLine()) {
                String line = terminalScanner.nextLine();
                if (":quit".equals(line)) {
                    break;
                }

                sb.append(line).append(System.lineSeparator());
            }

            bfAppender.write(sb.toString());
            bfAppender.flush();

        } catch(IOException e) {
            System.out.println(e);
        }
        
    }

    @Override
    public void FileDeleter(String pat) {
        Path path = Paths.get(pat);
        try{
            this.close();
            boolean isDeleted = Files.deleteIfExists(path);
            if(isDeleted) System.out.println("Successfully Deleted, LOC : " + pat);
            else System.out.println("Failed to Delete");
        }catch(IOException e){
            System.out.println("Error occured : " + e);
        }
    }

    public void close(){
        try {
            bfReader.close();
            bfAppender.close();
        } catch (IOException e) {
            System.out.println("Failed to Close : " + e);
        }
    }

}