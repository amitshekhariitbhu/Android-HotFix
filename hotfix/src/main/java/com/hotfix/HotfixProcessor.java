package com.hotfix;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.build.IClassTransformer;
import javassist.build.JavassistBuildException;

import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isActivity;
import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isApplication;
import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isBroadCastReceiver;
import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isContentProvider;
import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isFragment;
import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isService;
import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isSupportFragment;
import static com.github.stephanenicolas.morpheus.commons.JavassistUtils.isView;

/**
 * Created by amitshekhar on 29/09/16.
 */

public class HotfixProcessor implements IClassTransformer {

    private boolean debug;

    public HotfixProcessor(boolean debug) {
        this.debug = debug;
    }

    @Override
    public boolean shouldTransform(CtClass candidateClass) throws JavassistBuildException {
        try {
            return isSupported(candidateClass);
        } catch (Exception e) {
            throw new JavassistBuildException(e);
        }
    }

    private boolean isSupported(CtClass candidateClass) throws NotFoundException {
        return isActivity(candidateClass)
                || isFragment(candidateClass)
                || isSupportFragment(candidateClass)
                || isView(candidateClass)
                || isService(candidateClass)
                || isBroadCastReceiver(candidateClass)
                || isContentProvider(candidateClass)
                || isApplication(candidateClass);
    }

    @Override
    public void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
        try {
            ClassPool pool = classToTransform.getClassPool();
            addLogAtStartAndEndOfDeclaredMethods(pool, classToTransform.getName());
        } catch (Exception e) {
            throw new JavassistBuildException(e);
        }
    }

    private void addLogAtStartAndEndOfDeclaredMethods(ClassPool pool, String className)
            throws NotFoundException {
        CtMethod[] declaredMethods = pool.get(className).getDeclaredMethods();
        for (CtMethod method : declaredMethods) {
            try {
                method.insertBefore("android.util.Log.d(\"Start Method Name : \",\""
                        + method.getName()
                        + "\");");
                method.insertAfter("android.util.Log.d(\"End Method Name : \",\""
                        + method.getName()
                        + "\");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

