package com.github.cotrod.kyriba.object;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class GroupForAnalysis {
    private Set<TypeOfGroup> types = new HashSet<>();

    public Set<TypeOfGroup> getTypes() {
        return types;
    }

    public void setTypes(TypeOfGroup type) {
        types.add(type);
    }

    public boolean hasGroupParam() {
        return types.size() > 0;
    }
}
