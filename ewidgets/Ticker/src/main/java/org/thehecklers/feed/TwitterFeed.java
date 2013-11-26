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

import twitter4j.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark Heckler <mark.heckler@gmail.com>
 * Twitter: @MkHeck
 *
 *
 */
public class TwitterFeed extends Feed {

    Twitter twInstance;
    QueryResult resultSet;
    List<Status> messages;

    public TwitterFeed() {
        this(Feed.loadQuery());
    }

    public TwitterFeed(String query) {
//        this.setConsumer("Put Consumer Key Here", "Put Consumer Secret Here");
//        this.setToken("Put Token Here", "Put Token Secret Here");

        this.twInstance = TwitterFactory.getSingleton();
//
//        twInstance.setOAuthConsumer(this.getConsumerKey(), this.getConsumerSecret());
//        twInstance.setOAuthAccessToken(new AccessToken(this.getToken(), this.getTokenSecret()));

        try {
            resultSet = twInstance.search(new Query(query));
            messages = resultSet.getTweets();
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();

        if (messages.size() > 0) {
            msg = popMessage();
        } else {
            // Refill the list!
            if (resultSet.hasNext()) {
                try {
                    resultSet = twInstance.search(resultSet.nextQuery());
                    messages = resultSet.getTweets();
                    msg = popMessage();
                } catch (TwitterException ex) {
                    Logger.getLogger(TwitterFeed.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return msg;
    }

    private String popMessage() {
        String msg = messages.get(0).getText();
        messages.remove(0);
        return msg;
    }
}
