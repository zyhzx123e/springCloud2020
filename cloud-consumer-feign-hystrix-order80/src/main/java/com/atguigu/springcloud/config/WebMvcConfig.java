package com.atguigu.springcloud.config;

import com.atguigu.springcloud.interceptor.LoginInterceptor;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.util.UrlPathHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;




//once use @EnableWebMvc, it will add @import(DelegatingWebMvcConfiguration.class)
//DelegatingWebMvcConfiguration it self oso customized some behavior of webmvc config with WebMvcConfigurerComposite
//it get all WebMvcConfigurer in container and configured some most fundamental components like RequestMappingHandlerMapping
//@EnableWebMvc//you controll all the logic including welcome page, static files, etc.
@Configuration(proxyBeanMethods = false)//no need proxy(lite mode,higher performance)
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sessionManagerCustomizer() {
        return (server) -> {
            server.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/401"));
            server.setPort(8080);
            //server.setProtocol("AJP/1.3");
            server.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                @Override
                public void customize(org.apache.catalina.connector.Connector connector) {

                    //1.port & https
                    connector.setPort(8080);
                    connector.setSecure(true);
                    connector.setScheme("https");

                    //2. set x-power-by header and allow trace
                    connector.setXpoweredBy(false);
                    connector.setAllowTrace(false);
                    System.out.println("WebServerFactoryCustomizer customize update http2");

                    //3.enable http2
                    connector.addUpgradeProtocol(new Http2Protocol());

                    //4. customize protocl
                    AbstractHttp11Protocol<?> protocol = ((AbstractHttp11Protocol<?>) connector.getProtocolHandler());

                    //4.1 enable ssl: keystore & truststore
                    protocol.setSSLEnabled(true);
                    protocol.setTcpNoDelay(true);
//                    protocol.setKeystoreFile(keystore.getAbsolutePath());
//                    protocol.setKeystorePass("changeit");
//                    protocol.setTruststoreFile(truststore.getAbsolutePath());
//                    protocol.setTruststorePass("changeit");
//                    protocol.setKeyAlias("apitester");

                    //4.2 tls cipher
                    protocol.setUseServerCipherSuitesOrder(true);
                    protocol.setUseServerCipherSuitesOrder(true);
                    protocol.setSSLProtocol("TLSv1.3,TLSv1.2");//sslEnabledProtocols="TLSv1.2"
                    protocol.setSSLHonorCipherOrder(true);
                    protocol.setCiphers(
                            "TLS_AES_256_GCM_SHA384, "
                                    + "TLS_CHACHA20_POLY1305_SHA256, "
                                    + "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, "
                                    + "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, "
                                    + "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, "
                    );

                    //4.3 2way tls
                    protocol.setClientAuth(SSLHostConfig.CertificateVerification.REQUIRED.name());
                    protocol.setSSLVerifyClient(SSLHostConfig.CertificateVerification.REQUIRED.name());

                }
            });
        };
    }


    @Bean
    public HiddenHttpMethodFilter hiddenHttpFilter(){
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        hiddenHttpMethodFilter.setMethodParam("_m");//for parameter key matching
        return hiddenHttpMethodFilter;
    }

    public WebMvcConfigurer webMvcConfigurer(){// @Scheduled(cron = "#{'${schedule.data.time}'.split(',')[0]}"),
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //WebMvcConfigurer.super.addInterceptors(registry);
                registry.addInterceptor(new LoginInterceptor())
                        .addPathPatterns("/**")//intercept what path
                        //note if put /** means all resources including static resources like js,css those ,so that
                        //some of the css style might not work if redirect to login path
                        .excludePathPatterns("/login","/",
                                "/aa/**",
                                "/css/**","/js/**","/fonts/**","/images/**")//not intercept path
                ;

            }

            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                //WebMvcConfigurer.super.configurePathMatch(configurer);
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                urlPathHelper.setRemoveSemicolonContent(false);
                //for matrix content use semi-colon(;)
                configurer.setUrlPathHelper(urlPathHelper);
            }

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                //WebMvcConfigurer.super.configureContentNegotiation(configurer);

                HashMap<String, MediaType> mediaTypes = new HashMap();
                mediaTypes.put("json",MediaType.APPLICATION_JSON);
                mediaTypes.put("xml",MediaType.APPLICATION_XML);
                mediaTypes.put("gg",MediaType.parseMediaType("application/x-gg"));
                ParameterContentNegotiationStrategy parameterContentNegotiationStrategy = new ParameterContentNegotiationStrategy(mediaTypes);
                //parameterContentNegotiationStrategy.setParameterName("contentType");//default format

                //remeber add back the header , else it will only apply the parameterContentNego strategy
                HeaderContentNegotiationStrategy headerContentNegotiationStrategy = new HeaderContentNegotiationStrategy();
//                headerContentNegotiationStrategy.
                configurer.strategies(Arrays.asList(parameterContentNegotiationStrategy,
                        headerContentNegotiationStrategy));
            }

            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                WebMvcConfigurer.super.configureAsyncSupport(configurer);
            }

            @Override
            public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
                WebMvcConfigurer.super.configureDefaultServletHandling(configurer);
            }

            @Override
            public void addFormatters(FormatterRegistry registry) {
                WebMvcConfigurer.super.addFormatters(registry);
            }


            //configure how u control static files like js/css/ etc.
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                //WebMvcConfigurer.super.addResourceHandlers(registry);
                registry.addResourceHandler("/aa/**").
                        addResourceLocations("classpath:/static/");
                //all the /aa/ in request url use /static/ path
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                WebMvcConfigurer.super.addCorsMappings(registry);
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                WebMvcConfigurer.super.addViewControllers(registry);
            }

            @Override
            public void configureViewResolvers(ViewResolverRegistry registry) {
                WebMvcConfigurer.super.configureViewResolvers(registry);
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                WebMvcConfigurer.super.addArgumentResolvers(resolvers);
            }

            @Override
            public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
                WebMvcConfigurer.super.addReturnValueHandlers(handlers);
            }

            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                WebMvcConfigurer.super.configureMessageConverters(converters);
            }

            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                WebMvcConfigurer.super.extendMessageConverters(converters);
            }

            @Override
            public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
                WebMvcConfigurer.super.configureHandlerExceptionResolvers(resolvers);
            }

            @Override
            public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
                WebMvcConfigurer.super.extendHandlerExceptionResolvers(resolvers);
            }

            @Override
            public Validator getValidator() {
                return WebMvcConfigurer.super.getValidator();
            }

            @Override
            public MessageCodesResolver getMessageCodesResolver() {
                return WebMvcConfigurer.super.getMessageCodesResolver();
            }
        };
    }
}
