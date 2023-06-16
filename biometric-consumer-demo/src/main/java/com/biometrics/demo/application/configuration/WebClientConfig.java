package com.biometrics.demo.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    private final int CONNECT_TIMEOUT = 40000;
    private final long READ_TIMEOUT = 25000;

    private final String URL = "https://siuclientgw.sunedu.gob.pe";

  @Bean
  public WebClient webClient() {
    HttpClient httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                        new ReadTimeoutHandler(
                                READ_TIMEOUT, TimeUnit.MILLISECONDS)));

    ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    ExchangeStrategies strategies =
        ExchangeStrategies.builder()
            .codecs(
                clientDefaultCodecsConfigurer -> {
                  clientDefaultCodecsConfigurer
                      .defaultCodecs()
                      .jackson2JsonEncoder(
                          new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                  clientDefaultCodecsConfigurer
                      .defaultCodecs()
                      .jackson2JsonDecoder(
                          new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                  clientDefaultCodecsConfigurer.defaultCodecs().maxInMemorySize(26 * 1024 * 1024);
                })
            .build();

    return WebClient.builder()
        .exchangeStrategies(strategies)
        .clientConnector(connector)
        .filter(new MultipartExchangeFilterFunction())
        .baseUrl(URL)
        .build();
  }
}
