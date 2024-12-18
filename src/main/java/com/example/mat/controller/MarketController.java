package com.example.mat.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import com.example.mat.dto.shin.MemberDto;
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



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cart")
        public @ResponseBody ResponseEntity<String> cart(CartItemDto cartItemDto) {
            log.info("카트 추가 정보 {}",cartItemDto);          
        
      

       MemberDto memberDto = MemberDto.builder().mid(22L).build();
       Long cartItemId;

       try {
        cartItemId = cartService.addCart(cartItemDto,memberDto);
       } catch (Exception e) {
        return new ResponseEntity<String>("카트 추가 실패", HttpStatus.BAD_REQUEST);
       }
       return new ResponseEntity<String>(String.valueOf(cartItemId), HttpStatus.OK);
        
}

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/cart")
    public void orderHist(Model model){
        log.info("카트 목록");        


        String email = getAuthentication().getUsername();
        List<CartDetailDto> cartDetailList = cartService.getCartList(email);
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

   
    private User getAuthentication() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();


        User user = (User) authentication.getPrincipal();
        
        // MemberDto 에 들어있는 값 접근 시
        //AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        return user;
    }

}
