package com.tongji.codejourneycolab.codejourneycolabbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    // ! 仅作测试！
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("da jia hao a ,wo shi codejourneycolab");
    }
    // ! 仅作测试！这个接口需要携带token才能访问，并且不需要传入id，id在token里面
    @GetMapping("/helloauthorized")
    public ResponseEntity<String> helloAuthorized(@RequestAttribute Integer id) {
        return ResponseEntity.ok("尊贵的用户 " + id + " 您好，欢迎来到codejourneycolab");
    }
}
