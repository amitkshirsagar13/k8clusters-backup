package io.k8clusters.base.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseFilterConfigurations {

    @Bean
    public FilterRegistrationBean correlationHeaderFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        final CorrelationHeaderFilter correlationHeaderFilter = new CorrelationHeaderFilter();
        registrationBean.setFilter(correlationHeaderFilter);
        registrationBean.setFilter(correlationHeaderFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
