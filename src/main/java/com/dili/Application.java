package com.dili;

import com.dili.ss.dto.DTOScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * 由MyBatis Generator工具自动生成
 * @author yuehongbo
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.dili.ss","com.dili.customer","com.dili.uap.sdk","com.dili.logger.sdk"})
@RestfulScan({"com.dili.customer.rpc","com.dili.uap.sdk.rpc"})
@DTOScan(value={ "com.dili.customer.domain"})
@EnableDiscoveryClient
@EnableFeignClients
public class Application extends SpringBootServletInitializer {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
//        System.out.println(IdcardUtil.isValidCard("342501197904061777"));
        SpringApplication.run(Application.class, args);
    }


}
