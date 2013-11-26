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

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark Heckler <mark.heckler@gmail.com>
 * Twitter: @MkHeck
 *
 *
 */
public class FacebookFeed extends Feed {

    Facebook fbInstance;
    ResponseList<Post> messages;

    public FacebookFeed() {
        this("java");
    }

    public FacebookFeed(String searchTerm) {
        //this.setConsumer("Put Consumer Key Here", "Put Consumer Secret Here");
        //this.setToken("Put Token Here");

        this.fbInstance = FacebookFactory.getSingleton();

        fbInstance.setOAuthAppId(this.getConsumerKey(), this.getConsumerSecret());
        fbInstance.setOAuthAccessToken(new AccessToken(this.getToken()));

        try {
            messages = fbInstance.searchPosts(searchTerm);
            System.out.println(messages.size() + " messages retrieved.");
        } catch (FacebookException ex) {
            Logger.getLogger(FacebookFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();

        if (messages.size() > 0) {
            msg = popMessage();
            System.out.print(msg);
            if (isJunkMessage(msg)) {
                // For now, we revert to the default message.
                System.out.println(" *** JUNK *** ");
                msg = "SPAM message with multiple URLs purged. Carry on!";
            } else {
                System.out.println();
            }
            msg = msg.replaceAll("\n", " ");
        }

        return msg;
    }

    private String popMessage() {
        String msg = messages.get(0).getMessage();
        messages.remove(0);
        // Workaround for null message retrieval until Facebook4j library is replaced.
        return (msg == null ? "" : msg);
    }

    private boolean isJunkMessage(String msg) {
        boolean isJunk = false;
        int nLinks = 0;
        int nPos = 0;

        // If there are more than 5 URLs, it's FB spam...
        while ((nPos = msg.indexOf("http", nPos)) > -1) {
            if (++nLinks > 5) {
                isJunk = true;
                break;
            }
        }

        return isJunk;
    }
}
