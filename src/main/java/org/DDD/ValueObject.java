package org.DDD;

import javax.swing.*;
import java.util.List;

public class ValueObject implements Builder {
    private final String className;
    private final List<String> variables;
    private final List<String> variableTypes;

    public ValueObject(ValueObjectBuilder builder) {
        this.className = builder.className;
        this.variables = builder.variables;
        this.variableTypes = builder.variableTypes;
    }

    @Override
    public String generatetoString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\t@Override\n");
        sb.append("\tpublic String toString() {\n");
        sb.append("\t\treturn ");
        // Append each variable with its corresponding value
        for (int i = 0; i < variables.size(); i++) {
            String variable = variables.get(i);
            sb.append(variable).append("='").append((variable)).append("'");
            if (i < variables.size() - 1) {
                sb.append(" + ");
            }
        }

        sb.append(";\n");
        sb.append("\t}\n");
        return sb.toString();

    }

    @Override
    public String generateHashCode() {
        StringBuilder hashCodeCode = new StringBuilder();

        hashCodeCode.append("\t@Override\n");
        hashCodeCode.append("\tpublic int hashCode() {\n");
        hashCodeCode.append("\t\treturn Objects.hash(");

        for (int i = 0; i < variables.size(); i++) {
            hashCodeCode.append(variables.get(i));
            if (i < variables.size() - 1) {
                hashCodeCode.append(", ");
            }
        }

        hashCodeCode.append(");\n");
        hashCodeCode.append("\t}\n");

        return hashCodeCode.toString();
    }


    @Override
    public String generateGet() {
        StringBuilder getCode = new StringBuilder();
        // Generate getter methods for each variable
        for (String variable : variables) {
            // Construct the getter method name
            String getterName = "get" + variable.substring(0, 1).toUpperCase() + variable.substring(1);

            // Append the getter method definition
            getCode.append("\n\t/ Get method for ").append(variable).append("\n");
            getCode.append("\tpublic ").append(variableTypes.get(variables.indexOf(variable))).append(" ").append(getterName).append("() {\n");
            getCode.append("\t\treturn ").append(variable).append(";\n");
            getCode.append("\t}\n");
        }
        getCode.append("\t\n");

        return getCode.toString();
    }

    @Override
    public String generateBuilderGet() {
        StringBuilder getCode = new StringBuilder();
        // Generate getter methods for each variable
        for (String variable : variables) {
            getCode.append("\t\tpublic Builder").append(" ").append(variable).append("(").append(variableTypes.get(variables.indexOf(variable))).append(" ").append(variable).append(") {\n");
            getCode.append("\t\t\tthis.").append(variable).append(" = ").append(variable).append(";\n");
            getCode.append("\t\t\treturn ").append("this").append(";\n");
            getCode.append("\t\t}\n");
        }
        getCode.append("\t\t\n");

        return getCode.toString();
    }

    @Override
    public String generateBuilderBuild() {
        StringBuilder getCode  = new StringBuilder();
        getCode.append("\t\tpublic ").append(capitalize(className)).append(" build() {\n");
        getCode.append("\t\t\treturn new ").append(className).append("(this);\n");
        getCode.append("\t\t}\n");
        return getCode.toString();
    }

    @Override
    public String generateConstructor() {
        StringBuilder constructorCode = new StringBuilder();

        // Append the constructor signature
        constructorCode.append("\tpublic ").append(capitalize(className)).append("(");

        // Append parameters for constructor
        for (int i = 0; i < variables.size(); i++) {
            if (i > 0) {
                constructorCode.append(", ");
            }
            constructorCode.append(variableTypes.get(i)).append(" ").append(variables.get(i));
        }

        // Append the constructor body
        constructorCode.append(") {\n");
        // Initialize fields in the constructor body if needed
        for (String variable : variables) {
            constructorCode.append("\t\tthis.").append(variable).append(" = ").append(variable).append(";\n");
        }
        constructorCode.append("\t}\n");

        return constructorCode.toString();
    }



    private String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}



