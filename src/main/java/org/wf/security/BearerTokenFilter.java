package org.wf.security;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.wf.client.service.AzureADService;
import org.wf.client.service.AzureADServiceOIDC;
import org.wf.data.security.UserInfo;

@Slf4j
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BearerTokenFilter implements ContainerRequestFilter {

    @Inject
    @RestClient
    Instance<AzureADService> azureADService;

    @Inject
    @RestClient
    Instance<AzureADServiceOIDC> azureADServiceOIDC;

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
        try {
            UserInfo userInfo = azureADService.get().getUserInfo("Bearer " + token);
            UserInfo userInfoOIDC = azureADServiceOIDC.get().getUserInfo("Bearer " + token);
            ThreadLocalStorage.setTenantID(userInfo.getId());
            log.info("User Info [{}]", userInfo.getUserPrincipalName());
            log.info("User Info OIDC [{}]", userInfoOIDC.getMail());
        } catch (Exception e) {
            throw new Exception("Invalid token. Reason: " + e.getMessage(), e);
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"app\", error=\"invalid_token\", error_description=\"The access token is invalid\"")
                        .build());
    }
}
