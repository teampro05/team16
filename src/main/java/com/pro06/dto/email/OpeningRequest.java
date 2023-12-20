package com.pro06.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpeningRequest {
    private String reqId;
    private String part;
    private String lecType;
    private String lecDays;
    private String comment;
    private LocalDateTime crDate;
    private LocalDateTime modDate;
}
