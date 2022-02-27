package com.webnobis.consumption.presentation.coverage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.webnobis.consumption.model.Medium;

/**
 * Medium panel
 * 
 * @author steffen
 *
 */
public class MediumPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTextField dialCount;

	/**
	 * Medium panel
	 * 
	 * @param medium      medium
	 * @param dialCount   dial count
	 * @param shouldStore should store
	 */
	public MediumPanel(Medium medium, String dialCount, ShouldStore shouldStore) {
		super(new BorderLayout(2, 0));
		Objects.requireNonNull(medium, "medium is null");
		this.dialCount = new JTextField(dialCount, JTextField.RIGHT);
		if (shouldStore == null) {
			this.dialCount.setEditable(false);
			this.dialCount.setBackground(Color.LIGHT_GRAY);
		} else {
			this.dialCount.getDocument().addDocumentListener(new ChangeListener(shouldStore));
		}
		JLabel label = new JLabel(medium.name());
		label.setPreferredSize(new Dimension(70, label.getPreferredSize().height));
		this.add(label, BorderLayout.WEST);
		this.add(this.dialCount, BorderLayout.CENTER);
		label = new JLabel(medium.getUnit());
		label.setPreferredSize(new Dimension(50, label.getPreferredSize().height));
		this.add(label, BorderLayout.EAST);
	}

	/**
	 * Dial count
	 * 
	 * @return dial count
	 */
	public String getDialCount() {
		return dialCount.getText();
	}

	private class ChangeListener implements DocumentListener {

		private final ShouldStore shouldStore;

		private ChangeListener(ShouldStore shouldStore) {
			this.shouldStore = shouldStore;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			shouldStore.shouldStore();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			shouldStore.shouldStore();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			shouldStore.shouldStore();
		}

	}

}
