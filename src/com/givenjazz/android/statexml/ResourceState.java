
package com.givenjazz.android.statexml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ResourceState {

    final static String prefix = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">";
    final static String suffix = "\n</selector>";
    String name;
    String normal;
    String pressed;
    String focused;
    String selected;
    String checkabled;
    String checked;
    String disabled;

    ResourceState(String name, String normal) {
        this.name = name;
        this.normal = normal;
    }

    static String[] extentionSuffix = new String[] {
            ".9.png", ".png", ".jpg", ".gif"
    };

    static String[] normalSuffix = new String[] {
            "", "_normal", "_off"
    };

    static String[] pressedSuffix = new String[] {
            "_pressed", "_press"
    };

    static String[] disabledSuffix = new String[] {
            "_disabled", "_disable"
    };

    static String[] checkedSuffix = new String[] {
            "_on", "_checked"
    };

    /**
     * @param dest destination directory.
     * @return if a file was created, true. otherwise, false.
     */
    public boolean createXml(File dest) {
        File file = new File(dest, name + "_state.xml");
        if (file.exists()) {
            System.out.println(name + " exists.");
            return false;
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.append(prefix);
            if (pressed != null) {
                writer.append("\n\t<item android:drawable=\"@drawable/" + pressed
                        + "\" android:state_pressed=\"true\"/>");
            }
            if (disabled != null) {
                writer.append("\n\t<item android:drawable=\"@drawable/" + disabled
                        + "\" android:state_enabled=\"false\"/>");
            }
            if (checked != null) {
                writer.append("\n\t<item android:drawable=\"@drawable/" + checked
                        + "\" android:state_checked=\"true\"/>");
            }
            writer.append("\n\t<item android:drawable=\"@drawable/" + normal + "\" />");
            writer.append(suffix);
            System.out.println(name + " was created.");
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error");
                    e.printStackTrace();
                }
        }
        return true;
    }
}
