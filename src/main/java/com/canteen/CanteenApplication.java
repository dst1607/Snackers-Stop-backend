package com.canteen;

import com.canteen.model.FoodItem;
import com.canteen.model.User;
import com.canteen.repository.FoodRepository;
import com.canteen.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CanteenApplication {

    private static final String ADMIN_EMAIL = "saitejadst05@gmail.com";
    private static final String ADMIN_PASSWORD = "saiteja";

    public static void main(String[] args) {
        SpringApplication.run(CanteenApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner seedData(
            FoodRepository foodRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
                User admin = new User();
                admin.setFullName("Sai Teja");
                admin.setEmail(ADMIN_EMAIL);
                admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }

            if (foodRepository.count() == 0) {
                foodRepository.save(new FoodItem("Veg Frankie", 50, "Frankie", ""));
                foodRepository.save(new FoodItem("Egg Frankie", 70, "Frankie", ""));
                foodRepository.save(new FoodItem("Chicken Frankie", 90, "Frankie", ""));
                foodRepository.save(new FoodItem("Tea", 10, "Beverages", ""));
                foodRepository.save(new FoodItem("Coffee", 15, "Beverages", ""));
                foodRepository.save(new FoodItem("Lime Juice", 20, "Beverages", ""));
                foodRepository.save(new FoodItem("Veg Biryani", 80, "Biryani", ""));
                foodRepository.save(new FoodItem("Chicken Biryani", 120, "Biryani", ""));
                foodRepository.save(new FoodItem("Egg Biryani", 100, "Biryani", ""));
                foodRepository.save(new FoodItem("Veg Maggi", 40, "Maggi", ""));
                foodRepository.save(new FoodItem("Egg Maggi", 50, "Maggi", ""));
                foodRepository.save(new FoodItem("Veg Sandwich", 40, "Sandwich", ""));
                foodRepository.save(new FoodItem("Egg Sandwich", 50, "Sandwich", ""));
                foodRepository.save(new FoodItem("Chicken Sandwich", 60, "Sandwich", ""));
                foodRepository.save(new FoodItem("Veg Burger", 40, "Burger", ""));
                foodRepository.save(new FoodItem("Chicken Burger", 60, "Burger", ""));
                foodRepository.save(new FoodItem("Veg Parota", 60, "Parota", ""));
                foodRepository.save(new FoodItem("Chicken Parota", 90, "Parota", ""));
                foodRepository.save(new FoodItem("Bonda", 30, "Tiffins", ""));
                foodRepository.save(new FoodItem("Idli", 30, "Tiffins", ""));
                foodRepository.save(new FoodItem("Dosa", 30, "Tiffins", ""));
                foodRepository.save(new FoodItem("Vada", 35, "Tiffins", ""));
                foodRepository.save(new FoodItem("Masala Dosa", 40, "Tiffins", ""));
                foodRepository.save(new FoodItem("Veg Meals", 60, "Meals", ""));
                foodRepository.save(new FoodItem("Non-Veg Meals", 80, "Meals", ""));
                foodRepository.save(new FoodItem("Egg Fried Rice", 70, "Fried Rice", ""));
                foodRepository.save(new FoodItem("Veg Fried Rice", 60, "Fried Rice", ""));
                foodRepository.save(new FoodItem("Chicken Fried Rice", 80, "Fried Rice", ""));
                foodRepository.save(new FoodItem("Veg Noodles", 60, "Noodles", ""));
                foodRepository.save(new FoodItem("Egg Noodles", 70, "Noodles", ""));
                foodRepository.save(new FoodItem("Chicken Noodles", 80, "Noodles", ""));
                foodRepository.save(new FoodItem("Chocolate Shake", 50, "Shakes", ""));
                foodRepository.save(new FoodItem("Vanilla Shake", 40, "Shakes", ""));
                foodRepository.save(new FoodItem("Mosambi Juice", 25, "Juices", ""));
                foodRepository.save(new FoodItem("Grape Juice", 25, "Juices", ""));
                foodRepository.save(new FoodItem("Papaya Juice", 25, "Juices", ""));
                foodRepository.save(new FoodItem("Mix Fruit Salad", 30, "Salads", ""));
            }
        };
    }
}