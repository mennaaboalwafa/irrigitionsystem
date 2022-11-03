package com.example.irrigationsystem.core.utillites;

import com.example.irrigationsystem.core.constants.Constants;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface HeaderUtils {
    List<String> ALLOWED_HEADERS=List.of(Constants.KAFKA_KEY_REPLY_TIMEOUT_MS);
    static Map<String,String> httpToKafka(ServerRequest serverRequest){
      ServerRequest.Headers headers=serverRequest.headers();
      return ALLOWED_HEADERS.stream()
              .filter(allowedHeader-> headers.firstHeader(allowedHeader)!=null)
              .collect(Collectors.toMap(s -> s,headers::firstHeader));
    }
}
