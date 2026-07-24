package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

public class BadgeTest {
	private Badge badge;
	
	@BeforeEach
	void setup() {
		badge=new Badge("A98765");
	}
	
	@Test
	void testGetBadgeId() {
		assertEquals("A98765",badge.getBadgeId());
	}
	
	@Test
	void testMultipleBadges() {
		Badge b2=new Badge("B12345");
		assertEquals("A98765",badge.getBadgeId());
		assertEquals("B12345",b2.getBadgeId());
	}
	
	@Test
	void testEmptyBadge() {
		Badge b3=new Badge("");
		assertEquals("",b3.getBadgeId());
	}
	
	@Test
	void nullBadge() {
		Badge b4=new Badge(null);
		assertEquals(null,b4.getBadgeId());
	}
	
	@Test
	void testDifferentMemoryBadge() {
		Badge b5=new Badge("A98765");
		assertNotSame(b5,badge);
		assertEquals(b5.getBadgeId(),badge.getBadgeId());
	}
	
	@Test
	void testCaseSensitive() {
		Badge b6=new Badge("a98765");
		assertNotEquals(b6,badge);
	}
	
	@Test
	void testBadgeCalling() {
		String call1=badge.getBadgeId();
		String call2=badge.getBadgeId();
		assertEquals(call1,call2);
	}
	
	@Test
	void testSpecialBadges() {
		Badge b7=new Badge("!@#$%");
		assertEquals("!@#$%",b7.getBadgeId());
	}
	
	@Test
	void testSpacesBadge() {
		Badge b8=new Badge(" A98765 ");
		assertNotEquals(b8,badge);
	}
	
	@Test
	void testSpaceBadge() {
		Badge b9=new Badge(" ");
		assertEquals(" ",b9.getBadgeId());
	}
}
