package com.example.transactional.service;

import com.example.transactional.model.Order;
import com.example.transactional.model.User;
import com.example.transactional.model.enumeration.ProductType;
import com.example.transactional.repository.OrderRepository;
import com.example.transactional.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final EventService eventService;


    /**
     * Method with @Transactional parameter readOnly = true, to annotate that methods perform only reading data, not updating
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Method create user and order; Isolation.READ_UNCOMMITTED - meaning that changes made by one transaction can be read by other transactions before being committed.
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User addUser(User user) {
        addOrder();
        userRepository.save(user);
        return user;
    }

    /**
     * Method, which is annotated with @Transactional(Propagation.SUPPORTS) retrieves the user's information using the provided ID. If a transaction already exists, it participates in that transaction, but it doesn’t create a new transaction
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    /**
     * Method throws custom exception and rollback the creation of user and order
     */
    @Transactional(rollbackFor = CustomException.class)
    public User addUserThrowsCustomException(User user) throws CustomException {
        userRepository.save(user);
        throwCustomException();
        addOrder();
        return user;
    }

    private void throwCustomException() throws CustomException {
        throw new CustomException("Threw custom exception");
    }

    public void addOrder() {
        orderRepository.save(new Order("1", "test", ProductType.INDUSTRIAL_GOODS, 2));
    }

    /**
     * Method calls eventService.logEvent(), which is annotated with @Transactional(Propagation.NOT_SUPPORTED). It means that this method should not participate in any existing transaction. Therefore, it will execute as a separate, non-transactional operation.
     *
     * ISOLATION.READ_COMMITTED allowing other transactions to read only committed changes.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createOrder() {
        Order order = new Order("1", "test", ProductType.INDUSTRIAL_GOODS, 2);
        orderRepository.save(order);
        eventService.logEvent();
    }

    /**
     * Propagation.MANDATORY ensures that the method is executed within an existing transaction. If there is no existing transaction, a TransactionRequiredException will be thrown. Timeout ensures that the transaction takes place within a certain period of time(in second), otherwise it will rollback.
     */
    @Transactional(propagation = Propagation.MANDATORY, timeout = 30)
    public User updateUser(String id, User user) {
        User target = userRepository.findById(id).orElseThrow();
        target.setFirstName(user.getFirstName());
        target.setLastName(user.getLastName());
        target.setAge(user.getAge());
        return target;
    }
}
