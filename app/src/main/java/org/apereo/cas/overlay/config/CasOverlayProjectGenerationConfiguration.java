package org.apereo.cas.overlay.config;

import org.apereo.cas.initializr.contrib.ChainingSingleResourceProjectContributor;
import org.apereo.cas.overlay.buildsystem.CasOverlayBuildSystem;
import org.apereo.cas.overlay.buildsystem.CasOverlayGradleBuild;
import org.apereo.cas.overlay.contrib.CasOverlayAllReferencePropertiesContributor;
import org.apereo.cas.initializr.contrib.ApplicationYamlPropertiesContributor;
import org.apereo.cas.overlay.contrib.CasOverlayCasReferencePropertiesContributor;
import org.apereo.cas.overlay.contrib.CasOverlayConfigurationDirectoriesContributor;
import org.apereo.cas.overlay.contrib.CasOverlayConfigurationPropertiesContributor;
import org.apereo.cas.initializr.contrib.IgnoreRulesContributor;
import org.apereo.cas.overlay.contrib.CasOverlayLoggingConfigurationContributor;
import org.apereo.cas.overlay.contrib.CasOverlayOverrideConfigurationContributor;
import org.apereo.cas.initializr.contrib.ProjectLicenseContributor;
import org.apereo.cas.overlay.contrib.CasOverlayReadMeContributor;
import org.apereo.cas.overlay.contrib.CasOverlaySpringFactoriesContributor;
import org.apereo.cas.overlay.contrib.CasOverlayWebXmlContributor;
import org.apereo.cas.overlay.contrib.docker.CasOverlayDockerContributor;
import org.apereo.cas.overlay.contrib.docker.jib.CasOverlayGradleJibContributor;
import org.apereo.cas.overlay.contrib.docker.jib.CasOverlayGradleJibEntrypointContributor;
import org.apereo.cas.overlay.contrib.gradle.CasOverlayGradleBuildContributor;
import org.apereo.cas.overlay.contrib.gradle.CasOverlayGradlePropertiesContributor;
import org.apereo.cas.overlay.contrib.gradle.CasOverlayGradleSettingsContributor;
import org.apereo.cas.overlay.contrib.gradle.CasOverlayGradleSpringBootContributor;
import org.apereo.cas.overlay.contrib.gradle.CasOverlayGradleTasksContributor;
import org.apereo.cas.overlay.contrib.helm.CasOverlayHelmContributor;
import org.apereo.cas.overlay.customize.DefaultDependenciesBuildCustomizer;

import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import lombok.val;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.stream.Collectors;

@ProjectGenerationConfiguration
@ConditionalOnBuildSystem(CasOverlayBuildSystem.ID)
public class CasOverlayProjectGenerationConfiguration {
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    public CasOverlayDockerContributor casOverlayDockerContributor() {
        return new CasOverlayDockerContributor();
    }

    @Bean
    public CasOverlayHelmContributor casOverlayHelmContributor() {
        return new CasOverlayHelmContributor();
    }

    @Bean
    public ChainingSingleResourceProjectContributor casOverlayGradleConfigurationContributor() {
        var chain = new ChainingSingleResourceProjectContributor();
        chain.addContributor(new CasOverlayAllReferencePropertiesContributor(applicationContext));
        chain.addContributor(new CasOverlayCasReferencePropertiesContributor(applicationContext));
        chain.addContributor(new CasOverlayGradleBuildContributor(applicationContext));
        chain.addContributor(new CasOverlayGradlePropertiesContributor(applicationContext));
        chain.addContributor(new CasOverlayConfigurationDirectoriesContributor());
        chain.addContributor(new CasOverlayGradleSettingsContributor());
        chain.addContributor(new CasOverlayGradleSpringBootContributor());
        chain.addContributor(new CasOverlayGradleTasksContributor());
        chain.addContributor(new ApplicationYamlPropertiesContributor());
        chain.addContributor(new CasOverlayConfigurationPropertiesContributor(applicationContext));
        chain.addContributor(new CasOverlayOverrideConfigurationContributor());
        chain.addContributor(new CasOverlaySpringFactoriesContributor());
        chain.addContributor(new ProjectLicenseContributor());
        chain.addContributor(new CasOverlayWebXmlContributor());
        chain.addContributor(new CasOverlayLoggingConfigurationContributor());
        chain.addContributor(new CasOverlayReadMeContributor(applicationContext));
        chain.addContributor(new IgnoreRulesContributor());
        return chain;

    }

    @Bean
    public ChainingSingleResourceProjectContributor casOverlayJibConfigurationContributor() {
        var chain = new ChainingSingleResourceProjectContributor();
        chain.addContributor(new CasOverlayGradleJibContributor());
        chain.addContributor(new CasOverlayGradleJibEntrypointContributor());
        return chain;
    }

    @Bean
    public CasOverlayGradleBuild gradleBuild(ObjectProvider<BuildCustomizer<CasOverlayGradleBuild>> buildCustomizers,
                                             ObjectProvider<BuildItemResolver> buildItemResolver) {
        var build = new CasOverlayGradleBuild(buildItemResolver.getIfAvailable());
        val customizers = buildCustomizers.orderedStream().collect(Collectors.toList());
        customizers.forEach(c -> c.customize(build));
        return build;
    }

    @Bean
    public BuildCustomizer<CasOverlayGradleBuild> defaultDependenciesBuildCustomizer() {
        return new DefaultDependenciesBuildCustomizer(applicationContext);
    }
    
}