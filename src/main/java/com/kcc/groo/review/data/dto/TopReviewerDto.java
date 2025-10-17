package com.kcc.groo.review.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TopReviewerDto {
    private String userId;
    private int reviewCount;
}
