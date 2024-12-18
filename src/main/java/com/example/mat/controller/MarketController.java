package com.example.mat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;
import com.example.mat.dto.market.ProductDto;
import com.example.mat.entity.market.Cart;
import com.example.mat.entity.market.Product;
import com.example.mat.service.CartService;
import com.example.mat.service.ProductService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Log4j2
@RequestMapping("/market")
@Controller
public class MarketController {

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

   

    @GetMapping("/list")
    public void getList(PageRequestDto requestDto, Model model) {
        log.info("식자재 전체 목록 요청");

        PageResultDto<ProductDto, Product> result = productService.getList(requestDto);
        model.addAttribute("result", result);

    }

    @GetMapping("/details")
    public void getRow(@RequestParam Long pid, Model model) {
        log.info("식자재 상세 페이지 요청 {}", pid);

        ProductDto dto = productService.getRow(pid);
        model.addAttribute("dto", dto);
    }



    @PostMapping("/cart")
        public @ResponseBody ResponseEntity cart(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {

            // 인증되지 않은 사용자의 요청 처리
        if (principal == null) {
        return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
         }

       if(bindingResult.hasErrors()){
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage());
        }

        return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
       }

       String email = principal.getName();
       Long cartItemId;

       try {
        cartItemId = cartService.addCart(cartItemDto, email);
       } catch (Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
       return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
        
}

       @GetMapping(value = "/cart")
    public void orderHist(Principal principal, Model model){
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailList);
     
    }



       

    @GetMapping("/order")
    public void getOrder() {
        log.info("order 페이지 요청");
    }

    @GetMapping("/orderlist")
    public void getOrderList() {
        log.info("orderlist 페이지 요청");
    }

}
