package eu.kendall.simon.timetables.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record Departure (
    LocalDateTime expected,
    LocalDateTime scheduled,
    String display,
    @JsonProperty("line")
    Line lineData
){}
