package com.canteen.controller;

import com.canteen.model.Order;
import com.canteen.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Order> getOrders(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role
    ) {
        String safeRole = role == null ? "USER" : role.trim().toUpperCase();

        if ("ADMIN".equals(safeRole)) {
            return repo.findAllByOrderByCreatedAtDesc();
        }

        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email is required for customer order view");
        }

        return repo.findByCustomerEmailOrderByCreatedAtDesc(email.trim().toLowerCase());
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("Pending");
        }

        if (order.getCustomerName() == null || order.getCustomerName().isBlank()) {
            order.setCustomerName("Student");
        }

        if (order.getCustomerEmail() == null || order.getCustomerEmail().isBlank()) {
            throw new RuntimeException("Customer email is required");
        }

        order.setCustomerEmail(order.getCustomerEmail().trim().toLowerCase());

        if (order.getPaymentMethod() == null || order.getPaymentMethod().isBlank()) {
            order.setPaymentMethod("UPI");
        }

        if (order.getOrderType() == null || order.getOrderType().isBlank()) {
            order.setOrderType("Takeaway");
        }

        if (order.getPickupSlot() == null || order.getPickupSlot().isBlank()) {
            order.setPickupSlot("12:30 PM - 12:45 PM");
        }

        if (order.getWaitingTime() <= 0) {
            order.setWaitingTime(10 + (order.getQuantity() * 4));
        }

        return repo.save(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existing.setFoodName(updatedOrder.getFoodName());
        existing.setQuantity(updatedOrder.getQuantity());
        existing.setTotalPrice(updatedOrder.getTotalPrice());
        existing.setStatus(updatedOrder.getStatus());
        existing.setCustomerName(updatedOrder.getCustomerName());
        existing.setCustomerEmail(
                updatedOrder.getCustomerEmail() == null
                        ? null
                        : updatedOrder.getCustomerEmail().trim().toLowerCase()
        );
        existing.setPaymentMethod(updatedOrder.getPaymentMethod());
        existing.setOrderType(updatedOrder.getOrderType());
        existing.setPickupSlot(updatedOrder.getPickupSlot());
        existing.setNotes(updatedOrder.getNotes());
        existing.setWaitingTime(updatedOrder.getWaitingTime());

        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        repo.deleteById(id);
        return "Order deleted successfully";
    }
}