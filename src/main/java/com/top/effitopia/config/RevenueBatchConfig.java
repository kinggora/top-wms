package com.top.effitopia.config;

import com.top.effitopia.domain.DailyUsage;
import com.top.effitopia.domain.Revenue;
import com.top.effitopia.dto.DailyInOutboundDTO;
import com.top.effitopia.dto.DailyUsageCreateDTO;
import com.top.effitopia.dto.FreightCostDTO;
import com.top.effitopia.dto.MonthlyUsageDTO;
import com.top.effitopia.service.RevenueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 매출 관련 배치 설정
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class RevenueBatchConfig {

    private final SqlSessionFactory sqlSessionFactory;
    private final RevenueFactory revenueFactory;

    /**
     * 일일 매출 배치 처리 Job
     * 1. 일일 창고 사용 면적 집계
     * 2. 일일 입/출고 수수료 매출 등록
     * 3. 일일 운송료 매출 등록
     *
     * @param jobRepository
     * @param transactionManager
     * @return 일 매출 JobBuilder
     */
    @Bean
    public Job dailyRevenueJob(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new JobBuilder("dailyRevenueJob", jobRepository)
                .start(dailyAreaUsageStep(jobRepository, transactionManager))
                .next(dailyInboundOutboundFeeStep(jobRepository, transactionManager))
                .next(dailyFreightChargeStep(jobRepository, transactionManager))
                .build();
    }

    /**
     * 월간 매출 배치 처리 Job
     * 1. 월간 창고 총 사용량에 따른 보관료 매출 등록
     *
     * @param jobRepository
     * @param transactionManager
     * @return 월 매출 JobBuilder
     */
    @Bean
    public Job monthlyRevenueJob(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new JobBuilder("monthlyRevenueJob", jobRepository)
                .start(monthlyStorageCostStep(jobRepository, transactionManager))
                .build();
    }

    /**
     * 일일 창고 사용 면적 집계 Step
     * (창고, 회원) 별로 재고 상품의 면적, 수량을 조회해여 사용 면적을 집계
     *
     * @param jobRepository
     * @param transactionManager
     * @return
     */
    @Bean
    public Step dailyAreaUsageStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dailyAreaUsageStep", jobRepository)
                .<DailyUsageCreateDTO, DailyUsage>chunk(200, transactionManager)
                .reader(dailyAreaUsageReader())
                .processor(dailyUsageProcessor())
                .writer(dailyAreaUsageWriter())
                .build();
    }

    /**
     * 일일 입/출고 수수료 매출 등록 Step
     * (창고, 회원) 별로 입/출고 박스 수, 기준 수수료를 조회하여 입/출고 수수료를 매출로 등록
     *
     * @param jobRepository
     * @param transactionManager
     * @return
     */
    @Bean
    public Step dailyInboundOutboundFeeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dailyInboundOutboundFeeStep", jobRepository)
                .<DailyInOutboundDTO, List<Revenue>>chunk(200, transactionManager)
                .reader(dailyInboundOutboundReader(null))
                .processor(dailyInboundOutboundProcessor(null))
                .writer(dailyInboundOutboundWriter(revenueItemWriter()))
                .build();
    }

    /**
     * 월간 창고 보관료 매출 등록 Step
     * (창고, 회원) 별 일일 창고 사용량을 합산하여 월간 창고 보관료를 매출로 등록
     *
     * @param jobRepository
     * @param transactionManager
     * @return
     */
    @Bean
    public Step monthlyStorageCostStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("monthlyStorageCostStep", jobRepository)
                .<MonthlyUsageDTO, Revenue>chunk(200, transactionManager)
                .reader(monthlyStorageCostReader(null))
                .processor(monthlyStorageCostProcessor(null))
                .writer(revenueItemWriter())
                .build();
    }

    /**
     * 일일 운송료 매출 등록 Step
     * 발행된 운송장에 대하여 운송료를 합산하여 매출로 등록
     *
     * @param jobRepository
     * @param transactionManager
     * @return
     */
    @Bean
    public Step dailyFreightChargeStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("dailyFreightChargeStep", jobRepository)
                .<FreightCostDTO, Revenue>chunk(200, transactionManager)
                .reader(dailyFreightCostReader(null))
                .processor(dailyFreightCostProcessor(null))
                .writer(revenueItemWriter())
                .build();
    }

    /**
     * (창고, 회원) 별 재고 상품의 면적, 수량 조회 reader
     *
     * @return
     */
    @Bean
    public ItemReader<DailyUsageCreateDTO> dailyAreaUsageReader() {
        return new MyBatisPagingItemReaderBuilder<DailyUsageCreateDTO>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.top.effitopia.mapper.DailyUsageMapper.selectDailyStockArea")
                .pageSize(200)
                .build();
    }

    /**
     * DailyUsageCreateDTO -> DailyUsage 변환 Processor
     *
     * @return
     */
    @Bean
    public ItemProcessor<DailyUsageCreateDTO, DailyUsage> dailyUsageProcessor() {
        return dto -> DailyUsage.builder()
                .warehouseId(dto.getWarehouseId())
                .memberId(dto.getMemberId())
                .totalArea(dto.getTotalArea())
                .storageCostPerArea(dto.getStorageCostPerArea())
                .baseDate(LocalDate.now().minusDays(1))
                .regDate(LocalDate.now())
                .build();
    }

    /**
     * DailyUsage Writer
     *
     * @return
     */
    @Bean
    public ItemWriter<DailyUsage> dailyAreaUsageWriter() {
        return new MyBatisBatchItemWriterBuilder<DailyUsage>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.top.effitopia.mapper.DailyUsageMapper.insertDailyUsage")
                .build();
    }

    /**
     * 기준 날짜의 일일 입/출고 데이터 조회 Reader
     *
     * @param baseDate 기준 날짜
     * @return
     */
    @Bean
    @StepScope
    public ItemReader<DailyInOutboundDTO> dailyInboundOutboundReader(@Value("#{jobParameters['baseDate']}") String baseDate) {
        return new MyBatisPagingItemReaderBuilder<DailyInOutboundDTO>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.top.effitopia.mapper.DailyUsageMapper.selectDailyInboundOutboundSummary")
                .parameterValues(Map.of("baseDate", baseDate))
                .pageSize(200)
                .build();
    }

    /**
     * DailyInOutboundDTO -> List<Revenue> 변환 Processor
     * 입/출고 박스 수가 0 이상이면 매출 데이터 생성
     *
     * @param baseDate 기준 날짜
     * @return
     */
    @Bean
    @StepScope
    public ItemProcessor<DailyInOutboundDTO, List<Revenue>> dailyInboundOutboundProcessor(@Value("#{jobParameters['baseDate']}") String baseDate) {
        LocalDate date = LocalDate.parse(baseDate);
        return dto -> {
            List<Revenue> dailyRevenueList = new ArrayList<>();
            if(dto.getInboundBoxCnt() > 0) {
                Revenue inboundFee = revenueFactory.createInboundFee(dto, date);
                dailyRevenueList.add(inboundFee);
            }
            if(dto.getOutboundBoxCnt() > 0) {
                Revenue outboundFee = revenueFactory.createOutboundFee(dto, date);
                dailyRevenueList.add(outboundFee);
            }
            return dailyRevenueList;
        };
    }

    /**
     * 조회 chunk당 생성된 List<Revenue>를 flat list로 변환하여 revenueItemWriter 에 위임
     *
     * @return
     */
    @Bean
    public ItemWriter<List<Revenue>> dailyInboundOutboundWriter(ItemWriter<Revenue> revenueItemWriter) {
        return chunk -> {
            List<Revenue> flatList = chunk.getItems().stream()
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            revenueItemWriter.write(new Chunk<>(flatList));;
        };
    }

    /**
     * 기준 날짜의 일일 운송료 합계 조회 Reader
     *
     * @param baseDate 기준 날짜
     * @return
     */
    @Bean
    @StepScope
    public ItemReader<FreightCostDTO> dailyFreightCostReader(@Value("#{jobParameters['baseDate']}") String baseDate) {
        return new MyBatisPagingItemReaderBuilder<FreightCostDTO>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.top.effitopia.mapper.DailyUsageMapper.selectDailyTotalFreightCost")
                .parameterValues(Map.of("baseDate", baseDate))
                .pageSize(200)
                .build();
    }

    /**
     * FreightCostDTO -> Revenue 변환 Processor
     *
     * @param baseDate
     * @return
     */
    @Bean
    @StepScope
    public ItemProcessor<FreightCostDTO, Revenue> dailyFreightCostProcessor(@Value("#{jobParameters['baseDate']}") String baseDate) {
        LocalDate date = LocalDate.parse(baseDate);
        return dto -> revenueFactory.createFreightCost(dto, date);
    }

    /**
     * 월간 창고 보관료 조회 Reader
     * 기준 월의 [일별 창고 사용량 * 해당 일 보관료]의 합산 조회
     *
     * @param baseDate 기준 날짜
     * @return
     */
    @Bean
    @StepScope
    public ItemReader<MonthlyUsageDTO> monthlyStorageCostReader(@Value("#{jobParameters['baseDate']}") String baseDate) {
        LocalDate date = LocalDate.parse(baseDate);
        return new MyBatisPagingItemReaderBuilder<MonthlyUsageDTO>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.top.effitopia.mapper.DailyUsageMapper.selectMonthlyTotalStorageCost")
                .parameterValues(Map.of("year", date.getYear(), "month", date.getMonthValue()))
                .pageSize(200)
                .build();
    }

    /**
     * MonthlyUsageDTO -> Revenue 변환 Processor
     *
     * @param baseDate
     * @return
     */
    @Bean
    @StepScope
    public ItemProcessor<MonthlyUsageDTO, Revenue> monthlyStorageCostProcessor(@Value("#{jobParameters['baseDate']}") String baseDate) {
        LocalDate date = LocalDate.parse(baseDate);
        return dto -> revenueFactory.createStorageCost(dto, date);
    }

    /**
     * Revenue Writer
     *
     * @return
     */
    @Bean
    public ItemWriter<Revenue> revenueItemWriter() {
        return new MyBatisBatchItemWriterBuilder<Revenue>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.top.effitopia.mapper.RevenueMapper.insert")
                .build();
    }

}
