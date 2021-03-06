package org.jboss.seam.security.external.saml.sp;

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.security.external.jaxb.samlv2.metadata.IDPSSODescriptorType;
import org.jboss.seam.security.external.saml.SamlExternalEntity;
import org.jboss.seam.security.external.saml.SamlProfile;
import org.jboss.seam.security.external.saml.SamlService;

/**
 * @author Marcel Kolsteren
 */
public class SamlExternalIdentityProvider extends SamlExternalEntity {
    private Map<SamlProfile, SamlService> services = new HashMap<SamlProfile, SamlService>();

    private boolean wantAuthnRequestsSigned;

    public SamlExternalIdentityProvider(String entityId, IDPSSODescriptorType iDPSSODescriptor) {
        super(entityId, iDPSSODescriptor.getKeyDescriptor());

        wantAuthnRequestsSigned = iDPSSODescriptor.getWantAuthnRequestsSigned();

        services.put(SamlProfile.SINGLE_SIGN_ON, new SamlService(SamlProfile.SINGLE_SIGN_ON, iDPSSODescriptor.getSingleSignOnService()));
        services.put(SamlProfile.SINGLE_LOGOUT, new SamlService(SamlProfile.SINGLE_LOGOUT, iDPSSODescriptor.getSingleLogoutService()));
    }

    public SamlService getService(SamlProfile service) {
        return services.get(service);
    }

    public boolean isWantAuthnRequestsSigned() {
        return wantAuthnRequestsSigned;
    }

    public void setWantAuthnRequestsSigned(boolean wantAuthnRequestsSigned) {
        this.wantAuthnRequestsSigned = wantAuthnRequestsSigned;
    }
}
