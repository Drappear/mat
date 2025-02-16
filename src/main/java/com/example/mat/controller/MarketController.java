package com.example.mat.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.market.CartDetailDto;
import com.example.mat.dto.market.CartItemDto;
import com.example.mat.dto.market.OrderDto;
import com.example.mat.dto.market.ProductDto;
import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.entity.market.Order;
import com.example.mat.entity.market.Product;
import com.example.mat.service.CartService;
import com.example.mat.service.MemberService;
import com.example.mat.service.OrderService;
import com.example.mat.service.ProductService;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequestMapping("/market")
@Controller
public class MarketController {

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    MemberService memberService;

    @GetMapping("/list")
    public void getList(@RequestParam(required = false) Long cateid, PageRequestDto requestDto, Model model) {
        log.info("식자재 전체 목록 요청 카테고리 ID : {}, 페이지 번호 : {}", cateid, requestDto.getPage());

        PageResultDto<ProductDto, Product> result;

        if (cateid != null) {
            // 카테고리 ID 있는 경우
            result = productService.getProductsByCategory(cateid, requestDto);
            model.addAttribute("cateid", cateid);
        } else {
            // 전체 목록 조회(카테고리 ID 없는 경우)
            result = productService.getList(requestDto);
        }

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
    public String cart(@ModelAttribute CartItemDto cartItemDto) {
        log.info("카트 추가 정보 {}", cartItemDto);

        MemberDto memberDto = MemberDto.builder().mid(getAuthentication().getMemberDto().getMid()).build();
        cartService.addCart(cartItemDto, memberDto);
        return "redirect:cart";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cart")
    public void CartList(Model model) {
        log.info("카트 목록{}", model);

        Long mid = getAuthentication().getMemberDto().getMid();
        log.info("회원 ID", mid);

        List<CartDetailDto> cartDetailList = cartService.getCartList(mid);

        // System.out.println(cartDetailDtoList);
        for (CartDetailDto dto : cartDetailList) {
            System.out.println(dto.getItemName());
            System.out.println(dto.getPrice());
            System.out.println(dto.getQuantity());
            System.out.println(dto.getTotalPrice());
        }

        model.addAttribute("cartItems", cartDetailList);

    }

    @PostMapping("/remove")
    public String cartItemRemove(@RequestParam Long cartitemid) {
        log.info("카트 아이템 삭제 {}", cartitemid);

        cartService.deleteCartItem(cartitemid);

        return "redirect:/market/cart";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/order")
    public String orderPage(
            @RequestParam(value = "selectedCartItemIds", required = false) String selectedCartItemIds,
            Model model) {

        Long memberId = getAuthentication().getMemberDto().getMid();
        log.info("주문 페이지 요청 - 사용자 ID: {}", memberId);
        log.info("선택된 장바구니 상품 IDs: {}", selectedCartItemIds);

        // ✅ 사용자 정보 가져오기
        // MemberDto memberDto = memberService.getMemdberById(memberId);
        // model.addAttribute("member", memberDto); //

        if (selectedCartItemIds == null || selectedCartItemIds.isEmpty()) {
            model.addAttribute("error", "장바구니에서 상품을 선택해주세요.");
            return "redirect:/market/cart";
        }

        // 선택된 상품 ID를 리스트로 변환
        List<Long> cartItemIds = Arrays.stream(selectedCartItemIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // 체크된 상품만 조회
        List<CartDetailDto> cartItems = cartService.getSelectedCartItems(memberId, cartItemIds);

        if (cartItems.isEmpty()) {
            model.addAttribute("error", "선택된 장바구니 상품이 없습니다.");
            return "redirect:/market/cart";
        }

        // 총 주문 금액 계산 추가
        int totalOrderPrice = cartItems.stream()
                .mapToInt(item -> item.getQuantity() * item.getPrice())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalOrderPrice", totalOrderPrice); // 추가된 변수

        return "market/order";
    }

    // 장바구니에서 주문 생성
    // 현재 로그인한 사용자 정보를 기반으로 주문 생성
    // 주문 완료 후, 주문 ID 반환
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            @RequestParam("selectedCartItemIds") String selectedCartItemIds,
            @RequestParam("selectedQuantities") String selectedQuantities,
            @RequestParam("recipientName") String recipientName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("addr") String addr,
            @RequestParam("detailAddr") String detailAddr,
            @RequestParam("email") String email) {

        Long memberId = getAuthentication().getMemberDto().getMid();
        log.info("주문 생성 요청 - 멤버 ID: {}", memberId);

        try {
            List<Long> cartItemIds = Arrays.stream(selectedCartItemIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Integer> quantities = Arrays.stream(selectedQuantities.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (cartItemIds.isEmpty() || quantities.isEmpty() || cartItemIds.size() != quantities.size()) {
                return ResponseEntity.badRequest().body("선택된 상품 정보가 올바르지 않습니다.");
            }

            Long orderId = orderService.createOrder(memberId, cartItemIds, quantities, recipientName, phoneNumber,
                    zipcode, addr, detailAddr, email);
            log.info("주문 완료 - 주문 ID: {}", orderId);

            Order order = orderService.getOrderEntity(orderId);
            return ResponseEntity.ok(Map.of(
                    "orderId", orderId,
                    "orderUid", order.getOrderUid(),
                    "totalPrice", order.getPrice()));

        } catch (Exception e) {
            log.error("주문 생성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "주문 생성 실패", "message", e.getMessage()));
        }
    }

    // 주문 조회 (특정 주문 ID)
    // 주문 ID를 통해 주문 정보를 가져옴
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable Long orderId, Model model) {
        log.info("주문 조회 - 주문 ID: {}", orderId);

        try {
            OrderDto orderDto = orderService.getOrder(orderId);
            model.addAttribute("order", orderDto);
            return "orderDetails"; // 주문 상세 페이지

        } catch (Exception e) {
            log.error("주문 조회 실패: {}", e.getMessage());
            model.addAttribute("error", "주문 조회 중 오류가 발생했습니다.");
            return "orderList"; // 실패 시 주문 목록 페이지로 이동
        }
    }

    // 인증된 사용자 정보 가져오기(로그인한 사용자)
    private AuthMemberDto getAuthentication() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        // MemberDto 에 들어있는 값 접근 시
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        return authMemberDto;
    }

}
