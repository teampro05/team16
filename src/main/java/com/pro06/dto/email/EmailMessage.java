package com.pro06.dto.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class EmailMessage {
    private String to;
    private String subject;
    private String subtitle;
    private String message;
    private String ototitle;
    private String otocontent;
}
