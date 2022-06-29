package com.example.groceriesmanager;
import android.util.Log;

import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;

import java.util.*;

public class Lemma { // visit https://stanfordnlp.github.io/CoreNLP/lemma.html
    private static final String TAG = "Lemma";


    public static String text = "Marie was born in Paris.";

    public static void main(String[] args) {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object
//        CoreDocument document = pipeline.processToCoreDocument(text);
        // display tokens
//        for (CoreLabel tok : document.tokens()) {
//            System.out.println(String.format("%s\t%s", tok.word(), tok.lemma()));
//    }



}
