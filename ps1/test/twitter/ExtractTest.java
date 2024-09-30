package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

public class ExtractTest {

    /*
     * Testing strategy for getTimespan():
     * 
     * - Test an empty list of tweets
     * - Test one tweet
     * - Test multiple tweets with different timestamps
     * - Test multiple tweets with the same timestamp
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "Another tweet", d3);
    private static final Tweet tweet4 = new Tweet(4, "charlie", "Tweet with no mentions", d2);

    @Test
    public void testAssertionsEnabled() {
        assert false; // This should trigger an AssertionError
    }



    // Test case: Empty list of tweets
    @Test(expected = IllegalArgumentException.class)
    public void testGetTimespanEmptyList() {
        Extract.getTimespan(Collections.emptyList());
    }

    // Test case: One tweet
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    // Test case: Two tweets with different timestamps
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    // Test case: Multiple tweets with different timestamps
    @Test
    public void testGetTimespanMultipleTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    // Test case: Multiple tweets with the same timestamp
    @Test
    public void testGetTimespanSameTimestamp() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet4));
        assertEquals("expected start", d2, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    /*
     * Testing strategy for getMentionedUsers():
     * 
     * - No mentions
     * - One mention
     * - Multiple mentions
     * - Duplicate mentions (case-insensitive)
     * - Invalid mentions (e.g. part of email address)
     * - Case insensitivity in mentions
     */
    
    private static final Tweet tweetWithMention1 = new Tweet(5, "alice", "@bob how are you?", d1);
    private static final Tweet tweetWithMention2 = new Tweet(6, "bob", "Great talk with @alice and @charlie", d2);
    private static final Tweet tweetWithMention3 = new Tweet(7, "charlie", "No mention here", d3);
    private static final Tweet tweetWithDuplicateMention = new Tweet(8, "alice", "@BOB and @bob are the same person", d1);
    private static final Tweet tweetWithInvalidMention = new Tweet(9, "eve", "Contact me at eve@example.com", d1);

    // Test case: No mentions
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // Test case: One mention
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMention1));
        assertTrue("expected to contain bob", mentionedUsers.contains("bob"));
        assertEquals("expected size", 1, mentionedUsers.size());
    }

    // Test case: Multiple mentions
    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMention2));
        assertTrue("expected to contain alice", mentionedUsers.contains("alice"));
        assertTrue("expected to contain charlie", mentionedUsers.contains("charlie"));
        assertEquals("expected size", 2, mentionedUsers.size());
    }

    // Test case: Duplicate mentions (case-insensitive)
    @Test
    public void testGetMentionedUsersDuplicateMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithDuplicateMention));
        assertTrue("expected to contain bob", mentionedUsers.contains("bob"));
        assertEquals("expected size", 1, mentionedUsers.size());
    }

    // Test case: Invalid mention (email address)
    @Test
    public void testGetMentionedUsersInvalidMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithInvalidMention));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
}
