package org.chocassye.noule.lang;

public class ManchuData {
    public static String convertToManchu(String text) {
        return text.replace("a", "ᠠ")
                .replace("b", "ᠪ")
                .replace("c", "ᠴ")
                .replace("C", "ᡮ")
                .replace("d", "ᡩ")
                .replace("e", "ᡝ")
                .replace("f", "ᡶ")
                .replace("g", "ᡤ")
                .replace("G", "ᡬ")
                .replace("h", "ᡥ")
                .replace("H", "ᡭ")
                .replace("i", "ᡳ")
                .replace("j", "ᠵ")
                .replace("k", "ᡴ")
                .replace("K", "ᠺ")
                .replace("l", "ᠯ")
                .replace("m", "ᠮ")
                .replace("n", "ᠨ")
                .replace("N", "ᠩ")
                .replace("o", "ᠣ")
                .replace("p", "ᡦ")
                .replace("q", "ᠴ")
                .replace("Q", "ᡱ")
                .replace("r", "ᡵ")
                .replace("R", "ᡰ")
                .replace("s", "ᠰ")
                .replace("t", "ᡨ")
                .replace("u", "ᡠ")
                .replace("v", "ᡡ")
                .replace("w", "ᠸ")
                .replace("x", "ᡧ")
                .replace("y", "ᠶ")
                .replace("z", "ᡯ")
                .replace("Z", "ᡷ");
    }

    public static boolean isManchu(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[\u1800-\u18af]+");
    }
}
