package org.DDD;

import java.util.List;

public class DomainPrimitive implements Builder {
    private final String className;
    private final List<String> variables;
    private final List<String> variableTypes;

    public DomainPrimitive(DomainPrimitiveBuilder builder) {
        this.className = builder.className;
        this.variables = builder.variables;
        this.variableTypes = builder.variableTypes;
    }



    public String addID() {
        return "\t\n" +
                "\n\t// Getter method for " + className + "Id" + "\n" +
                "\tpublic " + "Long " + className + "Id" + "() {\n" +
                "\t\treturn " + deCapitalize(className) + "Id" + ";\n" +
                "\t}\n";
    }

    @Override
    public String generateGet() {
        StringBuilder getCode = new StringBuilder();

        // Generate getter methods for each variable
        for (int i = 0; i < 1; i++) {
            // Construct the getter method name
            String variable = variables.get(i);
            String getterName = "get" + variable.substring(0, 1).toUpperCase() + variable.substring(1);

            // Append the getter method definition
            getCode.append("\n\t// Getter method for ").append(variable).append("\n");
            getCode.append("\tpublic ").append(variableTypes.get(variables.indexOf(variable))).append(" ").append(getterName).append("() {\n");
            getCode.append("\t\treturn ").append(variable).append(";\n");
            getCode.append("\t}\n");
        }


        return getCode.toString();
    }

    @Override
    public String generateConstructor() {
        StringBuilder constructorCode = new StringBuilder();

        // Append the constructor signature
        constructorCode.append("\tpublic ").append(capitalize(className)).append("(");
        // Append parameters for constructor
        for (int i = 0; i < 1; i++) {
            constructorCode.append(variableTypes.get(i)).append(" ").append(variables.get(i));
            constructorCode.append(") {\n");
            constructorCode.append("\t\tthis.").append(variables.get(i)).append(" = ").append(variables.get(i)).append(";\n");
        }

        // Append the constructor body


        for (String variable : variables) {

        }
        constructorCode.append("\t}\n");

        return constructorCode.toString();
    }

    @Override
    public String generatetoString() {
        return null;
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

    public String generateBuilderBuild() {
        String getCode = "\t\tpublic " + capitalize(className) + " build() {\n" +
                "\t\t\treturn new " + className + "(this);\n" +
                "\t\t}\n";
        return getCode;
    }

    @Override
    public String generateHashCode() {
        return null;
    }



    private String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    private String deCapitalize(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }
}





