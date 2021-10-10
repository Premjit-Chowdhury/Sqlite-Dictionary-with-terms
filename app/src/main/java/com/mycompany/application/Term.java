package com.mycompany.application;
import java.io.Serializable;

public class Term implements Serializable {
    // Declare the member variables
    private long id;
    private String words;
    private String meaning;

    // Generate the constructor and choose all member variables to initialize by the
    // constructor
    public Term(long id, String words, String meaning) {
        this.id = id;
        this.words = words;
        this.meaning = meaning;
    }

    // Generate getter methods for all the member variables

    public long getId() {
        return id;
    }

    

    public String getWords() {
        return words;
    }

    

    public String getMeaning() {
        return meaning;
    }
    
    

    
}



