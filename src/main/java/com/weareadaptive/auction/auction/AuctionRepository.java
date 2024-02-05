package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.model.State;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AuctionRepository extends State<Auction> {
    public Stream<Auction> getUserAuctions(final int userId) {
        return stream().filter(a -> a.getOwnerId() == userId);
    }

    public boolean hasAuction(final int id) {
        return stream().anyMatch(a -> a.getId() == id);
    }

    public Stream<Auction> getAvailableAuctions(final int userId) {
        return stream().filter(
                a -> a.getOwnerId() != userId && a.getStatus() == AuctionStatus.OPEN);
    }

    public Stream<Auction> getAuctionsUserBidOn(final int userId) {
        return stream().filter(a -> a.hasUserBid(userId));
    }

    public Stream<Auction> getAllAuctions(final int id) {
        return stream().filter(a -> a.getOwnerId() != id);
    }

    public Auction getAuction(final int id) {
        return getEntity(id);
    }
}
