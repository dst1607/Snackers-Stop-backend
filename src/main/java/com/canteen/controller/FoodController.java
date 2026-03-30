package com.canteen.controller;

import com.canteen.model.FoodItem;
import com.canteen.repository.FoodRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foods")
@CrossOrigin(origins = "*")
public class FoodController {

    private final FoodRepository repo;

    public FoodController(FoodRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<FoodItem> getFoods() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public FoodItem getFoodById(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
    }

    @PostMapping
    public FoodItem addFood(@RequestBody FoodItem food) {
        return repo.save(food);
    }

    @PutMapping("/{id}")
    public FoodItem updateFood(@PathVariable Long id, @RequestBody FoodItem updatedFood) {
        FoodItem existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        existing.setName(updatedFood.getName());
        existing.setPrice(updatedFood.getPrice());
        existing.setCategory(updatedFood.getCategory());
        existing.setImageUrl(updatedFood.getImageUrl());

        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteFood(@PathVariable Long id) {
        repo.deleteById(id);
        return "Food item deleted successfully";
    }
}