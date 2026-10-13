package com.sahib.uberApp.UberApplication.config;

import com.sahib.uberApp.UberApplication.dtos.PointDto;
import com.sahib.uberApp.UberApplication.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();

        // pointDto covert to -> point

        modelMapper.typeMap(PointDto.class , Point.class).setConverter(
                context -> {
                    PointDto pointDto = context.getSource();
                    return GeometryUtil.convertPointDtoToPoint(pointDto);
                });

        // point convert to pointDto :

        modelMapper.typeMap(Point.class , PointDto.class).setConverter(context -> {

            Point point = context.getSource();

            double longitude = point.getX();
            double latitude = point.getY();

            double [] coordinates = {longitude , latitude};
            return new PointDto(coordinates);

        });

        return modelMapper;
    }

    @Bean
    public RestClient  restClient(){
        return RestClient.builder().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
