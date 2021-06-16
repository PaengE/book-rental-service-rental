package com.my.rental.adaptor;

import com.my.rental.config.FeignConfiguration;
import com.my.rental.web.rest.dto.LateFeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gateway", configuration = { FeignConfiguration.class })
public interface UserClient {
    @PutMapping("/api/users/latefee")
    ResponseEntity usePoint(@RequestBody LateFeeDTO lateFeeDTO);
}
