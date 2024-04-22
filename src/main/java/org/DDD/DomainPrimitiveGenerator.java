package org.DDD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList; // Ensure this import is present for list handling
import java.util.List;

public class DomainPrimitiveGenerator extends JFrame {
    private final JTextField classNameField;
    private final JTextArea variablesArea;
    private final JTextArea generatedCodeArea;
    private final JCheckBox finalCheckBox;

    public DomainPrimitiveGenerator() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Domain Primitive Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(45, 45, 45));
        setContentPane(contentPane);

        Font customFont = new Font("SansSerif", Font.PLAIN, 12);
        Color backgroundColor = new Color(60, 63, 65);
        Color inputBackgroundColor = new Color(230, 230, 230);
        Color textColor = new Color(230, 230, 230);
        Color accentColor = new Color(13, 1, 72);
        Color checkboxTextColor = Color.BLACK;


        finalCheckBox = new JCheckBox("Add final keyword");

        classNameField = new JTextField(20);
        variablesArea = new JTextArea(5, 20);
        generatedCodeArea = new JTextArea(5, 20);
        generatedCodeArea.setEditable(false);

        customizeCheckbox(finalCheckBox, checkboxTextColor, backgroundColor, customFont);

        customizeInputComponent(classNameField, Color.BLACK, inputBackgroundColor, customFont);
        customizeInputComponent(variablesArea, Color.BLACK, inputBackgroundColor, customFont);
        customizeComponent(generatedCodeArea, textColor, backgroundColor, customFont);

        contentPane.add(createCheckboxPanel(), BorderLayout.NORTH);
        contentPane.add(createInputPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPanel(accentColor, textColor, customFont), BorderLayout.SOUTH);
        // Adjusted to put GeneratedCodePanel in a more appropriate location
        contentPane.add(createGeneratedCodePanel(), BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Methods for customization and panel creation remain unchanged

    private void customizeComponent(JComponent component, Color textColor, Color backgroundColor, Font font) {
        component.setForeground(textColor);
        component.setBackground(backgroundColor);
        component.setFont(font);
        if (component instanceof JTextArea) {
            ((JTextArea) component).setCaretColor(textColor);
        }
    }

    private void customizeCheckbox(JCheckBox checkBox, Color textColor, Color backgroundColor, Font font) {
        checkBox.setForeground(textColor);
        checkBox.setBackground(backgroundColor);
        checkBox.setFont(font);
    }

    private void customizeInputComponent(JComponent component, Color textColor, Color backgroundColor, Font font) {
        customizeComponent(component, textColor, backgroundColor, font);
        if (component instanceof JTextArea || component instanceof JTextField) {
            component.setBorder(BorderFactory.createCompoundBorder(
                    component.getBorder(),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }
    }


    private JPanel createCheckboxPanel() {
        JPanel checkboxPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        checkboxPanel.add(finalCheckBox);
        return checkboxPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        inputPanel.add(createClassNamePanel());
        inputPanel.add(createVariablesPanel());
        return inputPanel;
    }

    private JPanel createClassNamePanel() {
        JPanel classNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        classNamePanel.add(new JLabel("Enter the name of the class:"));
        classNamePanel.add(classNameField);
        return classNamePanel;
    }

    private JPanel createVariablesPanel() {
        JPanel variablesPanel = new JPanel(new BorderLayout());
        variablesPanel.add(new JLabel("Enter variable name and type (one per line):"), BorderLayout.NORTH);
        variablesPanel.add(new JScrollPane(variablesArea), BorderLayout.CENTER);
        return variablesPanel;
    }

    private JPanel createButtonPanel(Color buttonColor, Color textColor, Font font) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateButton = new JButton("Generate Code");
        customizeButton(generateButton, buttonColor, textColor, font);
        generateButton.addActionListener(e -> {
            try {
                generateCode();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        buttonPanel.add(generateButton);
        return buttonPanel;
    }

    private void customizeButton(JButton button, Color buttonColor, Color textColor, Font font) {
        button.setBackground(buttonColor);
        button.setForeground(textColor);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createGeneratedCodePanel() {
        JPanel generatedCodePanel = new JPanel(new BorderLayout());
        generatedCodePanel.add(new JLabel("Generated class code:"), BorderLayout.NORTH);
        generatedCodePanel.add(new JScrollPane(generatedCodeArea), BorderLayout.CENTER);
        return generatedCodePanel;
    }


    private void generateCode() throws IOException {
        String className = classNameField.getText().trim();

        // Check if className field is empty
        if (className.isEmpty()) {
            generatedCodeArea.setText("Enter a class name please.");
            return;
        }

        DomainPrimitiveBuilder builder = new DomainPrimitiveBuilder(className);


        String[] lines = variablesArea.getText().split("\\n");
        for (String line : lines) {
            int spaceIndex = line.indexOf(' ');
            if (spaceIndex != -1 && spaceIndex < line.length() - 1) {
                String variableType = line.substring(0, spaceIndex).trim();
                String variableName = line.substring(spaceIndex + 1).trim();
                builder.addVariable(variableType, variableName);
            }
        }

        DomainPrimitive domainPrimitive = builder.build();

        StringBuilder classCode = new StringBuilder();
        classCode.append("public class ").append(capitalize(className)).append(" {\n\n");

        List<String> variables = builder.getVariables();
        java.util.List<String> variableTypes = builder.getVariableTypes();

        for (int i = 0; i < variables.size(); i++) {
            String variable = variables.get(i);
            String variableType = variableTypes.get(i);

            // Check if the final checkbox is selected
            String finalKeyword = finalCheckBox.isSelected() ? "final " : "";

            classCode.append("\tprivate ").append(finalKeyword).append(variableType).append(" ").append(variable).append(";\n");
        }
        classCode.append("\n");

        // Generating constructor
        classCode.append(domainPrimitive.generateConstructor());


        StringBuilder constructorCode = new StringBuilder();
        constructorCode.append("\tpublic ").append(capitalize(className)).append("(");

        // Process variables for constructor parameters and class fields with validation
        String[] lines1 = variablesArea.getText().split("\\n");
        for (int i = 0; i < lines1.length; i++) {
            String line = lines1[i];
            int spaceIndex = line.indexOf(' ');
            if (spaceIndex != -1 && spaceIndex < line.length() - 1) {
                String variableType = line.substring(0, spaceIndex).trim();
                String variableName = line.substring(spaceIndex + 1).trim();
                // Add field
                String finalKeyword = finalCheckBox.isSelected() ? "final " : "";
                classCode.append("\tprivate ").append(finalKeyword).append(variableType).append(" ").append(variableName).append("() {\n");

                // Add to constructor parameters
                constructorCode.append(variableType).append(" ").append(variableName);
                if (i < lines.length - 1) constructorCode.append(", ");

                // Add validation logic for String and int
                if ("String".equals(variableType)) {
                    classCode.append("\t\tif (").append(variableName).append(" == null || ").append(variableName).append(".trim().isEmpty()) {\n")
                            .append("\t\t\tthrow new IllegalArgumentException(\"").append(variableName).append(" cannot be null or empty.\");\n")
                            .append("\t\t}\n");

                } else if ("int".equals(variableType)) {
                    classCode.append("\t\tif (").append(variableName).append(" <= 0) {\n")
                            .append("\t\t\tthrow new IllegalArgumentException(\"").append(variableName).append(" must be positive.\");\n")
                            .append("\t\t}\n");

                } else if ("float".equals(variableType)) {
                    classCode.append("\t\tif (").append(variableName).append(" ==");
                }
                classCode.append("\t\treturn ").append(variableName).append(";\n")
                        .append("\t}\n");
            }
        }

        /* Generate get methods */
        classCode.append(domainPrimitive.generateGet());


        classCode.append("}\n");

        // Generating Builder class within a Domain Primitive

        classCode.append("\tpublic static class Builder {\n");

        for (int i = 0; i < variables.size(); i++) {
            String variable = variables.get(i);
            String variableType = variableTypes.get(i);

            // Check if the final checkbox is selected
            String finalKeyword = finalCheckBox.isSelected() ? "final " : "";

            classCode.append("\t\tprivate ").append(finalKeyword).append(variableType).append(" ").append(variable).append(";\n");
        }
        classCode.append("\n");
        classCode.append(domainPrimitive.generateBuilderGet());
        classCode.append(domainPrimitive.generateBuilderBuild());
        classCode.append("\t}\n");


        classCode.append("\n");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            String fileName = className + ".java";
            File file = new File(selectedDirectory, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(classCode.toString());
                generatedCodeArea.setText("Code has been generated and saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                generatedCodeArea.setText("Error writing code to file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        generatedCodeArea.setText(classCode.toString());
    }


    private String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(DomainPrimitiveGenerator::new);
    }
}
