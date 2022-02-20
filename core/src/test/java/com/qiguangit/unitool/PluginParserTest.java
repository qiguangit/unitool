package com.qiguangit.unitool;

import com.qiguangit.unitool.plugin.PluginParser;
import com.qiguangit.unitool.plugin.model.PluginJarInfo;
import org.junit.Test;

import java.io.File;

public class PluginParserTest {
    @Test
    public void testPluginParser() {
        final File file = new File("F:\\test\\xJavaFxTool-0.2.3.jar");
        final PluginJarInfo jarInfo = PluginParser.parse(file);
        System.out.println(jarInfo);
    }
}
