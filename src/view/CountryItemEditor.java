package view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

/**
 * Editor for JComboBox
 * @author wwww.codejava.net
 *
 */
public class CountryItemEditor extends BasicComboBoxEditor {
	private JPanel panel = new JPanel();
	private JLabel labelItem = new JLabel();
	private String selectedValue;
	
	public CountryItemEditor() {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(2, 5, 2, 2);
		
		labelItem.setOpaque(false);
		labelItem.setHorizontalAlignment(JLabel.LEFT);
		labelItem.setForeground(Color.GRAY);
		
		panel.add(labelItem, constraints);
		panel.setBackground(Color.WHITE);
	}
	
	public Component getEditorComponent() {
		return this.panel;
	}
	
	public Object getItem() {
		return this.selectedValue;
	}
	
	public void setItem(Object item) {
		if (item == null) {
			return;
		}
		String[] countryItem = (String[]) item;
		selectedValue = countryItem[0];
		labelItem.setText(selectedValue);
		ImageIcon icon = new ImageIcon(getClass().getResource(countryItem[1]));
//		labelItem.setIcon(new ImageIcon(countryItem[1]));
		labelItem.setIcon(icon);
	}
}