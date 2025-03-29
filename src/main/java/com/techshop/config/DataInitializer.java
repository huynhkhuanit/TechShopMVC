package com.techshop.config;

import com.techshop.model.Category;
import com.techshop.model.Product;
import com.techshop.repository.CategoryRepository;
import com.techshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        // Tạo danh mục (trả về đối tượng đã có hoặc mới tạo)
        Category cpu = createCategoryIfNotExists("CPU");
        Category gpu = createCategoryIfNotExists("GPU");
        Category ram = createCategoryIfNotExists("RAM");
        Category storage = createCategoryIfNotExists("Storage");
        Category monitor = createCategoryIfNotExists("Monitor");

        // Tạo sản phẩm cho từng danh mục
        createProduct("Intel Core i9-13900K", 15000000, "Bộ vi xử lý mạnh mẽ cho hiệu năng cao.",
                "https://images.unsplash.com/photo-1591799264318-5e96a5731371?q=80&w=2070&auto=format&fit=crop", cpu);

        createProduct("AMD Ryzen 9 7950X", 18000000, "CPU hàng đầu cho gaming và công việc nặng.",
                "https://images.unsplash.com/photo-1624704805041-4c0b11e0f75f?q=80&w=2070&auto=format&fit=crop", cpu);

        createProduct("NVIDIA RTX 4090", 45000000, "Card đồ họa hàng đầu cho gaming 4K.",
                "https://images.unsplash.com/photo-1597733336794-12d05021d510?q=80&w=2070&auto=format&fit=crop", gpu);

        createProduct("AMD Radeon RX 7900 XTX", 30000000, "GPU mạnh mẽ với giá hợp lý.",
                "https://images.unsplash.com/photo-1614624532983-8d66c3d5a4c8?q=80&w=2070&auto=format&fit=crop", gpu);

        createProduct("G.Skill Trident Z5 32GB", 4500000, "RAM tốc độ cao, dung lượng lớn.",
                "https://images.unsplash.com/photo-1589254066213-a0c9dc853511?q=80&w=2070&auto=format&fit=crop", ram);

        createProduct("Corsair Vengeance 16GB", 2000000, "RAM ổn định cho mọi nhu cầu.",
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?q=80&w=2070&auto=format&fit=crop", ram);

        createProduct("Samsung 990 Pro 1TB SSD", 3500000, "Ổ SSD tốc độ cao, dung lượng lớn.",
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?q=80&w=2070&auto=format&fit=crop", storage);

        createProduct("WD Black 2TB HDD", 2000000, "Ổ cứng HDD dung lượng lớn, đáng tin cậy.",
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?q=80&w=2070&auto=format&fit=crop", storage);

        createProduct("Dell UltraSharp 27\" 4K", 12000000, "Màn hình 4K sắc nét, lý tưởng cho công việc.",
                "https://images.unsplash.com/photo-1593642532973-d31b6557fa68?q=80&w=2070&auto=format&fit=crop", monitor);

        createProduct("LG 32\" Gaming Monitor", 8000000, "Màn hình gaming 144Hz, độ phân giải QHD.",
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?q=80&w=2070&auto=format&fit=crop", monitor);
    }

    private Category createCategoryIfNotExists(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    return categoryRepository.save(newCategory);
                });
    }

    private void createProduct(String name, int price, String description, String imageUrl, Category category) {
        if (!productRepository.existsByName(name)) {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setImageUrl(imageUrl);
            product.setCategory(category);
            productRepository.save(product);
        }
    }
}
