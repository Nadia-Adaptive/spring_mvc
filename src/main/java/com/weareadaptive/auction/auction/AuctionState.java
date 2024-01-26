package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.model.State;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AuctionState extends State<Auction> {
    public Stream<Auction> getUserAuctions(final String username) {
        return stream().filter(a -> a.getSeller().getUsername().equals(username));
    }

    public boolean hasAuction(final int id) {
        return stream().anyMatch(a -> a.getId() == id);
    }

    public Stream<Auction> getAvailableAuctions(final String currentUser) {
        return stream().filter(
                a -> !a.getSeller().getUsername().equalsIgnoreCase(currentUser) && a.getStatus() == AuctionStatus.OPEN);
    }

    public Stream<Auction> getAuctionsUserBidOn(final String username) {
        return stream().filter(a -> a.hasUserBid(username));
    }
}
