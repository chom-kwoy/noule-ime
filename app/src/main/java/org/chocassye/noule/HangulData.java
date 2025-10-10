package org.chocassye.noule;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class HangulData {
    public static class ConsInfo {
        public String compatChar;
        public String leadingChar;
        public String trailingChar;
        public ConsInfo(String compatChar, String leadingChar, String trailingChar) {
            this.compatChar = compatChar;
            this.leadingChar = leadingChar;
            this.trailingChar = trailingChar;
        }
    }
    public static final HashMap<String, ConsInfo> consInfoMap = new HashMap<>();
    public static class VowelInfo {
        public String compatChar;
        public String vowelChar;
        public VowelInfo(String compatChar, String vowelChar) {
            this.compatChar = compatChar;
            this.vowelChar = vowelChar;
        }
    }
    public static final HashMap<String, VowelInfo> vowelInfoMap = new HashMap<>();
    public static final HashMap<String, String> composeMap = new HashMap<>();
    public static final HashMap<String, String> toCompat = new HashMap<>();

    static {
        consInfoMap.put("ㄱ", new ConsInfo("ㄱ", "ᄀ", "ᆨ"));
        consInfoMap.put("ㄲ", new ConsInfo("ㄲ", "ᄁ", "ᆩ"));
        consInfoMap.put("ㄳ", new ConsInfo("ㄳ", null, "ᆪ"));
        consInfoMap.put("ㄴ", new ConsInfo("ㄴ", "ᄂ", "ᆫ"));
        consInfoMap.put("ㄵ", new ConsInfo("ㄵ", "ᅜ", "ᆬ"));
        consInfoMap.put("ㄶ", new ConsInfo("ㄶ", "ᅝ", "ᆭ"));
        consInfoMap.put("ㄷ", new ConsInfo("ㄷ", "ᄃ", "ᆮ"));
        consInfoMap.put("ㄸ", new ConsInfo("ㄸ", "ᄄ", "ퟍ"));
        consInfoMap.put("ㄹ", new ConsInfo("ㄹ", "ᄅ", "ᆯ"));
        consInfoMap.put("ㄺ", new ConsInfo("ㄺ", "ꥤ", "ᆰ"));
        consInfoMap.put("ㄻ", new ConsInfo("ㄻ", "ꥨ", "ᆱ"));
        consInfoMap.put("ㄼ", new ConsInfo("ㄼ", "ꥩ", "ᆲ"));
        consInfoMap.put("ㄽ", new ConsInfo("ㄽ", "ꥬ", "ᆳ"));
        consInfoMap.put("ㄾ", new ConsInfo("ㄾ", null, "ᆴ"));
        consInfoMap.put("ㄿ", new ConsInfo("ㄿ", null, "ᆵ"));
        consInfoMap.put("ㅀ", new ConsInfo("ㅀ", "ᄚ", "ᆶ"));
        consInfoMap.put("ㅁ", new ConsInfo("ㅁ", "ᄆ", "ᆷ"));
        consInfoMap.put("ㅂ", new ConsInfo("ㅂ", "ᄇ", "ᆸ"));
        consInfoMap.put("ㅃ", new ConsInfo("ㅃ", "ᄈ", "ퟦ"));
        consInfoMap.put("ㅄ", new ConsInfo("ㅄ", "ᄡ", "ᆹ"));
        consInfoMap.put("ㅅ", new ConsInfo("ㅅ", "ᄉ", "ᆺ"));
        consInfoMap.put("ㅆ", new ConsInfo("ㅆ", "ᄊ", "ᆻ"));
        consInfoMap.put("ㅇ", new ConsInfo("ㅇ", "ᄋ", "ᆼ"));
        consInfoMap.put("ㅈ", new ConsInfo("ㅈ", "ᄌ", "ᆽ"));
        consInfoMap.put("ㅉ", new ConsInfo("ㅉ", "ᄍ", "ퟹ"));
        consInfoMap.put("ㅊ", new ConsInfo("ㅊ", "ᄎ", "ᆾ"));
        consInfoMap.put("ㅋ", new ConsInfo("ㅋ", "ᄏ", "ᆿ"));
        consInfoMap.put("ㅌ", new ConsInfo("ㅌ", "ᄐ", "ᇀ"));
        consInfoMap.put("ㅍ", new ConsInfo("ㅍ", "ᄑ", "ᇁ"));
        consInfoMap.put("ㅎ", new ConsInfo("ㅎ", "ᄒ", "ᇂ"));
        consInfoMap.put("ㅥ", new ConsInfo("ㅥ", "ᄔ", null));
        consInfoMap.put("ㅦ", new ConsInfo("ㅦ", "ᄕ", "ᇆ"));
        consInfoMap.put("ㅧ", new ConsInfo("ㅧ", "ᅛ", "ᇇ"));
        consInfoMap.put("ㅨ", new ConsInfo("ㅨ", null, "ᇈ"));
        consInfoMap.put("ㅩ", new ConsInfo("ㅩ", null, "ᇌ"));
        consInfoMap.put("ㅪ", new ConsInfo("ㅪ", "ꥦ", "ᇎ"));
        consInfoMap.put("ㅫ", new ConsInfo("ㅫ", null, "ᇓ"));
        consInfoMap.put("ㅬ", new ConsInfo("ㅬ", null, "ᇗ"));
        consInfoMap.put("ㅭ", new ConsInfo("ㅭ", null, "ᇙ"));
        consInfoMap.put("ㅮ", new ConsInfo("ㅮ", "ᄜ", "ᇜ"));
        consInfoMap.put("ㅯ", new ConsInfo("ㅯ", "ꥱ", "ᇝ"));
        consInfoMap.put("ㅰ", new ConsInfo("ㅰ", null, "ᇟ"));
        consInfoMap.put("ㅱ", new ConsInfo("ㅱ", "ᄝ", "ᇢ"));
        consInfoMap.put("ㅲ", new ConsInfo("ㅲ", "ᄞ", null));
        consInfoMap.put("ㅳ", new ConsInfo("ㅳ", "ᄠ", "ퟣ"));
        consInfoMap.put("ㅴ", new ConsInfo("ㅴ", "ᄢ", null));
        consInfoMap.put("ㅵ", new ConsInfo("ㅵ", "ᄣ", "ퟧ"));
        consInfoMap.put("ㅶ", new ConsInfo("ㅶ", "ᄧ", "ퟨ"));
        consInfoMap.put("ㅷ", new ConsInfo("ㅷ", "ᄩ", null));
        consInfoMap.put("ㅸ", new ConsInfo("ㅸ", "ᄫ", "ᇦ"));
        consInfoMap.put("ㅹ", new ConsInfo("ㅹ", "ᄬ", null));
        consInfoMap.put("ㅺ", new ConsInfo("ㅺ", "ᄭ", "ᇧ"));
        consInfoMap.put("ㅻ", new ConsInfo("ㅻ", "ᄮ", null));
        consInfoMap.put("ㅼ", new ConsInfo("ㅼ", "ᄯ", "ᇨ"));
        consInfoMap.put("ㅽ", new ConsInfo("ㅽ", "ᄲ", "ᇪ"));
        consInfoMap.put("ㅾ", new ConsInfo("ㅾ", "ᄶ", "ퟯ"));
        consInfoMap.put("ㅿ", new ConsInfo("ㅿ", "ᅀ", "ᇫ"));
        consInfoMap.put("ㆀ", new ConsInfo("ㆀ", "ᅇ", "ᇮ"));
        consInfoMap.put("ㆁ", new ConsInfo("ㆁ", "ᅌ", "ᇰ"));
        consInfoMap.put("ㆂ", new ConsInfo("ㆂ", null, "ᇱ"));
        consInfoMap.put("ㆃ", new ConsInfo("ㆃ", null, "ᇲ"));
        consInfoMap.put("ㆄ", new ConsInfo("ㆄ", "ᅗ", "ᇴ"));
        consInfoMap.put("ㆅ", new ConsInfo("ㆅ", "ᅘ", null));
        consInfoMap.put("ㆆ", new ConsInfo("ㆆ", "ᅙ", "ᇹ"));

        vowelInfoMap.put("ㅏ", new VowelInfo("ㅏ", "ᅡ"));
        vowelInfoMap.put("ㅐ", new VowelInfo("ㅐ", "ᅢ"));
        vowelInfoMap.put("ㅑ", new VowelInfo("ㅑ", "ᅣ"));
        vowelInfoMap.put("ㅒ", new VowelInfo("ㅒ", "ᅤ"));
        vowelInfoMap.put("ㅓ", new VowelInfo("ㅓ", "ᅥ"));
        vowelInfoMap.put("ㅔ", new VowelInfo("ㅔ", "ᅦ"));
        vowelInfoMap.put("ㅕ", new VowelInfo("ㅕ", "ᅧ"));
        vowelInfoMap.put("ㅖ", new VowelInfo("ㅖ", "ᅨ"));
        vowelInfoMap.put("ㅗ", new VowelInfo("ㅗ", "ᅩ"));
        vowelInfoMap.put("ㅘ", new VowelInfo("ㅘ", "ᅪ"));
        vowelInfoMap.put("ㅙ", new VowelInfo("ㅙ", "ᅫ"));
        vowelInfoMap.put("ㅚ", new VowelInfo("ㅚ", "ᅬ"));
        vowelInfoMap.put("ㅛ", new VowelInfo("ㅛ", "ᅭ"));
        vowelInfoMap.put("ㅜ", new VowelInfo("ㅜ", "ᅮ"));
        vowelInfoMap.put("ㅝ", new VowelInfo("ㅝ", "ᅯ"));
        vowelInfoMap.put("ㅞ", new VowelInfo("ㅞ", "ᅰ"));
        vowelInfoMap.put("ㅟ", new VowelInfo("ㅟ", "ᅱ"));
        vowelInfoMap.put("ㅠ", new VowelInfo("ㅠ", "ᅲ"));
        vowelInfoMap.put("ㅡ", new VowelInfo("ㅡ", "ᅳ"));
        vowelInfoMap.put("ㅢ", new VowelInfo("ㅢ", "ᅴ"));
        vowelInfoMap.put("ㅣ", new VowelInfo("ㅣ", "ᅵ"));
        vowelInfoMap.put("ㆇ", new VowelInfo("ㆇ", "ᆄ"));
        vowelInfoMap.put("ㆈ", new VowelInfo("ㆈ", "ᆅ"));
        vowelInfoMap.put("ㆉ", new VowelInfo("ㆉ", "ᆈ"));
        vowelInfoMap.put("ㆊ", new VowelInfo("ㆊ", "ᆑ"));
        vowelInfoMap.put("ㆋ", new VowelInfo("ㆋ", "ᆒ"));
        vowelInfoMap.put("ㆌ", new VowelInfo("ㆌ", "ᆔ"));
        vowelInfoMap.put("ㆍ", new VowelInfo("ㆍ", "ᆞ"));
        vowelInfoMap.put("ㆎ", new VowelInfo("ㆎ", "ᆡ"));

        composeMap.put("ㅃㅇ", "ㅹ");
        composeMap.put("ㄱㄱ", "ㄲ");
        composeMap.put("ㄱㅅ", "ㄳ");
        composeMap.put("ㄴㅈ", "ㄵ");
        composeMap.put("ㄴㅎ", "ㄶ");
        composeMap.put("ㄷㄷ", "ㄸ");
        composeMap.put("ㄹㄱ", "ㄺ");
        composeMap.put("ㄹㅁ", "ㄻ");
        composeMap.put("ㄹㅂ", "ㄼ");
        composeMap.put("ㄹㅅ", "ㄽ");
        composeMap.put("ㄹㅌ", "ㄾ");
        composeMap.put("ㄹㅍ", "ㄿ");
        composeMap.put("ㄹㅎ", "ㅀ");
        composeMap.put("ㅂㅂ", "ㅃ");
        composeMap.put("ㅂㅅ", "ㅄ");
        composeMap.put("ㅅㅅ", "ㅆ");
        composeMap.put("ㅈㅈ", "ㅉ");
        composeMap.put("ㅗㅏ", "ㅘ");
        composeMap.put("ㅗㅐ", "ㅙ");
        composeMap.put("ㅗㅣ", "ㅚ");
        composeMap.put("ㅜㅓ", "ㅝ");
        composeMap.put("ㅜㅔ", "ㅞ");
        composeMap.put("ㅜㅣ", "ㅟ");
        composeMap.put("ㅡㅣ", "ㅢ");
        composeMap.put("ㄴㄴ", "ㅥ");
        composeMap.put("ㄴㄷ", "ㅦ");
        composeMap.put("ㄴㅅ", "ㅧ");
        composeMap.put("ㄴㅿ", "ㅨ");
        composeMap.put("ㄹㄱㅅ", "ㅩ");
        composeMap.put("ㄹㄷ", "ㅪ");
        composeMap.put("ㄹㅂㅅ", "ㅫ");
        composeMap.put("ㄹㅿ", "ㅬ");
        composeMap.put("ㄹㆆ", "ㅭ");
        composeMap.put("ㅁㅂ", "ㅮ");
        composeMap.put("ㅁㅅ", "ㅯ");
        composeMap.put("ㅁㅿ", "ㅰ");
        composeMap.put("ㅁㅇ", "ㅱ");
        composeMap.put("ㅂㄱ", "ㅲ");
        composeMap.put("ㅂㄷ", "ㅳ");
        composeMap.put("ㅂㅅㄱ", "ㅴ");
        composeMap.put("ㅂㅅㄷ", "ㅵ");
        composeMap.put("ㅂㅈ", "ㅶ");
        composeMap.put("ㅂㅌ", "ㅷ");
        composeMap.put("ㅂㅇ", "ㅸ");
        composeMap.put("ㅂㅂㅇ", "ㅹ");
        composeMap.put("ㅅㄱ", "ㅺ");
        composeMap.put("ㅅㄴ", "ㅻ");
        composeMap.put("ㅅㄷ", "ㅼ");
        composeMap.put("ㅅㅂ", "ㅽ");
        composeMap.put("ㅅㅈ", "ㅾ");
        composeMap.put("ㅇㅇ", "ㆀ");
        composeMap.put("ㆁㅅ", "ㆂ");
        composeMap.put("ㆁㅿ", "ㆃ");
        composeMap.put("ㅍㅇ", "ㆄ");
        composeMap.put("ㅎㅎ", "ㆅ");
        composeMap.put("ㅛㅑ", "ㆇ");
        composeMap.put("ㅛㅒ", "ㆈ");
        composeMap.put("ㅛㅣ", "ㆉ");
        composeMap.put("ㅠㅕ", "ㆊ");
        composeMap.put("ㅠㅖ", "ㆋ");
        composeMap.put("ㅠㅣ", "ㆌ");
        composeMap.put("ㆍㅣ", "ㆎ");

        for (Map.Entry<String, ConsInfo> entry : consInfoMap.entrySet()) {
            if (entry.getValue().leadingChar != null) {
                toCompat.put(entry.getValue().leadingChar, entry.getKey());
            }
            if (entry.getValue().trailingChar != null) {
                toCompat.put(entry.getValue().trailingChar, entry.getKey());
            }
        }
        for (Map.Entry<String, VowelInfo> entry : vowelInfoMap.entrySet()) {
            if (entry.getValue().vowelChar != null) {
                toCompat.put(entry.getValue().vowelChar, entry.getKey());
            }
        }
    }

    private enum State {
        EMPTY,
        LEADING,
        VOWEL,
        TRAILING,
    };

    static class StateMachine {
        StringBuilder output = new StringBuilder();
        State curState = State.EMPTY;
        StringBuilder curPartial = new StringBuilder();

        static boolean checkIfCommittable(String composed, State state) {
            if (state == State.LEADING) {
                ConsInfo consInfo = consInfoMap.get(composed);
                if (consInfo != null && consInfo.leadingChar != null) {
                    return true;  // not done yet
                }
            }
            else if (state == State.VOWEL) {
                VowelInfo vowelInfo = vowelInfoMap.get(composed);
                if (vowelInfo != null && vowelInfo.vowelChar != null) {
                    return true;  // not done yet
                }
            }
            else if (state == State.TRAILING) {
                ConsInfo consInfo = consInfoMap.get(composed);
                if (consInfo != null && consInfo.trailingChar != null) {
                    return true;  // not done yet
                }
            }
            return false;
        }

        boolean commitPartialIfDone() {
            if (curPartial.length() <= 1 || curState == State.EMPTY) {
                return false;
            }

            String composed = composeMap.get(curPartial.toString());
            if (composed != null && checkIfCommittable(composed, curState)) {
                return false;
            }

            commitPartial();
            return true;
        }

        void commitPartial() {
            if (curPartial.length() == 0 || curState == State.EMPTY) {
                return;
            }

            for (int l = curPartial.length(); l > 0; --l) {
                String composed = curPartial.substring(0, l);
                if (l > 1) {
                    composed = composeMap.get(curPartial.substring(0, l));
                }
                if (composed != null && checkIfCommittable(composed, curState)) {
                    if (curState == State.LEADING) {
                        ConsInfo consInfo = consInfoMap.get(composed);
                        output.append(consInfo.leadingChar);
                        curState = State.VOWEL;
                    } else if (curState == State.VOWEL) {
                        VowelInfo vowelInfo = vowelInfoMap.get(composed);
                        output.append(vowelInfo.vowelChar);
                        curState = State.TRAILING;
                    } else if (curState == State.TRAILING) {
                        ConsInfo consInfo = consInfoMap.get(composed);
                        output.append(consInfo.trailingChar);
                        curState = State.LEADING;
                    }

                    curPartial.replace(0, l, "");
                    break;
                }
            }
        }

        String run(String composingText) {
            for (int index = 0; index < composingText.length(); ++index) {
                String ch = composingText.substring(index, index + 1);

                if (curState == State.EMPTY) {
                    if (consInfoMap.containsKey(ch)) {
                        curPartial.append(ch);
                        curState = State.LEADING;
                    }
                    else if (vowelInfoMap.containsKey(ch)) {
                        output.append("\u115f"); // hangul choseong filler
                        curPartial.append(ch);
                        curState = State.VOWEL;
                    }
                    commitPartialIfDone();
                }
                else if (curState == State.LEADING) {
                    if (consInfoMap.containsKey(ch)) {
                        curPartial.append(ch);
                    }
                    else if (vowelInfoMap.containsKey(ch)) {
                        commitPartial();
                        curPartial.append(ch);
                        curState = State.VOWEL;
                    }
                    commitPartialIfDone();
                }
                else if (curState == State.VOWEL) {
                    if (vowelInfoMap.containsKey(ch)) {
                        curPartial.append(ch);
                    }
                    else if (consInfoMap.containsKey(ch)) {
                        commitPartial();
                        curPartial.append(ch);
                        curState = State.TRAILING;
                    }
                    commitPartialIfDone();
                }
                else if (curState == State.TRAILING) {
                    if (consInfoMap.containsKey(ch)) {
                        curPartial.append(ch);
                    }
                    else if (vowelInfoMap.containsKey(ch)) {
                        // commit existing partial minus the last
                        String lastChar = curPartial.substring(curPartial.length() - 1);
                        curPartial.setLength(curPartial.length() - 1);
                        commitPartial();

                        // convert last char into leading
                        curPartial.append(lastChar);
                        curState = State.LEADING;
                        commitPartial();

                        curPartial.append(ch);
                        curState = State.VOWEL;
                    }
                    commitPartialIfDone();
                }
            }

            commitPartial();

            return postProcess(output.toString() + curPartial.toString());
        }
    }

    private static String postProcess(String string) {
        // TODO: convert orphaned leading jamos into compat if applicable
        return Normalizer.normalize(string, Normalizer.Form.NFC);
    }

    public static String getDisplayComposingText(String composingText) {
        return new StateMachine().run(composingText);
    }

    public static String decomposeHangul(String text) {
        String decomposed = Normalizer.normalize(text, Normalizer.Form.NFD);
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < decomposed.length(); ++index) {
            String ch = decomposed.substring(index, index + 1);
            String compat = toCompat.get(ch);
            if (compat != null) {
                ch = compat;
            }
            result.append(ch);
        }
        return result.toString()
            .replace("ㅘ", "ㅗㅏ")
            .replace("ㅝ", "ㅜㅓ")
            .replace("ㅟ", "ㅜㅣ")
            .replace("ㅢ", "ㅡㅣ")
            .replace("ㅚ", "ㅗㅣ")
            .replace("ㅙ", "ㅗㅐ")
            .replace("ㅞ", "ㅜㅔ");
    }
}
