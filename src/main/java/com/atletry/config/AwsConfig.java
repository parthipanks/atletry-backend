package com.atletry.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

    private final AtletryProperties props;

    @Bean
    public S3Client s3Client() {
        var s3     = props.getAws().getS3();
        var region = Region.of(props.getAws().getRegion());

        if (s3.getAccessKey() != null && !s3.getAccessKey().isBlank()) {
            return S3Client.builder()
                    .region(region)
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(s3.getAccessKey(), s3.getSecretKey())))
                    .build();
        }
        // Falls back to IAM role / env vars / ~/.aws/credentials
        return S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

}
