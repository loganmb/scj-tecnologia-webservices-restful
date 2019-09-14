package fiap.scj.modulo1.application;

import java.io.Serializable;
import java.util.List;

import fiap.scj.modulo1.domain.ProductDetail;
import fiap.scj.modulo1.infrastructure.ProductServiceException;

public interface ProductDetailService extends Serializable {

    List<ProductDetail> search(String keyword) throws ProductServiceException;

    ProductDetail create(ProductDetail product) throws ProductServiceException;

    ProductDetail retrieve(Long id) throws ProductServiceException;

    ProductDetail update(Long id, ProductDetail product) throws ProductServiceException;

    void delete(Long id) throws ProductServiceException;
}
