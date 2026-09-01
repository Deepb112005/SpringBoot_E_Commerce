package com.ecommerce.sbecom.service.impl;

import com.ecommerce.sbecom.exceptions.customExceptions.APIException;
import com.ecommerce.sbecom.exceptions.customExceptions.ResourceNotFoundException;
import com.ecommerce.sbecom.model.Cart;
import com.ecommerce.sbecom.model.CartItem;
import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.CartDTO;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.repository.CartItemRepository;
import com.ecommerce.sbecom.repository.CartRepository;
import com.ecommerce.sbecom.repository.ProductRepository;
import com.ecommerce.sbecom.service.CartService;
import com.ecommerce.sbecom.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;


    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                productId, cart.getCartId()
        );

        if (cartItem != null) {
            throw new APIException("product " + product.getProductName() + " already exist in cart !");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available !");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("please make an order of the " + product.getProductName()
                    + "less than or equal to the quantity");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> cartDTOS = cartRepository.findAll();

        if (cartDTOS.isEmpty()) {
            throw new APIException("No Cart Exists !");
        }


        List<CartDTO> cartDTOList = cartDTOS.stream().map(item -> {
            CartDTO cartDTO = modelMapper.map(item, CartDTO.class);
            List<ProductDTO> productDTOS = item.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;
        }).toList();

        return cartDTOList;

    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "CartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(cartItem -> {
                    ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                    productDTO.setQuantity(cartItem.getQuantity());
                    return productDTO;
                }).toList();

        cartDTO.setProducts(productDTOS);

        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }
}
