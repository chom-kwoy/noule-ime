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
        if (hanjaDictInitialized) {
            // Already initialized
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                Log.i("MYLOG", "Loading Hanja dict...");

                HashMap<String, Integer> frequencyMap = new HashMap<>();
                readFreqFile(assetManager, "freq-hanja.txt", frequencyMap);
                readFreqFile(assetManager, "freq-hanjaeo.txt", frequencyMap);

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

                    hanjaDict.computeIfAbsent(
                        hangulDecomposed,
                        k -> new Vector<>()
                    ).add(entry);
                }
                hanjaTxtFile.close();

                for (Map.Entry<String, Vector<HanjaDictEntry>> item : hanjaDict.entrySet()) {
                    item.getValue().sort((a, b) -> b.freq - a.freq);
                }

                hanjaDictInitialized = true;
                Log.i("MYLOG", "Hanja dict Loaded.");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
}
