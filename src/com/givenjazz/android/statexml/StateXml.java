
package com.givenjazz.android.statexml;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class StateXml {
    private static File[] mFiles;
    private static ConcurrentHashMap<String, ResourceState> mStateMap;

    public static void main(String[] args) {
        File srcDir = null;
        File destDir = null;
        if (args.length > 0) {
            srcDir = new File(args[0]);
        } else {
            srcDir = new File(".");
        }

        if (args.length > 1) {
            destDir = new File(args[1]);
        } else {
            destDir = new File(".");
        }

        Collection<ResourceState> states = createStates(srcDir);
        int total = 0;
        for (ResourceState state : states) {
            if (state.createXml(destDir)) {
                total++;
            }
        }
        System.out.println(total + " files was created.");
    }

    private static Collection<ResourceState> createStates(File directory) {
        mFiles = directory.listFiles();
        mStateMap = new ConcurrentHashMap<String, ResourceState>();

        for (File file : mFiles) {
            final String resourceName = getResourceName(file.getName());
            putPressedState(resourceName);
            putDisabledState(resourceName);
            putCheckedState(resourceName);
        }

        return mStateMap.values();
    }

    private static void putPressedState(final String resourceName) {
        for (String suffix : ResourceState.pressedSuffix) {
            if (resourceName.endsWith(suffix)) {
                String prefixName = resourceName.substring(0, resourceName.lastIndexOf(suffix));
                String normalName = getNormalName(mFiles, prefixName);
                if (mStateMap.get(prefixName) == null) {
                    mStateMap.put(prefixName, new ResourceState(prefixName, normalName));
                }
                mStateMap.get(prefixName).pressed = resourceName;
            }
        }
    }

    /**
     * If there is no pressed resource, set a checked resource to the press
     * resource.
     * 
     * @param resourceName
     */
    private static void putCheckedState(final String resourceName) {
        for (String suffix : ResourceState.checkedSuffix) {
            if (resourceName.endsWith(suffix)) {
                String prefixName = resourceName.substring(0, resourceName.lastIndexOf(suffix));
                String normalName = getNormalName(mFiles, prefixName);
                if (mStateMap.get(prefixName) == null) {
                    mStateMap.put(prefixName, new ResourceState(prefixName, normalName));
                }
                mStateMap.get(prefixName).checked = resourceName;
                if (mStateMap.get(prefixName).pressed == null) {
                    mStateMap.get(prefixName).pressed = resourceName;
                }
            }
        }
    }

    private static void putDisabledState(final String resourceName) {
        for (String suffix : ResourceState.disabledSuffix) {
            if (resourceName.endsWith(suffix)) {
                String prefixName = resourceName.substring(0, resourceName.lastIndexOf(suffix));
                String normalName = getNormalName(mFiles, prefixName);
                if (mStateMap.get(prefixName) == null) {
                    mStateMap.put(prefixName, new ResourceState(prefixName, normalName));
                }
                mStateMap.get(prefixName).disabled = resourceName;
            }
        }
    }

    private static String getNormalName(File[] files, String prefixName) {
        for (File file : files) {
            for (String suffix : ResourceState.normalSuffix) {
                for (String extention : ResourceState.extentionSuffix) {
                    if (file.getName().equals(prefixName + suffix + extention)) {
                        return prefixName + suffix;
                    }
                }
            }
        }
        System.out.println("cant find " + prefixName);
        System.exit(0);
        return null;
    }

    private static String getResourceName(String filename) {
        for (String extention : ResourceState.extentionSuffix) {
            if (filename.lastIndexOf(extention) > 0) {
                return filename.substring(0, filename.lastIndexOf(extention));
            }
        }
        return filename;
    }
}
