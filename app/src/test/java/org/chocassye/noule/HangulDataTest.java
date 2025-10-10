package org.chocassye.noule;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.Normalizer;

public class HangulDataTest {
    @Test
    public void hangulCompose() {
        assertEquals(
            Normalizer.normalize("가", Normalizer.Form.NFD),
            HangulData.getDisplayComposingText("ㄱㅏ")
        );
        assertEquals(
            Normalizer.normalize("간", Normalizer.Form.NFD),
            HangulData.getDisplayComposingText("ㄱㅏㄴ")
        );
        assertEquals(
            Normalizer.normalize("가나", Normalizer.Form.NFD),
            HangulData.getDisplayComposingText("ㄱㅏㄴㅏ")
        );
        assertEquals(
            Normalizer.normalize("간다", Normalizer.Form.NFD),
            HangulData.getDisplayComposingText("ㄱㅏㄴㄷㅏ")
        );
        assertEquals(
            Normalizer.normalize("간담", Normalizer.Form.NFD),
            HangulData.getDisplayComposingText("ㄱㅏㄴㄷㅏㅁ")
        );
        assertEquals(
            Normalizer.normalize("담ㅎ", Normalizer.Form.NFD),
            HangulData.getDisplayComposingText("ㄷㅏㅁㅎ")
        );
    }
}
