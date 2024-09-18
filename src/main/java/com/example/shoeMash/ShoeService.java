package com.example.shoeMash;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ShoeService {
    
    private final ShoeRepository shoeRepository;

    public ShoeService(ShoeRepository shoeRepository) {
        this.shoeRepository = shoeRepository;
    }

    // Load shoes from the image directory
    public void loadShoesFromImages(String imageDirectory) {
        File folder = new File(imageDirectory);
        File[] imageFiles = folder.listFiles();

        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                if (imageFile.isFile() && imageFile.getName().endsWith(".jpg")) {
                    // Generate shoe name from the file name (remove extension)
                    String shoeName = imageFile.getName().replace(".jpg", "");
                    String imageUrl = "/images/" + imageFile.getName();

                    // Check if the shoe already exists by name (to avoid duplicates)
                    if (!shoeRepository.existsByName(shoeName)) {
                        shoeRepository.save(new Shoe(shoeName, imageUrl));
                    }
                }
            }
        }
    }

    // Voting for a shoe by id
    public void voteForShoe(Long shoeId) {
        Shoe shoe = shoeRepository.findById(shoeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shoe ID: " + shoeId));
        shoe.incrementVotes();
        shoeRepository.save(shoe);
    }

    // Get all shoes (for random selection or comparison)
    public List<Shoe> getAllShoes() {
        return shoeRepository.findAll();
    }
}
