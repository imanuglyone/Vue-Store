package com.vuestore.controller.admin;

import com.vuestore.dto.product.ProductRequest;
import com.vuestore.dto.product.ProductResponse;
import com.vuestore.service.AdminProductService;
import com.vuestore.service.FileStorageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminProductController {
  private final AdminProductService adminProductService;
  private final FileStorageService fileStorageService;

  public AdminProductController(AdminProductService adminProductService,
                                FileStorageService fileStorageService) {
    this.adminProductService = adminProductService;
    this.fileStorageService = fileStorageService;
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAll() {
    return ResponseEntity.ok(adminProductService.getAll());
  }

  @PostMapping
  public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(adminProductService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> update(@PathVariable("id") Long id,
                                                @Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(adminProductService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    adminProductService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/upload")
  public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
    String url = fileStorageService.store(file);
    return ResponseEntity.ok(Map.of("url", url));
  }
}
