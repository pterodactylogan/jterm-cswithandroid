/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private static final String TAG = "SampleDictionary";
    private ArrayList<String> words;
    private Random mRandom;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @VisibleForTesting
    public SimpleDictionary(ArrayList<String> words, long randomSeed) {
        this.words = words;
        mRandom = new Random(randomSeed);
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random rand = new Random();
        if(prefix=="" || prefix==null) {
            int index = rand.nextInt(words.size());
            return words.get(index);
        }else{
            return binarySearch(prefix, 0, words.size(), words);
        }
    }

    String binarySearch(String prefix, int min, int max, ArrayList<String> words){
        if(min>max) return null;
        int mid = (min+max)/2;
        String sub = words.get(mid);
        String fix = prefix;
        if(words.get(mid).length()>prefix.length()) {
            sub = words.get(mid).substring(0, prefix.length());
        }
        if(prefix.compareTo(sub)==0){
            return words.get(mid);
        }else if(fix.compareTo(sub)>0){
            min=mid+1;
            return binarySearch(prefix, min, max, words);
        }else{
            max=mid-1;
            return binarySearch(prefix,min,max,words);
        }
    }

    int biIndexSearch(String prefix, int min, int max, ArrayList<String> words){
        if(min>max) return null;
        int mid = (min+max)/2;
        String sub = words.get(mid);
        String fix = prefix;
        if(words.get(mid).length()>prefix.length()) {
            sub = words.get(mid).substring(0, prefix.length());
        }
        if(prefix.compareTo(sub)==0){
            return mid;
        }else if(fix.compareTo(sub)>0){
            min=mid+1;
            return binarySearch(prefix, min, max, words);
        }else{
            max=mid-1;
            return binarySearch(prefix,min,max,words);
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        
        String selected = null;
        int index = biIndexSearch(prefix, 0, words.size(), words);
        while(words(index).length()>=prefix && words.get(mid).substring(0, prefix.length()).equals(prefix)){
            index--;
        }
        while(words(index).length()>=prefix && words.get(mid).substring(0, prefix.length()).equals(prefix)){

        }
        return selected;
    }
}
