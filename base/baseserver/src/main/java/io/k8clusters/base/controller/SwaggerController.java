package io.k8clusters.base.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SwaggerController {
    @RequestMapping(value = {"/", "/swagger", "swagger-ui"}, method = RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("redirect:" + "/swagger-ui.html");
    }

}
