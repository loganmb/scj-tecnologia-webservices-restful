package fiap.scj.modulo1.application;

import static fiap.scj.modulo1.infrastructure.ProductServiceException.CREATE_OPERATION_ERROR;
import static fiap.scj.modulo1.infrastructure.ProductServiceException.DELETE_OPERATION_ERROR;
import static fiap.scj.modulo1.infrastructure.ProductServiceException.INVALID_PARAMETER_ERROR;
import static fiap.scj.modulo1.infrastructure.ProductServiceException.PRODUCT_NOT_FOUND_ERROR;
import static fiap.scj.modulo1.infrastructure.ProductServiceException.RETRIEVE_OPERATION_ERROR;
import static fiap.scj.modulo1.infrastructure.ProductServiceException.SEARCH_OPERATION_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fiap.scj.modulo1.domain.Product;
import fiap.scj.modulo1.domain.ProductDetail;
import fiap.scj.modulo1.domain.repository.ProductDetailRepository;
import fiap.scj.modulo1.infrastructure.ProductServiceException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailRepository repository;

    private final ObjectMapper objectMapper;

    @Autowired
    public ProductDetailServiceImpl(ProductDetailRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ProductDetail> search(String keyword) throws ProductServiceException {
        log.info("Searching products for keyword={}", keyword);
        try {
            List<ProductDetail> result = new ArrayList<>();

            if (keyword == null || keyword.isEmpty()) {
                log.debug("No keyword specified, listing all products");
                result.addAll(repository.findAll());
            } else {
                log.debug("Finding products by name or description");
                result.addAll(repository.findByNameOrDescriptionAllIgnoreCase(keyword, keyword));
            }

            return result;
        } catch (Exception e) {
            log.error("Error searching product", e);
            throw new ProductServiceException(SEARCH_OPERATION_ERROR, e);
        }
    }

    @Override
    public ProductDetail create(ProductDetail product) throws ProductServiceException {
        log.info("Creating product ({})", product);
        try {
            if (product == null) {
                log.error("Invalid product");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            ProductDetail result = repository.save(product);
            return result;
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw new ProductServiceException(CREATE_OPERATION_ERROR, e);
        }
    }

    @Override
    public ProductDetail retrieve(Long id) throws ProductServiceException {
        log.info("Retrieving product for id={}", id);
        try {
            if (id == null) {
                log.error("Invalid id");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            ProductDetail result = repository.findById(id).get();
            return result;
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw new ProductServiceException(RETRIEVE_OPERATION_ERROR, e);
        }
    }

    @Override
    public ProductDetail update(Long id, ProductDetail product) throws ProductServiceException {
        log.info("Updating product ({}) for id={}", product, id);
        try {
            if (id == null || product == null) {
                log.error("Invalid id or product");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            if (!repository.existsById(id)) {
                log.debug("Product not found for id={}", id);
                throw new ProductServiceException(PRODUCT_NOT_FOUND_ERROR, null);
            }
            ProductDetail result = repository.save(product);
            return result;
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw new ProductServiceException(RETRIEVE_OPERATION_ERROR, e);
        }
    }

    @Override
    public void delete(Long id) throws ProductServiceException {
        log.info("Deleting product for id={}", id);
        try {
            if (id == null) {
                log.error("Invalid id or product");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            if (!repository.existsById(id)) {
                log.debug("Product not found for id={}", id);
                throw new ProductServiceException(PRODUCT_NOT_FOUND_ERROR, null);
            }
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw new ProductServiceException(DELETE_OPERATION_ERROR, e);
        }
    }

}
