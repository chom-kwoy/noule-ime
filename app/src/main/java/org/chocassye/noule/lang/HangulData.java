package org.chocassye.noule.lang;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final HashSet<String> hangulSet = new HashSet<>();
    public static final HashSet<String> leadingJamos = new HashSet<>();
    public static final HashSet<String> vowelJamos = new HashSet<>();
    public static final HashSet<String> trailingJamos = new HashSet<>();

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
        consInfoMap.put("ᄓ", new ConsInfo("ᄓ", "ᄓ", "ᇅ"));
        consInfoMap.put("ᄖ", new ConsInfo("ᄖ", "ᄖ", null));
        consInfoMap.put("ᄗ", new ConsInfo("ᄗ", "ᄗ", "ᇊ"));
        consInfoMap.put("ᄘ", new ConsInfo("ᄘ", "ᄘ", "ᇍ"));
        consInfoMap.put("ᄙ", new ConsInfo("ᄙ", "ᄙ", "ᇐ"));
        consInfoMap.put("ᄛ", new ConsInfo("ᄛ", "ᄛ", "ퟝ"));
        consInfoMap.put("ᄟ", new ConsInfo("ᄟ", "ᄟ", null));
        consInfoMap.put("ᄤ", new ConsInfo("ᄤ", "ᄤ", null));
        consInfoMap.put("ᄥ", new ConsInfo("ᄥ", "ᄥ", null));
        consInfoMap.put("ᄦ", new ConsInfo("ᄦ", "ᄦ", null));
        consInfoMap.put("ᄨ", new ConsInfo("ᄨ", "ᄨ", "ퟩ"));
        consInfoMap.put("ᄪ", new ConsInfo("ᄪ", "ᄪ", "ᇤ"));
        consInfoMap.put("ᄰ", new ConsInfo("ᄰ", "ᄰ", "ᇩ"));
        consInfoMap.put("ᄱ", new ConsInfo("ᄱ", "ᄱ", "ퟪ"));
        consInfoMap.put("ᄳ", new ConsInfo("ᄳ", "ᄳ", null));
        consInfoMap.put("ᄴ", new ConsInfo("ᄴ", "ᄴ", null));
        consInfoMap.put("ᄵ", new ConsInfo("ᄵ", "ᄵ", null));
        consInfoMap.put("ᄷ", new ConsInfo("ᄷ", "ᄷ", "ퟰ"));
        consInfoMap.put("ᄸ", new ConsInfo("ᄸ", "ᄸ", null));
        consInfoMap.put("ᄹ", new ConsInfo("ᄹ", "ᄹ", "ퟱ"));
        consInfoMap.put("ᄺ", new ConsInfo("ᄺ", "ᄺ", null));
        consInfoMap.put("ᄻ", new ConsInfo("ᄻ", "ᄻ", "ퟲ"));
        consInfoMap.put("ᄼ", new ConsInfo("ᄼ", "ᄼ", null));
        consInfoMap.put("ᄽ", new ConsInfo("ᄽ", "ᄽ", null));
        consInfoMap.put("ᄾ", new ConsInfo("ᄾ", "ᄾ", null));
        consInfoMap.put("ᄿ", new ConsInfo("ᄿ", "ᄿ", null));
        consInfoMap.put("ᅁ", new ConsInfo("ᅁ", "ᅁ", "ᇬ"));
        consInfoMap.put("ᅂ", new ConsInfo("ᅂ", "ᅂ", null));
        consInfoMap.put("ᅃ", new ConsInfo("ᅃ", "ᅃ", null));
        consInfoMap.put("ᅄ", new ConsInfo("ᅄ", "ᅄ", null));
        consInfoMap.put("ᅅ", new ConsInfo("ᅅ", "ᅅ", null));
        consInfoMap.put("ᅆ", new ConsInfo("ᅆ", "ᅆ", null));
        consInfoMap.put("ᅈ", new ConsInfo("ᅈ", "ᅈ", null));
        consInfoMap.put("ᅉ", new ConsInfo("ᅉ", "ᅉ", null));
        consInfoMap.put("ᅊ", new ConsInfo("ᅊ", "ᅊ", null));
        consInfoMap.put("ᅋ", new ConsInfo("ᅋ", "ᅋ", null));
        consInfoMap.put("ᅍ", new ConsInfo("ᅍ", "ᅍ", null));
        consInfoMap.put("ᅎ", new ConsInfo("ᅎ", "ᅎ", null));
        consInfoMap.put("ᅏ", new ConsInfo("ᅏ", "ᅏ", null));
        consInfoMap.put("ᅐ", new ConsInfo("ᅐ", "ᅐ", null));
        consInfoMap.put("ᅑ", new ConsInfo("ᅑ", "ᅑ", null));
        consInfoMap.put("ᅒ", new ConsInfo("ᅒ", "ᅒ", null));
        consInfoMap.put("ᅓ", new ConsInfo("ᅓ", "ᅓ", null));
        consInfoMap.put("ᅔ", new ConsInfo("ᅔ", "ᅔ", null));
        consInfoMap.put("ᅕ", new ConsInfo("ᅕ", "ᅕ", null));
        consInfoMap.put("ᅖ", new ConsInfo("ᅖ", "ᅖ", "ᇳ"));
        consInfoMap.put("ᅚ", new ConsInfo("ᅚ", "ᅚ", null));
        consInfoMap.put("ᅞ", new ConsInfo("ᅞ", "ᅞ", "ᇋ"));
        consInfoMap.put("ᅟ", new ConsInfo("ᅟ", "ᅟ", null));
        consInfoMap.put("ᇃ", new ConsInfo("ᇃ", null, "ᇃ"));
        consInfoMap.put("ᇄ", new ConsInfo("ᇄ", null, "ᇄ"));
        consInfoMap.put("ᇉ", new ConsInfo("ᇉ", null, "ᇉ"));
        consInfoMap.put("ᇏ", new ConsInfo("ᇏ", null, "ᇏ"));
        consInfoMap.put("ᇑ", new ConsInfo("ᇑ", null, "ᇑ"));
        consInfoMap.put("ᇒ", new ConsInfo("ᇒ", null, "ᇒ"));
        consInfoMap.put("ᇔ", new ConsInfo("ᇔ", null, "ᇔ"));
        consInfoMap.put("ꥫ", new ConsInfo("ꥫ", "ꥫ", "ᇕ"));
        consInfoMap.put("ᇖ", new ConsInfo("ᇖ", null, "ᇖ"));
        consInfoMap.put("ꥮ", new ConsInfo("ꥮ", "ꥮ", "ᇘ"));
        consInfoMap.put("ꥯ", new ConsInfo("ꥯ", "ꥯ", "ᇚ"));
        consInfoMap.put("ᇛ", new ConsInfo("ᇛ", null, "ᇛ"));
        consInfoMap.put("ᇞ", new ConsInfo("ᇞ", null, "ᇞ"));
        consInfoMap.put("ᇠ", new ConsInfo("ᇠ", null, "ᇠ"));
        consInfoMap.put("ᇡ", new ConsInfo("ᇡ", null, "ᇡ"));
        consInfoMap.put("ᇣ", new ConsInfo("ᇣ", null, "ᇣ"));
        consInfoMap.put("ꥴ", new ConsInfo("ꥴ", "ꥴ", "ᇥ"));
        consInfoMap.put("ᇭ", new ConsInfo("ᇭ", null, "ᇭ"));
        consInfoMap.put("ᇯ", new ConsInfo("ᇯ", null, "ᇯ"));
        consInfoMap.put("ᇵ", new ConsInfo("ᇵ", null, "ᇵ"));
        consInfoMap.put("ᇶ", new ConsInfo("ᇶ", null, "ᇶ"));
        consInfoMap.put("ᇷ", new ConsInfo("ᇷ", null, "ᇷ"));
        consInfoMap.put("ᇸ", new ConsInfo("ᇸ", null, "ᇸ"));
        consInfoMap.put("ᇺ", new ConsInfo("ᇺ", null, "ᇺ"));
        consInfoMap.put("ᇻ", new ConsInfo("ᇻ", null, "ᇻ"));
        consInfoMap.put("ᇼ", new ConsInfo("ᇼ", null, "ᇼ"));
        consInfoMap.put("ᇽ", new ConsInfo("ᇽ", null, "ᇽ"));
        consInfoMap.put("ᇾ", new ConsInfo("ᇾ", null, "ᇾ"));
        consInfoMap.put("ꥠ", new ConsInfo("ꥠ", "ꥠ", null));
        consInfoMap.put("ꥡ", new ConsInfo("ꥡ", "ꥡ", "ퟏ"));
        consInfoMap.put("ꥢ", new ConsInfo("ꥢ", "ꥢ", "ퟐ"));
        consInfoMap.put("ꥣ", new ConsInfo("ꥣ", "ꥣ", "ퟒ"));
        consInfoMap.put("ꥥ", new ConsInfo("ꥥ", "ꥥ", "ퟕ"));
        consInfoMap.put("ꥧ", new ConsInfo("ꥧ", "ꥧ", null));
        consInfoMap.put("ꥪ", new ConsInfo("ꥪ", "ꥪ", null));
        consInfoMap.put("ꥭ", new ConsInfo("ꥭ", "ꥭ", null));
        consInfoMap.put("ꥰ", new ConsInfo("ꥰ", "ꥰ", null));
        consInfoMap.put("ꥲ", new ConsInfo("ꥲ", "ꥲ", null));
        consInfoMap.put("ꥳ", new ConsInfo("ꥳ", "ꥳ", null));
        consInfoMap.put("ꥵ", new ConsInfo("ꥵ", "ꥵ", null));
        consInfoMap.put("ꥶ", new ConsInfo("ꥶ", "ꥶ", null));
        consInfoMap.put("ꥷ", new ConsInfo("ꥷ", "ꥷ", null));
        consInfoMap.put("ꥸ", new ConsInfo("ꥸ", "ꥸ", null));
        consInfoMap.put("ꥹ", new ConsInfo("ꥹ", "ꥹ", null));
        consInfoMap.put("ꥺ", new ConsInfo("ꥺ", "ꥺ", null));
        consInfoMap.put("ꥻ", new ConsInfo("ꥻ", "ꥻ", null));
        consInfoMap.put("ꥼ", new ConsInfo("ꥼ", "ꥼ", null));
        consInfoMap.put("ퟋ", new ConsInfo("ퟋ", null, "ퟋ"));
        consInfoMap.put("ퟌ", new ConsInfo("ퟌ", null, "ퟌ"));
        consInfoMap.put("ퟎ", new ConsInfo("ퟎ", null, "ퟎ"));
        consInfoMap.put("ퟑ", new ConsInfo("ퟑ", null, "ퟑ"));
        consInfoMap.put("ퟓ", new ConsInfo("ퟓ", null, "ퟓ"));
        consInfoMap.put("ퟔ", new ConsInfo("ퟔ", null, "ퟔ"));
        consInfoMap.put("ퟖ", new ConsInfo("ퟖ", null, "ퟖ"));
        consInfoMap.put("ퟗ", new ConsInfo("ퟗ", null, "ퟗ"));
        consInfoMap.put("ퟘ", new ConsInfo("ퟘ", null, "ퟘ"));
        consInfoMap.put("ퟙ", new ConsInfo("ퟙ", null, "ퟙ"));
        consInfoMap.put("ퟚ", new ConsInfo("ퟚ", null, "ퟚ"));
        consInfoMap.put("ퟛ", new ConsInfo("ퟛ", null, "ퟛ"));
        consInfoMap.put("ퟜ", new ConsInfo("ퟜ", null, "ퟜ"));
        consInfoMap.put("ퟞ", new ConsInfo("ퟞ", null, "ퟞ"));
        consInfoMap.put("ퟟ", new ConsInfo("ퟟ", null, "ퟟ"));
        consInfoMap.put("ퟠ", new ConsInfo("ퟠ", null, "ퟠ"));
        consInfoMap.put("ퟡ", new ConsInfo("ퟡ", null, "ퟡ"));
        consInfoMap.put("ퟢ", new ConsInfo("ퟢ", null, "ퟢ"));
        consInfoMap.put("ퟤ", new ConsInfo("ퟤ", null, "ퟤ"));
        consInfoMap.put("ퟥ", new ConsInfo("ퟥ", null, "ퟥ"));
        consInfoMap.put("ퟫ", new ConsInfo("ퟫ", null, "ퟫ"));
        consInfoMap.put("ퟬ", new ConsInfo("ퟬ", null, "ퟬ"));
        consInfoMap.put("ퟭ", new ConsInfo("ퟭ", null, "ퟭ"));
        consInfoMap.put("ퟮ", new ConsInfo("ퟮ", null, "ퟮ"));
        consInfoMap.put("ퟳ", new ConsInfo("ퟳ", null, "ퟳ"));
        consInfoMap.put("ퟴ", new ConsInfo("ퟴ", null, "ퟴ"));
        consInfoMap.put("ퟵ", new ConsInfo("ퟵ", null, "ퟵ"));
        consInfoMap.put("ퟶ", new ConsInfo("ퟶ", null, "ퟶ"));
        consInfoMap.put("ퟷ", new ConsInfo("ퟷ", null, "ퟷ"));
        consInfoMap.put("ퟸ", new ConsInfo("ퟸ", null, "ퟸ"));
        consInfoMap.put("ퟺ", new ConsInfo("ퟺ", null, "ퟺ"));
        consInfoMap.put("ퟻ", new ConsInfo("ퟻ", null, "ퟻ"));

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
        vowelInfoMap.put("ᅶ", new VowelInfo("ᅶ", "ᅶ"));
        vowelInfoMap.put("ᅷ", new VowelInfo("ᅷ", "ᅷ"));
        vowelInfoMap.put("ᅸ", new VowelInfo("ᅸ", "ᅸ"));
        vowelInfoMap.put("ᅹ", new VowelInfo("ᅹ", "ᅹ"));
        vowelInfoMap.put("ᅺ", new VowelInfo("ᅺ", "ᅺ"));
        vowelInfoMap.put("ᅻ", new VowelInfo("ᅻ", "ᅻ"));
        vowelInfoMap.put("ᅼ", new VowelInfo("ᅼ", "ᅼ"));
        vowelInfoMap.put("ᅽ", new VowelInfo("ᅽ", "ᅽ"));
        vowelInfoMap.put("ᅾ", new VowelInfo("ᅾ", "ᅾ"));
        vowelInfoMap.put("ᅿ", new VowelInfo("ᅿ", "ᅿ"));
        vowelInfoMap.put("ᆀ", new VowelInfo("ᆀ", "ᆀ"));
        vowelInfoMap.put("ᆁ", new VowelInfo("ᆁ", "ᆁ"));
        vowelInfoMap.put("ᆂ", new VowelInfo("ᆂ", "ᆂ"));
        vowelInfoMap.put("ᆃ", new VowelInfo("ᆃ", "ᆃ"));
        vowelInfoMap.put("ᆆ", new VowelInfo("ᆆ", "ᆆ"));
        vowelInfoMap.put("ᆇ", new VowelInfo("ᆇ", "ᆇ"));
        vowelInfoMap.put("ᆉ", new VowelInfo("ᆉ", "ᆉ"));
        vowelInfoMap.put("ᆊ", new VowelInfo("ᆊ", "ᆊ"));
        vowelInfoMap.put("ᆋ", new VowelInfo("ᆋ", "ᆋ"));
        vowelInfoMap.put("ᆌ", new VowelInfo("ᆌ", "ᆌ"));
        vowelInfoMap.put("ᆍ", new VowelInfo("ᆍ", "ᆍ"));
        vowelInfoMap.put("ᆎ", new VowelInfo("ᆎ", "ᆎ"));
        vowelInfoMap.put("ᆏ", new VowelInfo("ᆏ", "ᆏ"));
        vowelInfoMap.put("ᆐ", new VowelInfo("ᆐ", "ᆐ"));
        vowelInfoMap.put("ᆓ", new VowelInfo("ᆓ", "ᆓ"));
        vowelInfoMap.put("ᆕ", new VowelInfo("ᆕ", "ᆕ"));
        vowelInfoMap.put("ᆖ", new VowelInfo("ᆖ", "ᆖ"));
        vowelInfoMap.put("ᆗ", new VowelInfo("ᆗ", "ᆗ"));
        vowelInfoMap.put("ᆘ", new VowelInfo("ᆘ", "ᆘ"));
        vowelInfoMap.put("ᆙ", new VowelInfo("ᆙ", "ᆙ"));
        vowelInfoMap.put("ᆚ", new VowelInfo("ᆚ", "ᆚ"));
        vowelInfoMap.put("ᆛ", new VowelInfo("ᆛ", "ᆛ"));
        vowelInfoMap.put("ᆜ", new VowelInfo("ᆜ", "ᆜ"));
        vowelInfoMap.put("ᆝ", new VowelInfo("ᆝ", "ᆝ"));
        vowelInfoMap.put("ᆟ", new VowelInfo("ᆟ", "ᆟ"));
        vowelInfoMap.put("ᆠ", new VowelInfo("ᆠ", "ᆠ"));
        vowelInfoMap.put("ᆢ", new VowelInfo("ᆢ", "ᆢ"));
        vowelInfoMap.put("ᆣ", new VowelInfo("ᆣ", "ᆣ"));
        vowelInfoMap.put("ᆤ", new VowelInfo("ᆤ", "ᆤ"));
        vowelInfoMap.put("ᆥ", new VowelInfo("ᆥ", "ᆥ"));
        vowelInfoMap.put("ᆦ", new VowelInfo("ᆦ", "ᆦ"));
        vowelInfoMap.put("ᆧ", new VowelInfo("ᆧ", "ᆧ"));
        vowelInfoMap.put("ힰ", new VowelInfo("ힰ", "ힰ"));
        vowelInfoMap.put("ힱ", new VowelInfo("ힱ", "ힱ"));
        vowelInfoMap.put("ힲ", new VowelInfo("ힲ", "ힲ"));
        vowelInfoMap.put("ힳ", new VowelInfo("ힳ", "ힳ"));
        vowelInfoMap.put("ힴ", new VowelInfo("ힴ", "ힴ"));
        vowelInfoMap.put("ힵ", new VowelInfo("ힵ", "ힵ"));
        vowelInfoMap.put("ힶ", new VowelInfo("ힶ", "ힶ"));
        vowelInfoMap.put("ힷ", new VowelInfo("ힷ", "ힷ"));
        vowelInfoMap.put("ힸ", new VowelInfo("ힸ", "ힸ"));
        vowelInfoMap.put("ힹ", new VowelInfo("ힹ", "ힹ"));
        vowelInfoMap.put("ힺ", new VowelInfo("ힺ", "ힺ"));
        vowelInfoMap.put("ힻ", new VowelInfo("ힻ", "ힻ"));
        vowelInfoMap.put("ힼ", new VowelInfo("ힼ", "ힼ"));
        vowelInfoMap.put("ힽ", new VowelInfo("ힽ", "ힽ"));
        vowelInfoMap.put("ힾ", new VowelInfo("ힾ", "ힾ"));
        vowelInfoMap.put("ힿ", new VowelInfo("ힿ", "ힿ"));
        vowelInfoMap.put("ퟀ", new VowelInfo("ퟀ", "ퟀ"));
        vowelInfoMap.put("ퟁ", new VowelInfo("ퟁ", "ퟁ"));
        vowelInfoMap.put("ퟂ", new VowelInfo("ퟂ", "ퟂ"));
        vowelInfoMap.put("ퟃ", new VowelInfo("ퟃ", "ퟃ"));
        vowelInfoMap.put("ퟄ", new VowelInfo("ퟄ", "ퟄ"));
        vowelInfoMap.put("ퟅ", new VowelInfo("ퟅ", "ퟅ"));
        vowelInfoMap.put("ퟆ", new VowelInfo("ퟆ", "ퟆ"));

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
        composeMap.put("ㄴㄱ", "ᄓ");
        composeMap.put("ㄴㅂ", "ᄖ");
        composeMap.put("ㄷㄱ", "ᄗ");
        composeMap.put("ㄹㄴ", "ᄘ");
        composeMap.put("ㄹㄹ", "ᄙ");
        composeMap.put("ㄹㅇ", "ᄛ");
        composeMap.put("ㅂㄴ", "ᄟ");
        composeMap.put("ㅂㅅㅂ", "ᄤ");
        composeMap.put("ㅂㅅㅅ", "ᄥ");
        composeMap.put("ㅂㅅㅈ", "ᄦ");
        composeMap.put("ㅂㅊ", "ᄨ");
        composeMap.put("ㅂㅍ", "ᄪ");
        composeMap.put("ㅅㄹ", "ᄰ");
        composeMap.put("ㅅㅁ", "ᄱ");
        composeMap.put("ㅅㅂㄱ", "ᄳ");
        composeMap.put("ㅅㅅㅅ", "ᄴ");
        composeMap.put("ㅅㅇ", "ᄵ");
        composeMap.put("ㅅㅊ", "ᄷ");
        composeMap.put("ㅅㅋ", "ᄸ");
        composeMap.put("ㅅㅌ", "ᄹ");
        composeMap.put("ㅅㅍ", "ᄺ");
        composeMap.put("ㅅㅎ", "ᄻ");
        composeMap.put("ᄼᄼ", "ᄽ");
        composeMap.put("ᄾᄾ", "ᄿ");
        composeMap.put("ㅇㄱ", "ᅁ");
        composeMap.put("ㅇㄷ", "ᅂ");
        composeMap.put("ㅇㅁ", "ᅃ");
        composeMap.put("ㅇㅂ", "ᅄ");
        composeMap.put("ㅇㅅ", "ᅅ");
        composeMap.put("ㅇㅿ", "ᅆ");
        composeMap.put("ㅇㅈ", "ᅈ");
        composeMap.put("ㅇㅊ", "ᅉ");
        composeMap.put("ㅇㅌ", "ᅊ");
        composeMap.put("ㅇㅍ", "ᅋ");
        composeMap.put("ㅈㅇ", "ᅍ");
        composeMap.put("ᅎᅎ", "ᅏ");
        composeMap.put("ㅊㅋ", "ᅒ");
        composeMap.put("ㅊㅎ", "ᅓ");
        composeMap.put("ㅍㅂ", "ᅖ");
        composeMap.put("ㄱㄷ", "ᅚ");
        composeMap.put("ㄷㄹ", "ᅞ");
        composeMap.put("ㅏㅗ", "ᅶ");
        composeMap.put("ㅏㅜ", "ᅷ");
        composeMap.put("ㅑㅗ", "ᅸ");
        composeMap.put("ㅑㅛ", "ᅹ");
        composeMap.put("ㅓㅗ", "ᅺ");
        composeMap.put("ㅓㅜ", "ᅻ");
        composeMap.put("ㅓㅡ", "ᅼ");
        composeMap.put("ㅕㅗ", "ᅽ");
        composeMap.put("ㅕㅜ", "ᅾ");
        composeMap.put("ㅗㅓ", "ᅿ");
        composeMap.put("ㅗㅔ", "ᆀ");
        composeMap.put("ㅗㅖ", "ᆁ");
        composeMap.put("ㅗㅗ", "ᆂ");
        composeMap.put("ㅗㅜ", "ᆃ");
        composeMap.put("ㅛㅕ", "ᆆ");
        composeMap.put("ㅛㅗ", "ᆇ");
        composeMap.put("ㅜㅏ", "ᆉ");
        composeMap.put("ㅜㅐ", "ᆊ");
        composeMap.put("ㅜㅓㅡ", "ᆋ");
        composeMap.put("ㅜㅖ", "ᆌ");
        composeMap.put("ㅜㅜ", "ᆍ");
        composeMap.put("ㅠㅏ", "ᆎ");
        composeMap.put("ㅠㅓ", "ᆏ");
        composeMap.put("ㅠㅔ", "ᆐ");
        composeMap.put("ㅠㅜ", "ᆓ");
        composeMap.put("ㅡㅜ", "ᆕ");
        composeMap.put("ㅡㅡ", "ᆖ");
        composeMap.put("ㅡㅣㅜ", "ᆗ");
        composeMap.put("ㅣㅏ", "ᆘ");
        composeMap.put("ㅣㅑ", "ᆙ");
        composeMap.put("ㅣㅗ", "ᆚ");
        composeMap.put("ㅣㅜ", "ᆛ");
        composeMap.put("ㅣㅡ", "ᆜ");
        composeMap.put("ㅣㆍ", "ᆝ");
        composeMap.put("ㆍㅓ", "ᆟ");
        composeMap.put("ㆍㅜ", "ᆠ");
        composeMap.put("ㆍㆍ", "ᆢ");
        composeMap.put("ㅏㅡ", "ᆣ");
        composeMap.put("ㅑㅜ", "ᆤ");
        composeMap.put("ㅕㅑ", "ᆥ");
        composeMap.put("ㅗㅑ", "ᆦ");
        composeMap.put("ㅗㅒ", "ᆧ");
        composeMap.put("ㄱㄹ", "ᇃ");
        composeMap.put("ㄱㅅㄱ", "ᇄ");
        composeMap.put("ㄴㅌ", "ᇉ");
        composeMap.put("ㄹㄷㅎ", "ᇏ");
        composeMap.put("ㄹㅁㄱ", "ᇑ");
        composeMap.put("ㄹㅁㅅ", "ᇒ");
        composeMap.put("ㄹㅂㅎ", "ᇔ");
        composeMap.put("ㄹㅂㅇ", "ꥫ");
        composeMap.put("ㄹㅅㅅ", "ᇖ");
        composeMap.put("ㄹㅋ", "ꥮ");
        composeMap.put("ㅁㄱ", "ꥯ");
        composeMap.put("ㅁㄹ", "ᇛ");
        composeMap.put("ㅁㅅㅅ", "ᇞ");
        composeMap.put("ㅁㅊ", "ᇠ");
        composeMap.put("ㅁㅎ", "ᇡ");
        composeMap.put("ㅂㄹ", "ᇣ");
        composeMap.put("ㅂㅎ", "ꥴ");
        composeMap.put("ㅇㄱㄱ", "ᇭ");
        composeMap.put("ㅇㅋ", "ᇯ");
        composeMap.put("ㅎㄴ", "ᇵ");
        composeMap.put("ㅎㄹ", "ᇶ");
        composeMap.put("ㅎㅁ", "ᇷ");
        composeMap.put("ㅎㅂ", "ᇸ");
        composeMap.put("ㄱㄴ", "ᇺ");
        composeMap.put("ㄱㅂ", "ᇻ");
        composeMap.put("ㄱㅊ", "ᇼ");
        composeMap.put("ㄱㅋ", "ᇽ");
        composeMap.put("ㄱㅎ", "ᇾ");
        composeMap.put("ㄷㅁ", "ꥠ");
        composeMap.put("ㄷㅂ", "ꥡ");
        composeMap.put("ㄷㅅ", "ꥢ");
        composeMap.put("ㄷㅈ", "ꥣ");
        composeMap.put("ㄹㄱㄱ", "ꥥ");
        composeMap.put("ㄹㄷㄷ", "ꥧ");
        composeMap.put("ㄹㅂㅂ", "ꥪ");
        composeMap.put("ㄹㅈ", "ꥭ");
        composeMap.put("ㅁㄷ", "ꥰ");
        composeMap.put("ㅂㅅㅌ", "ꥲ");
        composeMap.put("ㅂㅋ", "ꥳ");
        composeMap.put("ㅅㅅㅂ", "ꥵ");
        composeMap.put("ㅇㄹ", "ꥶ");
        composeMap.put("ㅇㅎ", "ꥷ");
        composeMap.put("ㅈㅈㅎ", "ꥸ");
        composeMap.put("ㅌㅌ", "ꥹ");
        composeMap.put("ㅍㅎ", "ꥺ");
        composeMap.put("ㅎㅅ", "ꥻ");
        composeMap.put("ㆆㆆ", "ꥼ");
        composeMap.put("ㅗㅕ", "ힰ");
        composeMap.put("ㅗㅗㅣ", "ힱ");
        composeMap.put("ㅛㅏ", "ힲ");
        composeMap.put("ㅛㅐ", "ힳ");
        composeMap.put("ㅛㅓ", "ힴ");
        composeMap.put("ㅜㅕ", "ힵ");
        composeMap.put("ㅜㅣㅣ", "ힶ");
        composeMap.put("ㅠㅐ", "ힷ");
        composeMap.put("ㅠㅗ", "ힸ");
        composeMap.put("ㅡㅏ", "ힹ");
        composeMap.put("ㅡㅓ", "ힺ");
        composeMap.put("ㅡㅔ", "ힻ");
        composeMap.put("ㅡㅗ", "ힼ");
        composeMap.put("ㅣㅑㅗ", "ힽ");
        composeMap.put("ㅣㅒ", "ힾ");
        composeMap.put("ㅣㅕ", "ힿ");
        composeMap.put("ㅣㅖ", "ퟀ");
        composeMap.put("ㅣㅗㅣ", "ퟁ");
        composeMap.put("ㅣㅛ", "ퟂ");
        composeMap.put("ㅣㅠ", "ퟃ");
        composeMap.put("ㅣㅣ", "ퟄ");
        composeMap.put("ㆍㅏ", "ퟅ");
        composeMap.put("ㆍㅔ", "ퟆ");
        composeMap.put("ㄴㄹ", "ퟋ");
        composeMap.put("ㄴㅊ", "ퟌ");
        composeMap.put("ㄷㄷㅂ", "ퟎ");
        composeMap.put("ㄷㅅㄱ", "ퟑ");
        composeMap.put("ㄷㅊ", "ퟓ");
        composeMap.put("ㄷㅌ", "ퟔ");
        composeMap.put("ㄹㄱㅎ", "ퟖ");
        composeMap.put("ㄹㄹㅋ", "ퟗ");
        composeMap.put("ㄹㅁㅎ", "ퟘ");
        composeMap.put("ㄹㅂㄷ", "ퟙ");
        composeMap.put("ㄹㅂㅍ", "ퟚ");
        composeMap.put("ㄹㆁ", "ퟛ");
        composeMap.put("ㄹㆆㅎ", "ퟜ");
        composeMap.put("ㅁㄴ", "ퟞ");
        composeMap.put("ㅁㄴㄴ", "ퟟ");
        composeMap.put("ㅁㅁ", "ퟠ");
        composeMap.put("ㅁㅂㅅ", "ퟡ");
        composeMap.put("ㅁㅈ", "ퟢ");
        composeMap.put("ㅂㄹㅍ", "ퟤ");
        composeMap.put("ㅂㅁ", "ퟥ");
        composeMap.put("ㅅㅂㅇ", "ퟫ");
        composeMap.put("ㅅㅅㄱ", "ퟬ");
        composeMap.put("ㅅㅅㄷ", "ퟭ");
        composeMap.put("ㅅㅿ", "ퟮ");
        composeMap.put("ㅿㅂ", "ퟳ");
        composeMap.put("ㅿㅂㅇ", "ퟴ");
        composeMap.put("ㆁㅁ", "ퟵ");
        composeMap.put("ㆁㅎ", "ퟶ");
        composeMap.put("ㅈㅂ", "ퟷ");
        composeMap.put("ㅈㅂㅂ", "ퟸ");
        composeMap.put("ㅍㅅ", "ퟺ");
        composeMap.put("ㅍㅌ", "ퟻ");

        for (Map.Entry<String, ConsInfo> entry : consInfoMap.entrySet()) {
            hangulSet.add(entry.getKey());
            if (entry.getValue().leadingChar != null) {
                toCompat.put(entry.getValue().leadingChar, entry.getKey());
                leadingJamos.add(entry.getValue().leadingChar);
                hangulSet.add(entry.getValue().leadingChar);
            }
            if (entry.getValue().trailingChar != null) {
                toCompat.put(entry.getValue().trailingChar, entry.getKey());
                trailingJamos.add(entry.getValue().trailingChar);
                hangulSet.add(entry.getValue().trailingChar);
            }
        }
        for (Map.Entry<String, VowelInfo> entry : vowelInfoMap.entrySet()) {
            hangulSet.add(entry.getKey());
            if (entry.getValue().vowelChar != null) {
                toCompat.put(entry.getValue().vowelChar, entry.getKey());
                vowelJamos.add(entry.getValue().vowelChar);
                hangulSet.add(entry.getValue().vowelChar);
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

            return commitPartial();
        }

        boolean commitPartial() {
            if (curPartial.length() == 0 || curState == State.EMPTY) {
                return false;
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
                    return true;
                }
            }
            return false;
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

                        if (commitPartialIfDone() && curPartial.length() > 0) {
                            String compat = toCompat.get(output.substring(output.length() - 1));
                            output.setLength(output.length() - 1);
                            output.append(compat);
                            curState = State.LEADING;
                        }
                    }
                    else if (vowelInfoMap.containsKey(ch)) {
                        commitPartial();
                        curPartial.append(ch);
                        curState = State.VOWEL;
                        commitPartialIfDone();
                    }
                }
                else if (curState == State.VOWEL) {
                    if (vowelInfoMap.containsKey(ch)) {
                        curPartial.append(ch);
                        if (commitPartialIfDone() && curPartial.length() > 0) {
                            output.append("\u115f");
                            curState = State.VOWEL;
                        }
                    }
                    else if (consInfoMap.containsKey(ch)) {
                        commitPartial();
                        curPartial.append(ch);
                        curState = State.TRAILING;
                        commitPartialIfDone();
                    }
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

            if (output.length() > 0) {
                String lastOne = output.substring(output.length() - 1);
                if (leadingJamos.contains(lastOne)) {
                    output.setLength(output.length() - 1);
                    output.append(toCompat.get(lastOne));
                }
            }

            return postProcess(output.toString() + curPartial.toString());
        }
    }

    public interface MatchCallback {
        String replace(Matcher matcher);
    }

    public static String replaceWithCallback(String input, String regex, MatchCallback callback) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // Call the user-defined callback function
            String replacement = callback.replace(matcher);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String postProcess(String string) {
        string = Normalizer.normalize(string, Normalizer.Form.NFC);

        string = replaceWithCallback(string, "\u115f(.)(?=(.?))", (matcher -> {
            String vowel = matcher.group(1);
            String trailing = matcher.group(2);
            if (!trailingJamos.contains(trailing)) {
                return toCompat.get(vowel);
            }
            return matcher.group();
        }));

        return string;
    }

    public static boolean isHangulString(String someText) {
        for (int index = 0; index < someText.length(); ++index) {
            String ch = someText.substring(index, index + 1);
            if (!hangulSet.contains(ch)) {
                return false;
            }
        }
        return true;
    }

    public static String getDisplayComposingText(String composingText) {
        if (isHangulString(composingText)) {
            return new StateMachine().run(composingText);
        }
        return composingText;
    }
}
