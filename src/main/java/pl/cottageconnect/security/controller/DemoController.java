package pl.cottageconnect.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'OWNER')")
public class DemoController {

    @GetMapping(value = "/hello")
    public String helloWorld() {
        return "hello World";
    }
}
