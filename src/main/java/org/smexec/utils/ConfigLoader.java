/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {

    private static Logger logger = LoggerFactory.getLogger("ConfigLoader");

    private static final String PROPERTY_CONFIG_FILE = "smart.config.file.location";

    private static final String PROPERTY_CONFIG_RESOURCE_NAME = "smart.config.resource.name";

    private static final String defaultXMLConfName = "SmartExecutor-default.xml";

    private InputStream getResourceAsStream(String configXMLresource) {
        Thread.currentThread().getContextClassLoader();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        InputStream configXML = systemClassLoader.getResourceAsStream(configXMLresource);
        if (configXML == null) {
            logger.info("Config file wasn't found by systemClassLoader");
            ClassLoader contextClassLoader = getClass().getClassLoader();
            do {
                configXML = contextClassLoader.getResourceAsStream(configXMLresource);
                if (configXML == null) {
                    logger.info("Config file wasn't AsStream found by :{}", contextClassLoader);
                } else {
                    logger.info("Config file was AsStream found by :{}", contextClassLoader);
                }

                contextClassLoader = contextClassLoader.getParent();
            } while (configXML == null && contextClassLoader != null);
        }
        return configXML;
    }

    private InputStream getResource(String configXMLresource) {
        Thread.currentThread().getContextClassLoader();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        InputStream configXML = null;
        URL resource = systemClassLoader.getResource(configXMLresource);
        try {
            if (resource != null) {
                return resource.openStream();
            }

            logger.info("Config file wasn't found by systemClassLoader");
            ClassLoader contextClassLoader = getClass().getClassLoader();
            do {
                resource = contextClassLoader.getResource(configXMLresource);
                if (resource == null) {
                    logger.info("Config file wasn't found as reasorce by :{}", contextClassLoader);
                } else {
                    logger.info("Config file was found as reasorce by :{}", contextClassLoader);
                    configXML = resource.openStream();
                }

                contextClassLoader = contextClassLoader.getParent();
            } while (configXML == null && contextClassLoader != null && resource != null);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return configXML;
    }

    private InputStream getFromFile(String configXMLresource) {
        File f = new File(configXMLresource);
        if (!f.exists() && System.getProperty(PROPERTY_CONFIG_FILE) != null) {
            logger.info("Config file wasn't found as file.");
            f = new File(System.getProperty(PROPERTY_CONFIG_FILE));
        }

        if (!f.exists()) {
            logger.info("Configuration file wan't found:" + configXMLresource);
            return null;
        }
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param configXMLresource = can be any file name under current classLoader or full file name path or
     *            full resource name of XML file that is packed inside some JAR: <b>com/example/work/config.xml</b>
     * @return
     */
    public InputStream loadConfig(String configXMLresource) {
        if (configXMLresource == null && System.getProperty(PROPERTY_CONFIG_RESOURCE_NAME) == null) {
            // no name is provided
            configXMLresource = defaultXMLConfName;
        } else if (System.getProperty(PROPERTY_CONFIG_RESOURCE_NAME) != null) {
            // in case you're interested to provide a name of XML file that is located inside some JAR file
            // and it's not the default one used by smart-monitoring
            configXMLresource = System.getProperty(PROPERTY_CONFIG_RESOURCE_NAME);
        }

        logger.info("configXMLresource:={}", configXMLresource);

        InputStream configXML = getResourceAsStream(configXMLresource);
        if (configXML == null) {
            configXML = getResource(configXMLresource);
            if (configXML == null) {
                configXML = getFromFile(configXMLresource);
            }
        }
        return configXML;
    }
}
