package com.weareadaptive.auction.response;

public record ResponseBody<T>(String message, T data) {
}
