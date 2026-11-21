package com.alquimia.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageReconstructor {
    public static String reconstruct(List<String> fragments) {
        return fragments.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(word -> !word.isEmpty())
            .collect(Collectors.joining(" "));
    }
}
