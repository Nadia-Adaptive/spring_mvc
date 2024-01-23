package com.weareadaptive.auction.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
public class Serialiser {
    private final ObjectMapper mapper = new ObjectMapper();

    // TODO: This should not throw a BusinessException as any serialisation errors will be the result of faulty data,
    //  so the server's error (500 code) not the client (500)
    public String serialise(final Object o) throws BusinessException {
        try {
            return mapper.writeValueAsString(o);
        } catch (final JsonProcessingException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
