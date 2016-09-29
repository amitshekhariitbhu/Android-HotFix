package com.hotfix

import com.github.stephanenicolas.morpheus.AbstractMorpheusPlugin
import javassist.build.IClassTransformer
import org.gradle.api.Project

public class HotFixPlugin extends AbstractMorpheusPlugin {

    @Override
    public IClassTransformer[] getTransformers(Project project) {
        return new HotfixProcessor(project.hotfix.debug);
    }

    @Override
    protected void configure(Project project) {

    }

    @Override
    protected Class getPluginExtension() {
        HotFixPluginExtension
    }

    @Override
    protected String getExtension() {
        "hotfix"
    }

    @Override
    public boolean skipVariant(def variant) {
        return variant.name.contains('release')
    }
}
