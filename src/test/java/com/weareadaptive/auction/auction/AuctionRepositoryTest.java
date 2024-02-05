package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.bid.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.weareadaptive.auction.TestData.AUCTION1;
import static com.weareadaptive.auction.TestData.AUCTION2;
import static com.weareadaptive.auction.TestData.AUCTION3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuctionRepositoryTest {
    private static AuctionRepository repository;

    @BeforeEach
    public void beforeEach() {
        repository = new AuctionRepository();
        repository.add(AUCTION1);
        repository.add(AUCTION3);
    }

    @Test
    @DisplayName("getUserAuctions returns a list of all a user's auctions")
    public void getUsersAuctions() {
        final var allAuctions = repository.getUserAuctions(1);

        assertEquals(1, allAuctions.count());
    }

    @Test
    @DisplayName("getAvailableAuctions returns a list of all the auctions a user can bid on")
    public void getAvailableAuctions() {
        final var availableAuctions = repository.getAvailableAuctions(1);

        assertEquals(1, availableAuctions.count());
    }

    @Test
    @DisplayName("hasAuction returns true if auction exists in state")
    public void hasAuction() {
        assertTrue(repository.hasAuction(0));
    }

    @Test
    @DisplayName("hasAuction returns false if auction does not exist in state")
    public void hasAuctionReturnsFalseIfAuctionDoesntExist() {
        assertFalse(repository.hasAuction(3));
    }

    @Test
    @DisplayName("getAuctionsUserBidOn returns all the auctions the user's bid on")
    public void getAuctionsUserBidOn() {
        final var bid01 = new Bid(1, 2, 12, 10, Instant.now());
        final var bid02 = new Bid(1, 2, 12, 10, Instant.now());

        repository.add(AUCTION2);

        AUCTION1.placeABid(bid01);
        AUCTION2.placeABid(bid02);

        AUCTION1.close();
        AUCTION2.close();

        final var auctions = repository.getAuctionsUserBidOn(2).toList();

        assertEquals(2, auctions.size());

        assertTrue(auctions.get(0).getBids().anyMatch(b -> b.equals(bid01)));
        assertTrue(auctions.get(1).getBids().anyMatch(b -> b.equals(bid02)));
    }

    @Test
    @DisplayName("getAllAuctions returns all the auctions excluding the requester's")
    void getAllAuctions() {
        assertEquals(1, repository.getAllAuctions(3).count());
        assertTrue(repository.getAllAuctions(3).allMatch(a -> a.getOwnerId() != 3));
    }

    @Test
    @DisplayName("getUserAuctions returns all the requester's auctions")
    void getUserAuctions() {
        assertEquals(1, repository.getUserAuctions(1).count());
        assertTrue(repository.getUserAuctions(1).allMatch(a -> a.getOwnerId() == 1));
    }

    @Test
    @DisplayName("getAuction returns the auction")
    void getAuction() {
        assertEquals(AUCTION1, repository.getAuction(0));
    }

    @Test
    @DisplayName("getAuction returns null if the auction doesn't exist")
    void getAuctionInvalidId() {
        assertEquals(null, repository.getAuction(-1));
    }
}
