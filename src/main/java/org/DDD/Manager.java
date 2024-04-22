package org.DDD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Manager extends JFrame implements ActionListener {
    public Manager() {
        setTitle("DDD Management Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);  // Center the window on the screen
        getContentPane().setBackground(new Color(18, 18, 18));

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // GridLayout for even spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.setBackground(new Color(18, 18, 18));

        // Creating and styling buttons
        JButton valueObjectsButton = createStyledButton("Value Object");
        JButton entitiesButton = createStyledButton("Entities");
        JButton domainPrimitivesButton = createStyledButton("Domain Primitives");

        // Add buttons to the panel
        buttonPanel.add(valueObjectsButton);
        buttonPanel.add(entitiesButton);
        buttonPanel.add(domainPrimitivesButton);

        // Register buttons with action listener
        valueObjectsButton.addActionListener(this);
        entitiesButton.addActionListener(this);
        domainPrimitivesButton.addActionListener(this);

        // Add the button panel to the content pane
        getContentPane().add(buttonPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(34, 34, 34));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Custom hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 34, 34));
            }
        });
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        dispose(); // Close the current frame
        switch (command) {
            case "Value Object":
                new ValueObjectGenerator().setVisible(true);
                break;
            case "Entities":
                new EntityGenerator().setVisible(true);
                break;
            case "Domain Primitives":
                new DomainPrimitiveGenerator().setVisible(true);
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Manager().setVisible(true));
    }
}
