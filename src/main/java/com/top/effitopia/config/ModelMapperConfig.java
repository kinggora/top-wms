package com.top.effitopia.config;

import com.top.effitopia.domain.Stock;
import com.top.effitopia.dto.StockDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addMappings(new PropertyMap<Stock, StockDTO>() {
            @Override
            protected void configure() {
                map(source.getCell(), destination.getCellDTO());
                map(source.getProduct(), destination.getProductDTO());
            }
        });
        return modelMapper;
    }
}
