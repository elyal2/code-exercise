package org.wf.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.wf.client.service.AzureADService;
import org.wf.data.security.UserInfo;

@Slf4j
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BearerTokenFilter implements ContainerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Inject
    @RestClient
    Instance<AzureADService> azureADService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            validateToken(token);
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage(), e);
            abortWithUnauthorized(requestContext);
        }
    }

    private void validateToken(String token) throws Exception {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        try {
            UserInfo userInfo = azureADService.get().getUserInfo("Bearer " + token);
            String userInfoJson = MAPPER.writeValueAsString(userInfo);
            ThreadLocalStorage.setTenantID(userInfo.getUserPrincipalName());
            Log.info("User Info ["+ThreadLocalStorage.getTenantID()+"]" );
        } catch (Exception e) {
            throw new Exception("Invalid token: "+ e.getMessage());
        }

    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        String token = "[EMPTY]";
        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token from the HTTP Authorization header
            token = authorizationHeader.substring("Bearer".length()).trim();
        }
        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\""+token+"\", error=\"invalid_token\", error_description=\"The access token is invalid\"")
                        .build());
    }
}
