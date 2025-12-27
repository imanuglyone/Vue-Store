package com.vuestore;

import com.vuestore.entity.Product;
import com.vuestore.entity.Role;
import com.vuestore.entity.User;
import com.vuestore.repository.ProductRepository;
import com.vuestore.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VueStoreApplication {
  public static void main(String[] args) {
    SpringApplication.run(VueStoreApplication.class, args);
  }

  @Bean
  CommandLineRunner seedData(ProductRepository productRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
    return args -> {
      if (productRepository.count() < 10) {
        Product laptop = new Product();
        laptop.setName("Aurum Laptop 15");
        laptop.setDescription("Lightweight aluminum body, 2.8K display, long battery life.");
        laptop.setPrice(new BigDecimal("125000"));
        laptop.setImageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?q=80&w=800&auto=format&fit=crop");
        laptop.setCategory("Electronics");
        laptop.setStock(8);
        laptop.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, laptop);

        Product phone = new Product();
        phone.setName("Lumiere X Phone");
        phone.setDescription("108MP camera, fast chipset, sapphire-coated glass.");
        phone.setPrice(new BigDecimal("89000"));
        phone.setImageUrl("https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=800&auto=format&fit=crop");
        phone.setCategory("Electronics");
        phone.setStock(12);
        phone.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, phone);

        Product headphones = new Product();
        headphones.setName("Noire Headphones");
        headphones.setDescription("Active noise cancellation and premium materials.");
        headphones.setPrice(new BigDecimal("24000"));
        headphones.setImageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=800&auto=format&fit=crop");
        headphones.setCategory("Accessories");
        headphones.setStock(20);
        headphones.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, headphones);

        Product watch = new Product();
        watch.setName("Helios Watch");
        watch.setDescription("Smartwatch with sapphire glass and 14-day battery.");
        watch.setPrice(new BigDecimal("32000"));
        watch.setImageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=800&auto=format&fit=crop");
        watch.setCategory("Accessories");
        watch.setStock(14);
        watch.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, watch);

        Product speaker = new Product();
        speaker.setName("Orion Speaker");
        speaker.setDescription("Wireless premium speaker with room-filling sound.");
        speaker.setPrice(new BigDecimal("18000"));
        speaker.setImageUrl("https://impult.ru/preview/r/456x456/upload/iblock/f63/f630c4861736ade8c1c719fb4e19b4b5.jpg");
        speaker.setCategory("Electronics");
        speaker.setStock(18);
        speaker.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, speaker);

        Product camera = new Product();
        camera.setName("Nova Camera");
        camera.setDescription("Mirrorless camera with 4K video and fast autofocus.");
        camera.setPrice(new BigDecimal("99000"));
        camera.setImageUrl("https://images.unsplash.com/photo-1510127034890-ba27508e9f1c?q=80&w=800&auto=format&fit=crop");
        camera.setCategory("Electronics");
        camera.setStock(6);
        camera.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, camera);

        Product chair = new Product();
        chair.setName("Nord Chair");
        chair.setDescription("Ergonomic lounge chair with premium upholstery.");
        chair.setPrice(new BigDecimal("45000"));
        chair.setImageUrl("https://images.unsplash.com/photo-1501045661006-fcebe0257c3f?q=80&w=800&auto=format&fit=crop");
        chair.setCategory("Home");
        chair.setStock(10);
        chair.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, chair);

        Product lamp = new Product();
        lamp.setName("Aura Lamp");
        lamp.setDescription("Ambient lighting with adjustable warmth and brightness.");
        lamp.setPrice(new BigDecimal("12000"));
        lamp.setImageUrl("https://m.media-amazon.com/images/I/81j2S+6u5WL.jpg");
        lamp.setCategory("Home");
        lamp.setStock(22);
        lamp.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, lamp);

        Product jacket = new Product();
        jacket.setName("Sable Jacket");
        jacket.setDescription("Minimalist jacket in premium fabric for city style.");
        jacket.setPrice(new BigDecimal("38000"));
        jacket.setImageUrl("https://cdn1.ozone.ru/s3/multimedia-1-o/7561361400.jpg");
        jacket.setCategory("Fashion");
        jacket.setStock(9);
        jacket.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, jacket);

        Product bag = new Product();
        bag.setName("Aria Bag");
        bag.setDescription("Leather bag designed for daily essentials.");
        bag.setPrice(new BigDecimal("26000"));
        bag.setImageUrl("https://images.unsplash.com/photo-1524498250077-390f9e378fc0?q=80&w=800&auto=format&fit=crop");
        bag.setCategory("Fashion");
        bag.setStock(16);
        bag.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, bag);

        Product fragrance = new Product();
        fragrance.setName("Luxe Fragrance");
        fragrance.setDescription("Signature fragrance with a warm and modern profile.");
        fragrance.setPrice(new BigDecimal("14000"));
        fragrance.setImageUrl("https://avatars.mds.yandex.net/i?id=0f40f19592b0a083fc07147b1e493763_l-5208259-images-thumbs&n=13");
        fragrance.setCategory("Lifestyle");
        fragrance.setStock(30);
        fragrance.setCreatedAt(LocalDateTime.now());
        upsertProduct(productRepository, fragrance);
      }

      if (userRepository.count() == 0) {
        User admin = new User();
        admin.setEmail("admin@vue-store.local");
        admin.setName("Admin");
        admin.setRole(Role.ROLE_ADMIN);
        admin.setPassword(passwordEncoder.encode("admin123"));
        userRepository.save(admin);
      }
    };
  }

  private void upsertProduct(ProductRepository productRepository, Product seed) {
    var existing = productRepository.findAllByName(seed.getName());
    if (existing.isEmpty()) {
      productRepository.save(seed);
      return;
    }

    Product primary = existing.stream()
      .sorted((a, b) -> a.getId().compareTo(b.getId()))
      .findFirst()
      .orElse(null);

    if (primary == null) {
      productRepository.save(seed);
      return;
    }

    primary.setDescription(seed.getDescription());
    primary.setPrice(seed.getPrice());
    primary.setImageUrl(seed.getImageUrl());
    primary.setCategory(seed.getCategory());
    primary.setStock(seed.getStock());
    if (primary.getCreatedAt() == null) {
      primary.setCreatedAt(seed.getCreatedAt());
    }
    productRepository.save(primary);

    for (Product duplicate : existing) {
      if (!duplicate.getId().equals(primary.getId())) {
        productRepository.delete(duplicate);
      }
    }
  }
}
