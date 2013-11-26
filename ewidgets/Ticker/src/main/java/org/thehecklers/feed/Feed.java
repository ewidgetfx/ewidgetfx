/*
 * Copyright 2013 eWidgetFX.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thehecklers.feed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Mark Heckler <mark.heckler@gmail.com>
 * Twitter: @MkHeck
 *
 *
 */
public class Feed {

    private String consumerKey = "";
    private String consumerSecret = "";
    private String token = "";
    private String tokenSecret = "";

    public Feed() {
    }

    public void setConsumer(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public void setToken(String token) {
        // For a single-element/combined token (e.g. Facebook's)
        this.token = token;
    }

    public void setToken(String token, String tokenSecret) {
        // For a dual-element token (e.g. Twitter's)
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public String getMessage() {
        // No real implementation in base class; implement in each subclass.
        return "Message unavailable. For sponsorship opportunities, please contact @MkHeck. :D";
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void setAccessToken(String token) {
        this.token = token;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    static protected String loadQuery() {
        Properties applicationProps = new Properties();
        FileInputStream in = null;
        File propFile = new File("Ticker.properties");
        String defaultQuery = "javafx OR javaone";

        if (!propFile.exists()) {
            // If it doesn't exist, create it.
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        try {
            in = new FileInputStream(propFile);
            applicationProps.load(in);
            in.close();

            if (applicationProps.containsKey("query")) {
                return applicationProps.getProperty("query", defaultQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // If we got to this point, we couldn't retrieve any useful parameters from the properties file.
        return defaultQuery;
    }
}
