package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.bid.BidDTO;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuctionService {

    AuctionRepository auctionRepository;
    UserRepository userRepository;
    private BidRepository bidRepository;

    public AuctionService(final AuctionRepository auctionRepository, final UserRepository userRepository,
                          final BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
    }

    public void createAuction(final int seller, final String product, final double price, final int quantity) {
        if (userRepository.getUserById(seller) == null) {
            throw new BusinessException("User does not exist.");
        }
        auctionRepository.add(new Auction(auctionRepository.nextId(), seller, product, price, quantity));
    }

    public List<Auction> getAllAuctions(final int id) {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("User does not exist");
        }
        return auctionRepository.getAllAuctions(id).toList();
    }

    public List<Auction> getAvailableAuctions(final int id) {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("User does not exist");
        }
        return auctionRepository.getAvailableAuctions(id).toList();
    }

    public Auction getAuction(final int id) {
        final var auction = auctionRepository.getAuction(id);
        if (auction == null) {
            throw new NotFoundException("Auction does not exist");
        }
        return auction;
    }

    public void makeABid(final int auctionId, final BidDTO body) {
        final var auction = auctionRepository.getAuction(auctionId);
        if (auction == null) {
            throw new NotFoundException("Auction does not exist");
        }
        if (userRepository.getUserById(body.bidderId()) == null) {
            throw new NotFoundException("User does not exist");
        }
        final var bid = new Bid(bidRepository.nextId(), body.bidderId(), body.offerPrice(), body.quantity(),
                Instant.now()); // TODO: implement time provider with tests
        bidRepository.add(bid);
        auction.placeABid(bid);
    }

    public void closeAuction(final int id, final int requesterId) {
        final var auction = auctionRepository.getAuction(id);

        if (auction == null) {
            throw new NotFoundException("Auction not found.");
        }

        if (auction.getOwnerId() != requesterId) {
            throw new BusinessException("User does not own this resource.");
        }
        auction.close();
    }
}
