package org.chocassye.noule.lang;

import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HanjaDict {
    public static final HashMap<String, Integer> frequencyMap = new HashMap<>();

    public static class HanjaDictEntry {
        public String hangul;
        public String hanja;
        public String[] meanings;
        public int freq;
    }
    public static final Trie<String, Vector<HanjaDictEntry>> hanjaDict = new PatriciaTrie<>();
    private static boolean hanjaDictInitialized = false;
    public static boolean isHanjaDictInitialized() {
        return hanjaDictInitialized;
    }

    public static final Trie<String, Vector<String>> cangjieDict = new PatriciaTrie<>();
    private static boolean cangjieDictInitialized = false;
    public static boolean isCangjieDictInitialized() {
        return cangjieDictInitialized;
    }

    public static final HashMap<String, String> cangjieLayout = new HashMap<>();
    static {
        cangjieLayout.put("Q", "手");
        cangjieLayout.put("W", "田");
        cangjieLayout.put("E", "水");
        cangjieLayout.put("R", "口");
        cangjieLayout.put("T", "廿");
        cangjieLayout.put("Y", "卜");
        cangjieLayout.put("U", "山");
        cangjieLayout.put("I", "戈");
        cangjieLayout.put("O", "人");
        cangjieLayout.put("P", "心");
        cangjieLayout.put("A", "日");
        cangjieLayout.put("S", "尸");
        cangjieLayout.put("D", "木");
        cangjieLayout.put("F", "火");
        cangjieLayout.put("G", "土");
        cangjieLayout.put("H", "竹");
        cangjieLayout.put("J", "十");
        cangjieLayout.put("K", "大");
        cangjieLayout.put("L", "中");
        cangjieLayout.put("Z", "重");
        cangjieLayout.put("X", "難");
        cangjieLayout.put("C", "金");
        cangjieLayout.put("V", "女");
        cangjieLayout.put("B", "月");
        cangjieLayout.put("N", "弓");
        cangjieLayout.put("M", "一");
    }

    public static String toCangjie(String input) {
        StringBuilder output = new StringBuilder();
        for (char ch : input.toCharArray()) {
            output.append(cangjieLayout.getOrDefault(String.valueOf(ch), ""));
        }
        return output.toString();
    }

    private static void readFreqFile(
            AssetManager assetManager,
            String filename,
            HashMap<String, Integer> frequencyMap
    ) throws IOException {
        InputStream freqHanjaFile = assetManager.open(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(freqHanjaFile));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.strip();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            String[] words = line.split(":");
            String hanja = words[0];
            int frequency = Integer.parseInt(words[1]) % 1000000;

            frequencyMap.put(hanja, frequency);
        }
        freqHanjaFile.close();
    }

    public static void initializeAsync(AssetManager assetManager) {
        if (!hanjaDictInitialized) {
            new Thread(() -> initializeHanjaDict(assetManager)).start();
        }
        if (!cangjieDictInitialized) {
            new Thread(() -> initializeCangjieDict(assetManager)).start();
        }
    }

    public static void initializeHanjaDict(AssetManager assetManager) {
        try {
            Log.i("MYLOG", "Loading Hanja dict...");

            readFreqFile(assetManager, "freq-hanja.txt", frequencyMap);
            readFreqFile(assetManager, "freq-hanjaeo.txt", frequencyMap);

            Log.i("MYLOG", "Read frequencies.");

            InputStream hanjaTxtFile = assetManager.open("hanja.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(hanjaTxtFile));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                String[] words = line.split(":");
                String hangul = words[0];
                String hangulDecomposed = HangulData.decomposeHangul(hangul);

                HanjaDictEntry entry = new HanjaDictEntry();
                entry.hangul = hangul;
                entry.hanja = words[1];
                entry.meanings = words.length >= 3? words[2].split(", ") : null;
                entry.freq = frequencyMap.getOrDefault(entry.hanja, 0);

                hanjaDict.computeIfAbsent(hangulDecomposed, k -> new Vector<>()).add(entry);
            }
            hanjaTxtFile.close();

            Log.i("MYLOG", "Sorting by freq...");

            for (Map.Entry<String, Vector<HanjaDictEntry>> item : hanjaDict.entrySet()) {
                item.getValue().sort((a, b) -> b.freq - a.freq);
            }

            hanjaDictInitialized = true;
            Log.i("MYLOG", "Hanja dict Loaded.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initializeCangjieDict(AssetManager assetManager) {
        try {
            Log.i("MYLOG", "Loading Cangjie dict...");

            InputStream unihanFile = assetManager.open("Unihan_DictionaryLikeData.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(unihanFile));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                String[] words = line.split("\t");
                String category = words[1];

                if (category.equals("kCangjie")) {
                    String unicode = Character.toString(
                            Integer.parseInt(words[0].substring(2), 16));
                    String cangjie = words[2];

                    cangjieDict.computeIfAbsent(cangjie, k -> new Vector<>()).add(unicode);
                }
            }

            cangjieDictInitialized = true;

            Log.i("MYLOG", "Cangjie dict loaded.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
