package com.networking.semesterProject;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ErrorDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final JLabel lblError = new JLabel();
	
	public void showDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
	    setVisible(true);
	}
	/**
	 * Create the dialog.
	 */
	public ErrorDialog(String errorString) {
		setTitle("Error");
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		//lblError = new JLabel("Error");
		lblError.setText(errorString);
		lblError.setBounds(12, 13, 408, 42);
		contentPanel.add(lblError);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ErrorDialog.this.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
