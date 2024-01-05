package org.wf.client.service;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.wf.data.security.UserInfo;

@Path("/oidc")
@RegisterRestClient(baseUri = "https://graph.microsoft.com")
public interface AzureADServiceOIDC {
    @GET
    @Path("/userinfo")
    @Produces(MediaType.APPLICATION_JSON)
    UserInfo getUserInfo(@HeaderParam("Authorization") String token);
}
