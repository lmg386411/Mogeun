package com.mogun.backend.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "hello", tags = {"swagger", "v1", "api"})
@RestController
@RequestMapping("/api")
public class SwaggerController {

    @ApiOperation(value = "this is test", notes = "note")
    @PostMapping("/test")
    public String test(@ApiParam(name = "first param", value = "first value", required = true) String input,
                       @ApiParam(name = "second param", value = "second value", required = false) String input2) {
        return "test";
    }
}
