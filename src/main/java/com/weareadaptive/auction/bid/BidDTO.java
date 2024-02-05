package com.weareadaptive.auction.bid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record BidDTO(int id, int bidderId, String product, double offerPrice, int quantity) {

}
