package com.example.shoeMash;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
public class ShoeController {

    private final ShoeService shoeService;
    private final Random random = new Random(); // To pick a random shoe

    public ShoeController(ShoeService shoeService) {
        this.shoeService = shoeService;
    }

    // Display two random shoes for comparison
    @GetMapping("/")
    public String showShoes(Model model, 
                            @RequestParam(required = false) Long votedShoeId,
                            @RequestParam(required = false) String votedShoePosition) {

        List<Shoe> shoes = shoeService.getAllShoes();

        // Ensure there are at least 2 shoes
        if (shoes.size() >= 2) {
            Shoe shoe1;
            Shoe shoe2;

            if (votedShoeId != null) {
                // Get the voted shoe
                Shoe votedShoe = shoes.stream()
                        .filter(shoe -> shoe.getId().equals(votedShoeId))
                        .findFirst()
                        .orElse(shoes.get(0));  // Fallback to default if not found

                // Pick a new random shoe, ensuring it's not the same as the voted shoe
                Shoe randomShoe = getRandomShoeExcluding(shoes, votedShoeId);

                // Keep the voted shoe in its original position (left or right)
                if ("left".equals(votedShoePosition)) {
                    shoe1 = votedShoe;
                    shoe2 = randomShoe;  // Replace the right shoe
                } else {
                    shoe1 = randomShoe;  // Replace the left shoe
                    shoe2 = votedShoe;
                }
            } else {
                // Default two random shoes if no vote has been cast yet
                shoe1 = shoes.get(random.nextInt(shoes.size()));
                shoe2 = getRandomShoeExcluding(shoes, shoe1.getId());
            }

            model.addAttribute("shoe1", shoe1);
            model.addAttribute("shoe2", shoe2);
        }

        return "shoemash";  // Thymeleaf template for display
    }

    // Handle voting for a shoe
    @PostMapping("/vote")
    public String voteForShoe(@RequestParam("shoeId") Long shoeId,
                              @RequestParam("position") String position) {
        shoeService.voteForShoe(shoeId);
        // Redirect to retain the voted shoe and its position (left or right)
        return "redirect:/?votedShoeId=" + shoeId + "&votedShoePosition=" + position;
    }

    // Utility function to get a random shoe, excluding a specific shoe ID
    private Shoe getRandomShoeExcluding(List<Shoe> shoes, Long excludeShoeId) {
        Shoe randomShoe;
        do {
            randomShoe = shoes.get(random.nextInt(shoes.size()));
        } while (randomShoe.getId().equals(excludeShoeId));
        return randomShoe;
    }
}
