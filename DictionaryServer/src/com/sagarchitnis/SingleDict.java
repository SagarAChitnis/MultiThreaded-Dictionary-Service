package com.sagarchitnis;

/*
Author: Sagar A. Chitnis
Student ID: 1094691
email-id: sagar.chitnis@student.unimelb.edu.au
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public final class SingleDict {

    public HashMap<String, String> mappings = new HashMap<>();

    private static final SingleDict dictionary = new SingleDict();
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    private SingleDict(){
    }

    public static SingleDict getInstance(){
        return dictionary;
    }

    public static void csvListToDict(List<String> wordPair){
        for(String i : wordPair){
            String []wordPairResult = i.split(",");
            dictionary.mappings.put(wordPairResult[0].trim(), wordPairResult[1].trim());
        }
    }

    public String queryWord(String word){
        lock.readLock().lock();
        try {
            return this.mappings.get(word.toUpperCase());
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean addWord(String word, String meaning){
        lock.writeLock().lock();
        try {
            if (this.mappings.get(word.toUpperCase()) != null) {
                return false;
            }
            this.mappings.put(word.toUpperCase(), meaning.trim());
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deleteWord(String word){
        lock.writeLock().lock();
        try {
            this.mappings.remove(word.toUpperCase());
        } finally {
            lock.writeLock().unlock();
        }
    }
}

