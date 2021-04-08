package com.paywallet.borrowerservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/dummy")
public class DummyController {

    @GetMapping("/merchant")
    public ModelAndView merchantView() {
        return new ModelAndView("merchant_view");
    }

    @GetMapping("/thank-you")
    public ModelAndView merchantFinalPage() {
        return new ModelAndView("dummy_merchant");
    }
}
