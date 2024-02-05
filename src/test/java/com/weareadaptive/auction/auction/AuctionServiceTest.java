package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.bid.BidDTO;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.weareadaptive.auction.TestData.USER1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionServiceTest {
    AuctionRepository auctionRepository;
    AuctionService auctionService;
    UserRepository userRepository;


    AuctionServiceTest() {
        auctionRepository = new AuctionRepository();
        userRepository = mock(UserRepository.class);
        when(userRepository.getUserById(0)).thenReturn(USER1);
        when(userRepository.getUserById(1)).thenReturn(USER1);
        auctionService = new AuctionService(auctionRepository, userRepository, new BidRepository());
    }

    @AfterEach
    void afterEach() {
        auctionRepository = new AuctionRepository();
    }

    @Test
    @DisplayName("createAuction should not throw when adding new auction")
    void createAuction() {
        assertDoesNotThrow(() -> auctionService.createAuction(0, "product", 1.0, 10));
        assertTrue(auctionRepository.hasAuction(1));
    }

    @Test
    @DisplayName("createAuction should throw when passed a user that doesn't exist parameters")
    void createAuctionHasInvalidSeller() {
        assertThrows(BusinessException.class, () -> auctionService.createAuction(2, "product", 1.0, 10));
    }

    @Test
    @DisplayName("getAllAuctions should return all auctions excluding the requester's")
    void getAllAuctions() {
        auctionService.createAuction(0, "product", 1.0, 10);
        auctionService.createAuction(0, "product", 1.0, 10);
        auctionService.createAuction(1, "product", 1.0, 10);
        final var auctions = auctionService.getAllAuctions(1);
        assertEquals(2, auctions.size());
        assertTrue(auctions.stream().noneMatch((auction -> auction.getOwnerId() == 1)));
    }

    @Test
    @DisplayName("getAllAuctions should throw when passed a user that doesn't exist parameters")
    void getAllAuctionsUserDoesntExist() {
        assertThrows(NotFoundException.class, () -> auctionService.getAllAuctions(-1));
    }

    @Test
    @DisplayName("getAvailableAuctions should return all open auctions excluding the requester's")
    void getAvailableAuctions() {
        auctionService.createAuction(0, "product", 1.0, 10);
        auctionService.createAuction(0, "product", 1.0, 10);

        auctionRepository.getAuction(2).close();


        final var auctions = auctionService.getAvailableAuctions(1);
        assertEquals(1, auctions.size());
        assertTrue(auctions.stream().allMatch((auction -> auction.getStatus() == AuctionStatus.OPEN)));
    }

    @Test
    @DisplayName("getAvailableAuctions should return all open auctions excluding the requester's")
    void getAvailableAuctionsUserDoesntExist() {
        assertThrows(NotFoundException.class, () -> auctionService.getAvailableAuctions(-1));
    }

    @Test
    @DisplayName("getAuction returns an auction with a given id")
    void getAuction() {
        auctionService.createAuction(0, "product", 1.0, 10);
        assertEquals(0, auctionService.getAuction(1).getOwnerId());
    }

    @Test
    @DisplayName("getAuction returns null with the id is invalid")
    void getAuctionInvalidId() {
        assertThrows(NotFoundException.class, () -> auctionService.getAuction(-10));
    }

    @Test
    @DisplayName("makeABid does not throw when provided valid inputs")
    void makeABid() {
        auctionService.createAuction(0, "product", 1.0, 10);
        auctionService.makeABid(1, new BidDTO(1, 1, "product", 1.0, 10));
        assertEquals(1, auctionService.getAuction(1).getBids().count());
    }

    @Test
    @DisplayName("makeABid throws when provided an auction that doesn't exist")
    void makeABidOnNonExistentAuction() {
        auctionService.createAuction(0, "product", 1.0, 10);
        assertThrows(NotFoundException.class, () -> auctionService.makeABid(-10, new BidDTO(0, 1, "product", 0.1, 10)));
    }

    @Test
    @DisplayName("makeABid throws when provided a user that doesn't exist")
    void makeABidWithNonExistentUser() {
        auctionService.createAuction(0, "product", 1.0, 10);
        assertThrows(NotFoundException.class, () -> auctionService.makeABid(0, new BidDTO(0, -1, "product", 0.1, 10)));
    }

    @Test
    @DisplayName("closeAuction closes an auction")
    void closeAuction() {
        auctionService.createAuction(0, "product", 1.0, 10);
        auctionService.makeABid(1, new BidDTO(0, 1, "product", 1.1, 10));
        assertDoesNotThrow(() -> auctionService.closeAuction(1, 0));
        assertEquals(1, auctionService.getAuction(1).getWinningBids().count());
    }

    @Test
    @DisplayName("closeAuction throws if provided an invalid auction id")
    void closeAuctionProvidedInvalidId() {
        auctionService.createAuction(0, "product", 1.0, 10);

        assertThrows(NotFoundException.class, () -> auctionService.closeAuction(-1, 1));
    }

    @Test
    @DisplayName("closeAuction throws if user requesting close does not own the auction")
    void closeAuctionClosedByNonOwner() {
        auctionService.createAuction(0, "product", 1.0, 10);

        assertThrows(BusinessException.class, () -> auctionService.closeAuction(1, 1));
    }
}
