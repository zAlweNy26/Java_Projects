package it.alwe.acronym.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Field;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicSpinnerUI;
 
public class JSpinnerLook extends BasicSpinnerUI {
	public static ComponentUI createUI(JComponent c) { return new JSpinnerLook(); }
	 
	@Override
	protected Component createNextButton() {
	    Component c = createArrowButton(SwingConstants.NORTH);
	    c.setName("Spinner.nextButton");
	    Field field;
		try {
			field = BasicArrowButton.class.getDeclaredField("darkShadow");
			field.setAccessible(true);
			field.set(c, new Color(41, 41, 41));
			field.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) { e1.printStackTrace(); }
	    installNextButtonListeners(c);
	    return c;
	}
	 
	@Override
	protected Component createPreviousButton() {
	    Component c = createArrowButton(SwingConstants.SOUTH);
	    c.setName("Spinner.previousButton");
	    Field field;
	    try {
			field = BasicArrowButton.class.getDeclaredField("darkShadow");
			field.setAccessible(true);
			field.set(c, new Color(41, 41, 41));
			field.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) { e1.printStackTrace(); }
	    installPreviousButtonListeners(c);
	    return c;
	}
	
	private Component createArrowButton(int direction) {
	    JButton b = new BasicArrowButton(direction);
	    b.setBorder(BorderFactory.createLineBorder(new Color(0x2dce98)));
	    b.setInheritsPopupMenu(false);
	    b.setBackground(new Color(0x2dce98));
	    b.setForeground(new Color(41, 41, 41));
	    return b;
	}
	 
	@Override
	public void installUI(JComponent c) {
	    super.installUI(c);
	    c.removeAll();
	    c.setLayout(new BorderLayout());
	    c.add(createNextButton(), BorderLayout.WEST);
	    c.add(createPreviousButton(), BorderLayout.EAST);
	    c.add(createEditor(), BorderLayout.CENTER);
	}
}