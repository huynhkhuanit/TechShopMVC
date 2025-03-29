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
                "https://i.ebayimg.com/images/g/5GQAAOSwNwlnXLcw/s-l960.webp", cpu);

        createProduct("AMD Ryzen 9 7950X", 18000000, "CPU hàng đầu cho gaming và công việc nặng.",
                "https://i.ebayimg.com/images/g/5GQAAOSwNwlnXLcw/s-l960.webp", cpu);

        createProduct("NVIDIA RTX 4090", 45000000, "Card đồ họa hàng đầu cho gaming 4K.",
                "https://ictc.edu.vn/wp-content/uploads/2024/04/image-36.png", gpu);

        createProduct("AMD Radeon RX 7900 XTX", 30000000, "GPU mạnh mẽ với giá hợp lý.",
                "https://ictc.edu.vn/wp-content/uploads/2024/04/image-36.png", gpu);

        createProduct("G.Skill Trident Z5 32GB", 4500000, "RAM tốc độ cao, dung lượng lớn.",
                "https://ictc.edu.vn/wp-content/uploads/2024/04/image-36.png", ram);

        createProduct("Corsair Vengeance 16GB", 2000000, "RAM ổn định cho mọi nhu cầu.",
                "https://product.hstatic.net/200000420363/product/ram-kingston-fury-beast-rgb-16gb-2_a998ff2f529243f3a2430cb2f4c8f3c3_master.jpg", ram);

        createProduct("Samsung 990 Pro 1TB SSD", 3500000, "Ổ SSD tốc độ cao, dung lượng lớn.",
                "https://www.newegg.com/insider/wp-content/uploads/2018/04/Intel-900P-SSD-9.jpg", storage);

        createProduct("WD Black 2TB HDD", 2000000, "Ổ cứng HDD dung lượng lớn, đáng tin cậy.",
                "https://www.newegg.com/insider/wp-content/uploads/2018/04/Intel-900P-SSD-9.jpg", storage);

        createProduct("Dell UltraSharp 27\" 4K", 12000000, "Màn hình 4K sắc nét, lý tưởng cho công việc.",
                "https://product.hstatic.net/200000309925/product/cellphones_d99c4ebca91e450fb087e4eda5331da1_master.png", monitor);

        createProduct("LG 32\" Gaming Monitor", 8000000, "Màn hình gaming 144Hz, độ phân giải QHD.",
                "https://product.hstatic.net/200000309925/product/cellphones_d99c4ebca91e450fb087e4eda5331da1_master.png", monitor);
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
        Product product = productRepository.findByName(name).orElse(null);
        
        if (product == null) {
            product = new Product();
            product.setName(name);
        }
    
        // Cập nhật nếu chưa có hoặc imageUrl đang null
        if (product.getImageUrl() == null || !product.getImageUrl().equals(imageUrl)) {
            product.setPrice(price);
            product.setDescription(description);
            product.setImageUrl(imageUrl);
            product.setCategory(category);
            productRepository.save(product);
        }
    }
}
