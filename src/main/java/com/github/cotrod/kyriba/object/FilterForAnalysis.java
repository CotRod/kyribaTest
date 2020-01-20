package com.github.cotrod.kyriba.object;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FilterForAnalysis {
    private String username;
    private LocalDate fromDate;
    private LocalDate toDate;
    private TypeOfMessage typeOfMessage;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public TypeOfMessage getTypeOfMessage() {
        return typeOfMessage;
    }

    public void setTypeOfMessage(TypeOfMessage typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    public boolean hasFilter() {
        return username != null || typeOfMessage != null || (fromDate != null && toDate != null);
    }
}
