package it.alwe.acronym.game;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FiltriDoc extends DocumentFilter {
	Boolean cdn = false;
	
    public FiltriDoc(int i) {
    	if (i == 1) cdn = true;
    	else cdn = false;
	}
    
	@Override
    public void replace(FilterBypass fb, int i, int i1, String string, AttributeSet as) throws BadLocationException {
        for (int n = string.length(); n > 0; n--) {
            char c = string.charAt(n - 1);
            if (!cdn && (((int) c >= 48 && (int) c <= 57) ||  c == '.')) super.replace(fb, i, i1, String.valueOf(c), as);
            else if (cdn && ((int) c >= 48 && (int) c <= 57)) super.replace(fb, i, i1, String.valueOf(c), as);
        }
    }
	
    @Override
    public void remove(FilterBypass fb, int i, int i1) throws BadLocationException { super.remove(fb, i, i1); }
    
    @Override
    public void insertString(FilterBypass fb, int i, String string, AttributeSet as) throws BadLocationException { super.insertString(fb, i, string, as); }
}
