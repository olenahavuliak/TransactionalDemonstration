package com.example.transactional.service;

import com.example.transactional.model.Event;
import com.example.transactional.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;

    /**
     * This method should not participate in any existing transaction.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void logEvent() {
        eventRepository.save(new Event("1", "Event was recorded."));
    }

    /**
     * Propagation.NEVER indicating that it should not participate in an existing transaction. If a transaction is already in progress when getEvent is called, an exception will be thrown.
     */
    @Transactional(propagation = Propagation.NEVER)
    public Event getEvent(String id) {
        return eventRepository.findById(id).orElseThrow();
    }

    /**
     Propagation.REQUIRES_NEW means that each of these methods will create its own transaction and commit or roll back independently of the other. Isolation.SERIALIZABLE - ensures complete isolation from other transactions, meaning that transactions are executed in a way that produces the same outcome as if they were executed serially.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void updateEvent(Event source){
        userService.createOrder();
        eventRepository.save(source);
    }
}
