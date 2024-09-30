package twitter;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of every
     *         tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        // If the list is empty, throw an exception (to match test expectation)
        if (tweets.isEmpty()) {
            throw new IllegalArgumentException("List of tweets cannot be empty.");
        }

        // Initialize the earliest and latest timestamps
        Instant earliest = tweets.get(0).getTimestamp();
        Instant latest = tweets.get(0).getTimestamp();

        // Iterate through the tweets to find the earliest and latest timestamps
        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            if (timestamp.isBefore(earliest)) {
                earliest = timestamp;
            }
            if (timestamp.isAfter(latest)) {
                latest = timestamp;
            }
        }

        // Return the timespan from earliest to latest tweet
        return new Timespan(earliest, latest);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets. A
     *         username-mention is "@" followed by a Twitter username (as defined by
     *         Tweet.getAuthor()'s spec). The username-mention cannot be immediately
     *         preceded or followed by any character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT
     *         contain a mention of the username mit. Twitter usernames are
     *         case-insensitive, and the returned set may include a username at most
     *         once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();

        // Regular expression to find valid mentions in the format '@username'
        Pattern pattern = Pattern.compile("(?<=\\s|^)@[a-zA-Z0-9_]+(?=\\s|$)");

        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            Matcher matcher = pattern.matcher(text);

            // Find all valid mentions and add them to the set
            while (matcher.find()) {
                // Extract the username, remove the '@', and convert to lowercase
                String username = matcher.group().substring(1).toLowerCase();
                mentionedUsers.add(username);
            }
        }

        return mentionedUsers;
    }
}
