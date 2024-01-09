package org.wf.data.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    // "userinfo" version
//    private String sub;
//    private String givenname;
//    private String familyname;
//    private String email;
//    private String locale;
//    private String picture;
    // other fields as needed...

    // "me" version
    @JsonProperty("@odata.context")
    private String odataContext;

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
