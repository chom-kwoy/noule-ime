package org.chocassye.noule.lang;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

public class SymbolData {
    public static HashMap<String, Vector<String>> symbolMap = new HashMap<>();

    public static void initialize(AssetManager assetManager)  {
        try {
            InputStream symbolFile = assetManager.open("mssymbol.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(symbolFile));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                String[] words = line.split(":");
                String key = words[0];
                String value = words[1];

                symbolMap.computeIfAbsent(key, k -> new Vector<>()).add(value);
            }
            symbolFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
