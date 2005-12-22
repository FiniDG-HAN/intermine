package org.intermine.log;

import org.intermine.objectstore.ObjectStoreException;
import org.intermine.util.PropertiesUtil;

import java.util.Properties;
import java.lang.reflect.Method;

/*
 * Copyright (C) 2002-2005 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 * User: pmclaren
 * Date: 13-Dec-2005
 * Time: 10:16:44
 */

/**
 * Standard InterMine style factory that provides InterMineLogger instances.
 *
 * @author Peter McLaren
 */
public class InterMineLoggerFactory {

    public static InterMineLogger getInterMineLogger() throws Exception {

        return getInterMineLogger("logger.default");
    }

    public static InterMineLogger getInterMineLogger(String loggerAlias) throws Exception {

        if(loggerAlias == null || "".equals(loggerAlias)) {
            throw new Exception("InterMineLoggerFactory supplied an invalid alias! " + loggerAlias);
        }

        System.out.println("InterMineLoggerFactory.getInterMineLogger(.) called, loggerAlias:" + loggerAlias);

        Properties props = PropertiesUtil.getPropertiesStartingWith(loggerAlias);

        if (0 == props.size()) {
            throw new ObjectStoreException(
                    "No ObjectStore properties were found for alias '" + loggerAlias + "'");
        }

        props = PropertiesUtil.stripStart(loggerAlias, props);
        String clsName = props.getProperty("class");
        if (clsName == null) {
            throw new ObjectStoreException(loggerAlias
                    + " does not have an InterMineLogger class specified (check properties file)");
        }
        Class cls = null;

        String oswAlias = props.getProperty("osw");
        if (clsName == null) {
            throw new ObjectStoreException(loggerAlias
                    + " does not have an InterMineLogger osw specified (check properties file)");
        }

        try {
            cls = Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            throw new ObjectStoreException("Cannot find specified ObjectStore class '" + clsName
                    + "' for " + loggerAlias + " (check properties file)", e);
        }

        Class[] parameterTypes = new Class[] {String.class};
        Method m = cls.getDeclaredMethod("getInstance", parameterTypes);




        return (InterMineLogger) m.invoke(null, new Object[] {oswAlias});
    }

}



