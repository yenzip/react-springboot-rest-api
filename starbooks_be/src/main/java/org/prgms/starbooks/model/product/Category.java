package org.prgms.starbooks.model.product;

public enum Category {
    NOVEL("소설"),
    ESSAY("에세이"),
    IT("IT"),
    HISTORY("역사");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
