package org.DDD;

import javax.swing.*;

public interface Builder {

    String generateGet();
    String generateConstructor();
    String generateHashCode();
    String generatetoString();
    String generateBuilderGet();
    String generateBuilderBuild();
}