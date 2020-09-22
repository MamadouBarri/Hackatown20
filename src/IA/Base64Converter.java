package IA;

import org.apache.commons.codec.binary.Base64;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Base64Converter {
    String encodedFile = null;
    public String getEncodedFile(){
        return this.encodedFile;
    }
    public void setEncodedFile(String encodedFile){
        this.encodedFile = encodedFile;
    }

    public String encodeFileToBase64Binary(String path) throws IOException {
        try {
            //
            byte[] imageData = Base64.encodeBase64(Files.readAllBytes(Paths.get(path)));
            //byte[] fileContent = FileUtils.readFileToByteArray(file);
            System.out.println();
           String encodedString =  new String(imageData);
           // System.out.println(encodedString);
            setEncodedFile(encodedString);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return this.encodedFile;
    }
}


