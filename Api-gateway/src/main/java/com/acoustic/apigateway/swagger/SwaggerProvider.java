package com.acoustic.apigateway.swagger;

import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;


@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
    public static final String API_URI = "/v3/api-docs";
    public static final String SWAGGER_VERSION = "3.0";
    public static final String ROUTE_PREFIX = "server";
    public static final String GENKEY_0 = "_genkey_0";

    private final RouteDefinitionLocator routeLocator;

    public SwaggerProvider(RouteDefinitionLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        routeLocator.getRouteDefinitions().subscribe(routeDefinition -> {
            if (!routeDefinition.getId().startsWith(ROUTE_PREFIX)) {
                String resourceName = routeDefinition.getId();
                String location = routeDefinition.getPredicates().get(0).getArgs().get(GENKEY_0).replace("/**", API_URI);
                resources.add(swaggerResource(resourceName, location));
            }
        });

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(SWAGGER_VERSION);
        return swaggerResource;
    }
}
