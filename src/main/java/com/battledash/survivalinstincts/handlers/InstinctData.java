package com.battledash.survivalinstincts.handlers;

import com.battledash.survivalinstincts.instincts.AbstractInstinct;
import com.google.common.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstinctData {
    private final List<AbstractInstinct> instincts = new ArrayList<>();

    public List<AbstractInstinct> getInstincts() {
        return instincts;
    }

    public <T extends AbstractInstinct> List<T> getInstinctsOfType(Class<T> clazz) {
        return instincts.stream().filter(clazz::isInstance).map(s -> (T)s).collect(Collectors.toList());
    }
}
