package com.weareadaptive.auction.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {
  public BusinessException(final String message) {
    super(message);
  }
}
