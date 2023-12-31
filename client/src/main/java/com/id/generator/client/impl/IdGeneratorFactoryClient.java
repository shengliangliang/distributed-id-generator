package com.id.generator.client.impl;

import com.id.generator.client.config.TinyIdClientConfig;
import com.id.generator.client.utils.PropertiesLoader;
import com.id.generator.client.utils.TinyIdNumberUtils;
import com.id.generator.common.factory.AbstractIdGeneratorFactory;
import com.id.generator.common.generator.IdGenerator;
import com.id.generator.common.generator.impl.CachedIdGenerator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class IdGeneratorFactoryClient extends AbstractIdGeneratorFactory {

    private static final Logger logger = Logger.getLogger(IdGeneratorFactoryClient.class.getName());

    private static IdGeneratorFactoryClient idGeneratorFactoryClient;

    private static final String DEFAULT_PROP = "tinyid_client.properties";

    private static final int DEFAULT_TIME_OUT = 5000;

    private static String serverUrl = "http://{0}/tinyid/id/nextSegmentIdSimple?token={1}&bizType=";

    private IdGeneratorFactoryClient() {

    }

    public static IdGeneratorFactoryClient getInstance(String location) {
        if (idGeneratorFactoryClient == null) {
            synchronized (IdGeneratorFactoryClient.class) {
                if (idGeneratorFactoryClient == null) {
                    if (location == null || "".equals(location)) {
                        init(DEFAULT_PROP);
                    } else {
                        init(location);
                    }
                }
            }
        }
        return idGeneratorFactoryClient;
    }

    private static void init(String location) {
        idGeneratorFactoryClient = new IdGeneratorFactoryClient();
        Properties properties = PropertiesLoader.loadProperties(location);
        String tinyIdToken = properties.getProperty("tinyid.token");
        String tinyIdServer = properties.getProperty("tinyid.server");
        String readTimeout = properties.getProperty("tinyid.readTimeout");
        String connectTimeout = properties.getProperty("tinyid.connectTimeout");

        if (tinyIdToken == null || "".equals(tinyIdToken.trim())
                || tinyIdServer == null || "".equals(tinyIdServer.trim())) {
            throw new IllegalArgumentException("cannot find tinyid.token and tinyid.server config in:" + location);
        }

        TinyIdClientConfig tinyIdClientConfig = TinyIdClientConfig.getInstance();
        tinyIdClientConfig.setTinyIdServer(tinyIdServer);
        tinyIdClientConfig.setTinyIdToken(tinyIdToken);
        tinyIdClientConfig.setReadTimeout(TinyIdNumberUtils.toInt(readTimeout, DEFAULT_TIME_OUT));
        tinyIdClientConfig.setConnectTimeout(TinyIdNumberUtils.toInt(connectTimeout, DEFAULT_TIME_OUT));

        String[] tinyIdServers = tinyIdServer.split(",");
        List<String> serverList = new ArrayList<>(tinyIdServers.length);
        for (String server : tinyIdServers) {
            String url = MessageFormat.format(serverUrl, server, tinyIdToken);
            serverList.add(url);
        }
        logger.info("init tinyId client success url info:" + serverList);
        tinyIdClientConfig.setServerList(serverList);
    }

    @Override
    protected IdGenerator createIdGenerator(String bizType) {
        return new CachedIdGenerator(bizType, new HttpSegmentIdServiceImpl());
    }

}
