package com.top.effitopia.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 매출 배치 스케줄러
 */
@Component
@RequiredArgsConstructor
public class RevenueBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job dailyRevenueJob;
    private final Job monthlyRevenueJob;

    /**
     * 작일에 대한 일 배치 스케줄
     * 매일 2시 실행
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void runDailyRevenueJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("baseDate", LocalDate.now().minusDays(1).toString())
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(dailyRevenueJob, params);
    }

    /**
     * 작월에 대한 월 배치 스케줄
     * 매월 5일 0시 실행
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 5 * *")
    public void runMonthlyRevenueJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("baseDate", LocalDate.now().minusMonths(1).toString())
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(monthlyRevenueJob, params);
    }

}
