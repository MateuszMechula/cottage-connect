package pl.cottageconnect.cottage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = CottageController.BASE_PATH)
public class CottageController {
    public static final String BASE_PATH = "/api/v1/cottages";

    @GetMapping
    public String getCottage() {
        return "hello";
    }


}
