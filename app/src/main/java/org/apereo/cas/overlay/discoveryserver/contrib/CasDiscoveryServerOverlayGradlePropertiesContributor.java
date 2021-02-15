package org.apereo.cas.overlay.discoveryserver.contrib;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apereo.cas.initializr.contrib.TemplatedProjectContributor;
import org.springframework.context.ApplicationContext;

@Slf4j
public class CasDiscoveryServerOverlayGradlePropertiesContributor extends TemplatedProjectContributor {
    public CasDiscoveryServerOverlayGradlePropertiesContributor(final ApplicationContext applicationContext) {
        super(applicationContext, "./gradle.properties", "classpath:discoveryserver-overlay/gradle.properties");
    }

    @Override
    protected Object contributeInternal(final ProjectDescription project) {
        val provider = applicationContext.getBean(InitializrMetadataProvider.class);
        val defaults = provider.get().defaults();
        defaults.put("casVersion", provider.get().getConfiguration().getEnv().getBoms().get("cas-bom").getVersion());
        return defaults;
    }
}
