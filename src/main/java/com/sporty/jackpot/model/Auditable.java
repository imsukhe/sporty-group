package com.sporty.jackpot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auditable {

    private String updatedBy;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}