package org.wf.data.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    // "userinfo" version
    @JsonProperty("sub")
    private String sub;
    @JsonProperty("@odata.context")
    private String odataContext;
    @JsonProperty("givenname")
    private String givenname;
    @JsonProperty("familyname")
    private String familyname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("locale")
    private String locale;
    @JsonProperty("picture")
    private String picture;

    // "me" version
    @JsonProperty("userPrincipalName")
    private String userPrincipalName;
    @JsonProperty("id")
    private String id;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("givenName")
    private String givenName;
    @JsonProperty("familyName")
    private String familyName;
    @JsonProperty("preferredLanguage")
    private String preferredLanguage;
    @JsonProperty("mail")
    private String mail;
    @JsonProperty("mobilePhone")
    private String mobilePhone;
    @JsonProperty("jobTitle")
    private String jobTitle;
    @JsonProperty("officeLocation")
    private String officeLocation;
    @JsonProperty("businessPhones")
    private String[] businessPhones;
}
