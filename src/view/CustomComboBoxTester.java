package view;

import javax.swing.*;
import java.awt.*;

public class CustomComboBoxTester extends JFrame {
	
	public CustomComboBoxTester() {
		super("Demo program for custom combobox");
		setLayout(new FlowLayout());
		
		CountryComboBox customCombobox = new CountryComboBox();
		customCombobox.setPreferredSize(new Dimension(120, 30));
		customCombobox.setEditable(true);
		customCombobox.addItems(countryList);
		
		add(customCombobox);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 100);
		setLocationRelativeTo(null);	// center on screen
	}
	


	private String[][] countryList = {{"USA", "/icon/us.png"},
									  {"India", "/icon/in.png"},
									  {"Vietnam", "/icon/vn.png"},
									  {"Germany", "/icon/de.png"},
									  {"Canada", "/icon/ca.png"},
									  {"Japan", "/icon/jp.png"},
									  {"Great Britain", "/color/gb.png"},
									  {"Great Britain", "/icon/icon.png"},
									  {"Great ", "icon/icon.png"},
									  {"France", "icon/fr.png"}};

	public void CreateCustome(){
		new CustomComboBoxTester().setVisible(true);
	}

}