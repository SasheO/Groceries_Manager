//package com.example.groceriesmanager;
//import android.util.Log;
//
//import edu.stanford.nlp.io.*;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.pipeline.*;
//
//import java.util.*;
//
//public class Lemma { // visit https://stanfordnlp.github.io/CoreNLP/lemma.html
//    private static final String TAG = "Lemma";
//
//    public String lemmatize(String query) {
//        String result = "";
//
//        // set up pipeline properties
//        Properties props = new Properties();
//        // set the list of annotators to run
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
//        // build pipeline
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        // create a document object
//        // create a document object
//        CoreDocument document = new CoreDocument(query);
//        // annotate the document
//        pipeline.annotate(document);
//        // display tokens
//        for (CoreLabel tok : document.tokens()) {
//            result = result + " " + tok.lemma();
//            Log.i(TAG, tok.word() + " " + tok.lemma());
//        }
//    return result;
//}
//
//}
