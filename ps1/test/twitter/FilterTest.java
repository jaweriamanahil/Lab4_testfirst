package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "talk about rivest", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "30 minutes #hype", d3);

    // ========== Tests for writtenBy() ==========
    
    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> result = Filter.writtenBy(Collections.emptyList(), "alyssa");
        assertTrue("expected empty list when no tweets are provided", result.isEmpty());
    }

    @Test
    public void testWrittenByNoMatchingAuthor() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "unknownAuthor");
        assertTrue("expected empty list when no tweets match the author", result.isEmpty());
    }

    @Test
    public void testWrittenBySingleMatch() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        assertEquals("expected 1 tweet from 'alyssa'", 1, result.size());
        assertTrue("expected result to contain tweet1", result.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleMatches() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        assertEquals("expected 2 tweets from 'alyssa'", 2, result.size());
        assertTrue("expected result to contain tweet1 and tweet3", result.containsAll(Arrays.asList(tweet1, tweet3)));
    }

    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");
        assertEquals("expected 1 tweet from 'ALYSSA' in case-insensitive mode", 1, result.size());
        assertTrue("expected result to contain tweet1", result.contains(tweet1));
    }

    // ========== Tests for inTimespan() ==========

    @Test
    public void testInTimespanNoTweets() {
        Timespan timespan = new Timespan(d1, d3);
        List<Tweet> result = Filter.inTimespan(Collections.emptyList(), timespan);
        assertTrue("expected empty list when no tweets are provided", result.isEmpty());
    }

    @Test
    public void testInTimespanNoTweetsInRange() {
        Timespan timespan = new Timespan(Instant.parse("2016-02-17T13:00:00Z"), Instant.parse("2016-02-17T14:00:00Z"));
        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2), timespan);
        assertTrue("expected empty list when no tweets fall in the timespan", result.isEmpty());
    }

    @Test
    public void testInTimespanSomeTweetsInRange() {
        Timespan timespan = new Timespan(Instant.parse("2016-02-17T10:30:00Z"), Instant.parse("2016-02-17T12:30:00Z"));
        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), timespan);
        assertEquals("expected 2 tweets in timespan", 2, result.size());
        assertTrue("expected result to contain tweet2 and tweet3", result.containsAll(Arrays.asList(tweet2, tweet3)));
    }

    @Test
    public void testInTimespanBoundaryCases() {
        Timespan timespan = new Timespan(d1, d2);
        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2), timespan);
        assertEquals("expected 2 tweets on the boundary", 2, result.size());
        assertTrue("expected result to contain tweet1 and tweet2", result.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    // ========== Tests for containing() ==========

    @Test
    public void testContainingNoTweets() {
        List<Tweet> result = Filter.containing(Collections.emptyList(), Arrays.asList("talk"));
        assertTrue("expected empty list when no tweets are provided", result.isEmpty());
    }

    @Test
    public void testContainingNoMatchingWords() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("unknownWord"));
        assertTrue("expected empty list when no tweets contain the word", result.isEmpty());
    }

    @Test
    public void testContainingSomeMatchingWords() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("talk"));
        assertEquals("expected 2 tweets containing 'talk'", 2, result.size());
        assertTrue("expected result to contain tweet1 and tweet2", result.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testContainingMultipleWords() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("talk", "hype"));
        assertEquals("expected 3 tweets containing 'talk' or 'hype'", 3, result.size());
        assertTrue("expected result to contain all tweets", result.containsAll(Arrays.asList(tweet1, tweet2, tweet3)));
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("RIVEST"));
        assertEquals("expected 2 tweets containing 'RIVEST' in case-insensitive mode", 2, result.size());
        assertTrue("expected result to contain tweet1 and tweet2", result.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testContainingSpecialCharacters() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("#hype"));
        assertEquals("expected 1 tweet containing '#hype'", 1, result.size());
        assertTrue("expected result to contain tweet2", result.contains(tweet2));
    }
}

