package com.qiguangit.unitool.plugin;

import com.qiguangit.unitool.plugin.model.PluginJarInfo;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PluginParser {

    private static final String META_INF_PLUGIN_XML = "META-INF/plugin.xml";

    private PluginParser() {}

    public static PluginJarInfo parse(File file) {

        if (!file.exists()) {
            return null;
        }
        try (JarFile jarFile = new JarFile(file)) {
            final JarEntry jarEntry = jarFile.getJarEntry(META_INF_PLUGIN_XML);
            if (jarEntry == null) {
                return null;
            }

            try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                return parsePluginXml(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PluginJarInfo parsePluginXml(InputStream inputStream) throws Exception {

        SAXReader saxReader = new SAXReader();
        saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();

        String idText = selectSingleText(rootElement, "/plugin/id");
        String nameText = selectSingleText(rootElement, "/plugin/name");
        String descriptionText = selectSingleText(rootElement, "/plugin/description");
        String versionText = selectSingleText(rootElement, "/plugin/version");

        List<PluginJarInfo.FxConfiguration> fxConfigurations = new ArrayList<>();
        selectElements(rootElement, "/plugin/fx-configurations")
                .forEach(element -> {
                    String fxmlPath = selectSingleText(element, "fx-configuration/fxml-path");
                    String resourceBundleName = selectSingleText(element, "fx-configuration/resource-bundle-name");
                    String menuPath = selectSingleText(element, "fx-configuration/menu");
                    String title = selectSingleText(element, "fx-configuration/title");
                    String controllerType = selectSingleText(element, "fx-configuration/controller-type");
                    PluginJarInfo.FxConfiguration fxConfiguration =
                            PluginJarInfo.FxConfiguration.builder()
                            .fxmlPath(fxmlPath)
                            .resourceBundleName(resourceBundleName)
                            .menu(menuPath)
                            .title(title)
                            .controllerType(controllerType)
                            .build();
                    fxConfigurations.add(fxConfiguration);
                });
        return PluginJarInfo.builder()
                .id(idText)
                .name(nameText)
                .description(descriptionText)
                .version(versionText)
                .fxConfigurations(fxConfigurations)
                .build();
    }

    private static String selectSingleText(Element element, String xpath) {

        Element selectElement = selectSingleElement(element, xpath);
        return selectElement == null ? "" : selectElement.getTextTrim();
    }

    private static Element selectSingleElement(Element element, String xpath) {

        if (element == null || StringUtils.isBlank(xpath)) {
            return null;
        }
        List<Node> nodes = element.selectNodes(xpath);
        return nodes.stream()
                .filter(node -> node instanceof Element)
                .map(Element.class::cast)
                .findFirst()
                .orElse(null);
    }

    private static List<Element> selectElements(Element element, String xpath) {

        if (element == null || StringUtils.isBlank(xpath)) {
            return Collections.emptyList();
        }
        return element.selectNodes(xpath)
                .stream()
                .filter(node -> node instanceof Element)
                .map(Element.class::cast)
                .collect(Collectors.toList());
    }
}
