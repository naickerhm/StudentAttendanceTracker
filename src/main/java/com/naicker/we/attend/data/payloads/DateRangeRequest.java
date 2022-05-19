package com.naicker.we.attend.data.payloads;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DateRangeRequest {
    @NotBlank
    @NotNull
    public LocalDate startDate;

    @NotBlank
    @NotNull
    public LocalDate endDate;

    public String toString(){
        return "Start: " + startDate + ", End: " + endDate;
    }
}
