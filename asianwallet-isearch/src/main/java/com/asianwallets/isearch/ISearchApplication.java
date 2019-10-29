package com.asianwallets.isearch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com.asianwallets")
public class ISearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ISearchApplication.class, args);
    }

}

