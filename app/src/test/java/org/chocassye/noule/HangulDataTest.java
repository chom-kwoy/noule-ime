package org.chocassye.noule;

import org.junit.Test;

import static org.junit.Assert.*;

public class HangulDataTest {
    @Test
    public void hangulCompose() {
        assertEquals(
            "가",
            HangulData.getDisplayComposingText("ㄱㅏ")
        );
        assertEquals(
            "간",
            HangulData.getDisplayComposingText("ㄱㅏㄴ")
        );
        assertEquals(
            "가나",
            HangulData.getDisplayComposingText("ㄱㅏㄴㅏ")
        );
        assertEquals(
            "간다",
            HangulData.getDisplayComposingText("ㄱㅏㄴㄷㅏ")
        );
        assertEquals(
            "간담",
            HangulData.getDisplayComposingText("ㄱㅏㄴㄷㅏㅁ")
        );
        assertEquals(
            "ᄋᆢ",
            HangulData.getDisplayComposingText("ㅇㆍㆍ")
        );
        assertEquals(
            "ᄋᆢᆨ",
            HangulData.getDisplayComposingText("ㅇㆍㆍㄱ")
        );
    }
    @Test
    public void hangulComposeUnfinished() {
        assertEquals(
            "담ㅍ",
            HangulData.getDisplayComposingText("ㄷㅏㅁㅍ")
        );
    }
    @Test
    public void hangulComposeIncomplete() {
        assertEquals(
            "닾ㅍ파",
            HangulData.getDisplayComposingText("ㄷㅏㅍㅍㅍㅏ")
        );
        assertEquals(
            "닾ㅍㅋ파",
            HangulData.getDisplayComposingText("ㄷㅏㅍㅍㅋㅍㅏ")
        );
    }
    @Test
    public void hangulComposeIncomplete2() {
        assertEquals(
            "ㅣ아",
            HangulData.getDisplayComposingText("ㅣㅇㅏ")
        );
    }
    @Test
    public void hangulComposeIncomplete3() {
        assertEquals(
            "아ㅏㅓㅓ",
            HangulData.getDisplayComposingText("ㅇㅏㅏㅓㅓ")
        );
    }
    @Test
    public void hangulComposeIncomplete4() {
        assertEquals(
            "ㅓ아",
            HangulData.getDisplayComposingText("ㅓㅇㅏ")
        );
    }
    @Test
    public void hangulComposeIncomplete5() {
        assertEquals(
            "서ㅓ아",
            HangulData.getDisplayComposingText("ㅅㅓㅓㅇㅏ")
        );
    }
}
