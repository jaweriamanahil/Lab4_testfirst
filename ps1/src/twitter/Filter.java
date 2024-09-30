package twitter;

import java.util.List;
import java.util.stream.Collectors;

public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        return tweets.stream()
                     .filter(tweet -> tweet.getAuthor().equalsIgnoreCase(username))
                     .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        return tweets.stream()
                     .filter(tweet -> !tweet.getTimestamp().isBefore(timespan.getStart()) 
                                   && !tweet.getTimestamp().isAfter(timespan.getEnd()))
                     .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<String> lowerCaseWords = words.stream()
                                           .map(String::toLowerCase)
                                           .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
        return tweets.stream()
                     .filter(tweet -> {
                         String tweetText = tweet.getText().toLowerCase();
                         return lowerCaseWords.stream().anyMatch(tweetText::contains);
                     })
                     .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
    }

}
