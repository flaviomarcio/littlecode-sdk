package com.app.common.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Configuration
public class SwaggerConfig {

    private static final String STATIC_BEARER = "Bearer ";
    private static final String STATIC_HEADER = "header";

    @Value(value = "${springdoc.info.company:}")
    private String infoCompany;

    @Value(value = "${springdoc.info.product:}")
    private String infoProduct;

    @Value(value = "${springdoc.info.title:}")
    private String infoTitle;

    @Value(value = "${springdoc.info.description:}")
    private String infoDescription;

    @Value(value = "${springdoc.info.version:v0.0.0}")
    private String infoVersion;

    @Value(value = "${springdoc.servers.dev:}")
    private String srvDev;

    @Value(value = "${springdoc.servers.stg:}")
    private String srvStg;

    @Value(value = "${springdoc.servers.prd:}")
    private String srvPrd;

    @Value(value = "${server.servlet.context-path:/}")
    private String infoContextPath;

    private List<Server> makeServerList() {
        List<Server> __return = new ArrayList<>();
        List.of(srvDev, srvStg, srvPrd)
                .forEach(server -> {
                    if (!server.isEmpty())
                        __return.add(
                                new Server()
                                        .url(String.format("%s%s", server, infoContextPath)
                                        )
                        );
                });
        return __return;
    }

    @Bean
    public OpenAPI makeOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(String.format("%s %s - %s", infoCompany, infoProduct, infoTitle))
                                .description(infoDescription)
                                .version(infoVersion)
                )
                .servers(makeServerList());
    }

    @Bean
    public OperationCustomizer makeDefaultHeaders() {
        return (operation, handlerMethod) -> {
            operation
                    .addParametersItem(
                            new io.swagger.v3.oas.models.parameters.Parameter()
                                    .in(STATIC_HEADER)
                                    .name(HttpHeaders.AUTHORIZATION)
                                    .required(true)
                                    .schema(new io.swagger.v3.oas.models.media.StringSchema())
                                    .description(STATIC_BEARER + " token")
                    );

            return operation;
        };
    }

}
