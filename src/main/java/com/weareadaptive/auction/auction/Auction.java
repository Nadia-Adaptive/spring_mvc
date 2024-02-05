package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.model.Entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.weareadaptive.auction.utils.StringUtil.isNullOrEmpty;

public class Auction implements Entity {
    private final List<Bid> bids;
    private final int id;
    private final String product;
    private final double minPrice;
    private final int quantity;
    private final int ownerId;
    private AuctionStatus auctionStatus;
    private Instant closingTime;
    private BigDecimal totalRevenue;


    private int totalQuantitySold;
    private final List<Bid> winningBids;
    private final List<Bid> losingBids;

    public Auction(final int id, final int ownerId, final String symbol, final double minPrice, final int quantity) {
        if (isNullOrEmpty(symbol)) {
            throw new BusinessException("Symbol cannot be empty!");
        }

        if (minPrice <= 0d) {
            throw new BusinessException("Price cannot be less than or equal to zero.");
        }
        if (quantity <= 0) {
            throw new BusinessException("Quantity cannot be less than or equal to zero.");
        }

        this.id = id;
        this.ownerId = ownerId;
        this.product = symbol;
        this.minPrice = minPrice;
        this.quantity = quantity;
        bids = new ArrayList<>();
        winningBids = new ArrayList<>();
        losingBids = new ArrayList<>();
        this.auctionStatus = AuctionStatus.OPEN;
        totalRevenue = new BigDecimal(0);
    }

    @Override
    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public String getProduct() {
        return product;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public AuctionStatus getStatus() {
        return auctionStatus;
    }

    public double getTotalRevenue() {
        return totalRevenue.doubleValue();
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public Stream<Bid> getBids() {
        return bids.stream();
    }

    public Stream<Bid> getWinningBids() {
        return winningBids.stream();
    }

    public void placeABid(final Bid bid) {
        if (auctionStatus == AuctionStatus.CLOSED) {
            throw new BusinessException("Can't bid on closed auction.");
        }

        if (bid.getPrice() < minPrice) {
            throw new BusinessException("Bid doesn't meet minimum price.");
        }

        if (bid.getBidderId() == ownerId) {
            throw new BusinessException("A seller cannot bid on their own auction.");

        }
        bids.add(bid);
    }

    public void close() {
        bids.stream().sorted(Comparator.reverseOrder()).forEach(bid -> {
            if (totalQuantitySold < quantity) {
                int fillQuantity = bid.getQuantity();
                totalQuantitySold += fillQuantity;

                if (totalQuantitySold > quantity) {
                    fillQuantity = bid.getQuantity() - (totalQuantitySold - quantity);
                    totalQuantitySold = quantity;
                }
                bid.fillBid(fillQuantity);

                totalRevenue =
                        totalRevenue.add(BigDecimal.valueOf(bid.getPrice()).multiply(BigDecimal.valueOf(fillQuantity)));
                winningBids.add(bid);
            } else {
                bid.fillBid(0);
                losingBids.add(bid);
            }
        });

        this.auctionStatus = AuctionStatus.CLOSED;
    }

    public Boolean hasUserBid(final int userId) {
        return bids.stream().anyMatch(b -> b.getBidderId() == userId);
    }


    public List<Bid> getLosingBids() {
        return losingBids;
    }
}
