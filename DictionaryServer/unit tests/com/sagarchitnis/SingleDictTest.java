package com.sagarchitnis;

import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/*
Contains all JUnit Test Cases for Dictionary Operations
 */

public class SingleDictTest {

    SingleDict dict = SingleDict.getInstance();

    @Test //Proving that Single Dict Successfully follows the singleton design pattern
    public void testGetSingleInstance() {
        SingleDict another_dict = SingleDict.getInstance(); //Creating another dict
        dict.addWord("sharing", "act of splitting with others"); // Adding a new key-value to initial dict obj
        assertTrue(another_dict.mappings.containsKey("sharing".toUpperCase())); // Value still reflecting the new dictionary object
        assertEquals(dict, another_dict); // Both are the same, just two different reference names
    }

    @Test
    public void testQueryWord() {
        dict.addWord("apple", "Red Fruit");
        assertEquals("Red Fruit", dict.queryWord("apple"));
    }

    @Test
    public void testAddWord() {
        dict.addWord("Antarctica", "Cold Southern Continent");
        assertTrue(dict.mappings.containsKey("Antarctica".toUpperCase()));
    }

    @Test
    public void testDeleteWord() {
        dict.deleteWord("apple");
        assertFalse(dict.mappings.containsKey("apple".toUpperCase()));
    }
}