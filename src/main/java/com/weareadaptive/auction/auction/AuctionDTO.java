package com.weareadaptive.auction.auction;

public record AuctionDTO(int ownerId, String product, double minPrice, int quantity) {
}
