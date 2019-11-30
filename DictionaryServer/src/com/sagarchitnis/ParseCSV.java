package com.sagarchitnis;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class ParseCSV {
    public static void main(String args[]){
        try{
            HashMap<String, String> dictionary = new HashMap<String, String>();
            List<String> wordPair = Files.readAllLines(Paths.get("/Users/sagar/Desktop/dict.csv")); //Read pre-defined dict file from disk

            //Enumerating file contents onto hash map
            for(String i : wordPair){
                String []wordPairResult = i.split(",");
                    dictionary.put(wordPairResult[0].trim(), wordPairResult[1].trim());
            }

            System.out.println(dictionary);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
