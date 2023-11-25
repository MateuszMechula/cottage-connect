package pl.cottageconnect.security.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearer-token")
public class DemoController {

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'OWNER')")
    @GetMapping(value = "/hello")
    public String helloWorld() {
        return "hello World";
    }
}


