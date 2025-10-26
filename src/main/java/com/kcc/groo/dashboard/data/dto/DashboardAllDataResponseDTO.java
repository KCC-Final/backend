package com.kcc.groo.dashboard.data.dto;

import com.kcc.groo.user.data.dto.FollowUserInfoDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DashboardAllDataResponseDTO {

    private List<FollowUserInfoDTO> followers;

    private List<FollowUserInfoDTO> followings;

    private int totalReviews;

    private int totalScrappedBooks;

    private int totalLikedReviews;

    private List<MonthlyStat> monthlyStats;

    private List<YearlyStat> yearlyStats;

    private List<CategoryStat> categoryStats;

    private ReportInfo reportInfo;

    @Getter
    @Builder
    public static class ReportInfo {
        private int myAverage;
        private double totalAverage;
        private int year;
        private int month;
    }
}
