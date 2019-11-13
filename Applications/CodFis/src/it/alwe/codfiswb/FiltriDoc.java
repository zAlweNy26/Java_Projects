package it.alwe.codfiswb;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FiltriDoc extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int i, int i1, String string, AttributeSet as) throws BadLocationException {
        for (int n = string.length(); n > 0; n--) {
            char c = string.charAt(n - 1);
            if (Character.isAlphabetic(c) || c == ' ' || c == '\'') super.replace(fb, i, i1, String.valueOf(c), as);
        }
    }
    @Override
    public void remove(FilterBypass fb, int i, int i1) throws BadLocationException { super.remove(fb, i, i1); }
    @Override
    public void insertString(FilterBypass fb, int i, String string, AttributeSet as) throws BadLocationException { super.insertString(fb, i, string, as); }
}
