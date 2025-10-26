package com.top.effitopia.controller;

import com.top.effitopia.dto.*;
import com.top.effitopia.enumeration.OutboundStatus;
import com.top.effitopia.service.DispatchService;
import com.top.effitopia.service.OutboundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/outbounds")
@Log4j2
@RequiredArgsConstructor
public class OutboundController {

    private final OutboundService outboundService;
    private final DispatchService dispatchService;

    @GetMapping("/register")
    public void showRegisterForm() {
        log.info("OutboundController showRegisterForm GetMapping");
    }

    @PostMapping("/register")
    public String registerForm(@Valid @ModelAttribute OrderDTO orderDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("OutboundController registerForm PostMapping");
        if (bindingResult.hasErrors()) {
            log.info("OutboundController registerForm PostMapping has Errors");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/outbounds/register";
        }
        outboundService.registerOrder(orderDTO);
        redirectAttributes.addFlashAttribute("message", "주문이 등록되었습니다.");
        return "redirect:/outbounds/list";
    }

//    @GetMapping("/list")
//    public void showList(PageRequestDTO pageRequestDTO, Model model) {
//        log.info("OutboundController showList GetMapping");
//        model.addAttribute("orders", outboundService.getOrders(pageRequestDTO));
//    }
    @GetMapping("/list")
    public void showList(PageRequestDTO pageRequestDTO, @ModelAttribute OutboundSearchDTO searchDTO, Model model) {
        log.info("OutboundController showList GetMapping with search: {}", searchDTO);
        pageRequestDTO.setSearchCond(searchDTO);
        PageResponseDTO<AllInOneDTO> pageResponseDTO = outboundService.getOrders(pageRequestDTO);
        model.addAttribute("orders", pageResponseDTO);
    }

    @GetMapping("/read/{id}")
    public String read(@PathVariable("id") Integer id, Model model) {
        log.info("OutboundController read GetMapping");
        Optional<DetailsDTO> detailsDTO = outboundService.getOrderDetails(id);
        if (detailsDTO.isPresent()) {
            log.info("OutboundController read GetMapping with id {}", id);
            model.addAttribute("orderDetails", detailsDTO.get());
            return "redirect:/outbounds/read/{id}";
        } else {
            model.addAttribute("message", "Order not found");
            return "redirect:/outbounds/list";
        }
    }


    @PostMapping("/read/{id}/updateOrder")
    public String updateOrder(@PathVariable("id") Integer id, @Valid @ModelAttribute OrderDTO orderDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("OutboundController updateOrder PostMapping");
        if (bindingResult.hasErrors()) {
            log.info("OutboundController updateOrder PostMapping has Errors");
            return "redirect:/outbounds/list";
        }
        log.info("OutboundController updateOrder PostMapping id {}", id);
        outboundService.updateOrder(id, orderDTO);
        redirectAttributes.addFlashAttribute("message", "주문 정보가 수정되었습니다.");
        return "redirect:/outbounds/read/{id}";
    }

    @PostMapping("/read/{id}/updateOrderStatus")
    public String updateOrderStatus(@PathVariable("id") Integer id, @RequestParam("status") OutboundStatus status, RedirectAttributes redirectAttributes) {
        log.info("OutboundController rejectOrder PostMapping");
        outboundService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("message", "Order status updated to " + status);
        return "redirect:/outbounds/read/{id}";
    }

    @PostMapping("/read/{id}/registerDispatch")
    public String registerDispatch(@Valid @ModelAttribute DispatchDTO dispatchDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("OutboundController registerDispatch PostMapping");
        if (bindingResult.hasErrors()) {
            log.info("OutboundController registerDispatch PostMapping BindingResult has Errors");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/outbounds/read/{id}";
        }

        boolean result = dispatchService.registerDispatch(dispatchDTO);
        if (!result) {
            log.info("OutboundController registerDispatch PostMapping Result has Errors");
            redirectAttributes.addFlashAttribute("errorMessage", "Dispatch 등록 실패");
            return "redirect:/outbounds/read/{id}";
        }

        redirectAttributes.addFlashAttribute("message", "Dispatch 등록 성공");
        return "redirect:/outbounds/read/{id}";
    }

    @PostMapping("/read/{id}/updateDispatch")
    public String updateDispatch(@Valid @ModelAttribute DispatchDTO dispatchDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("OutboundController updateDispatch PostMapping BindingResult has Errors");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/outbounds/read/{id}";
        }

        boolean result = dispatchService.updateDispatch(dispatchDTO);
        if (!result) {
            log.info("OutboundController updateDispatch PostMapping Result has Errors");
            redirectAttributes.addFlashAttribute("errorMessage", "Dispatch 수정 실패");
            return "redirect:/outbounds/read/{id}";
        }

        redirectAttributes.addFlashAttribute("message", "Dispatch 수정 성공");
        return "redirect:/outbounds/read/{id}";
    }

    @PostMapping("/read/{id}/deleteDispatch")
    public String deleteDispatch(@RequestParam Integer dispatchId, RedirectAttributes redirectAttributes) {
        boolean result = dispatchService.deleteDispatch(dispatchId);
        if (!result) {
            log.info("OutboundController deleteDispatch PostMapping Result has Errors");
            redirectAttributes.addFlashAttribute("errorMessage", "Dispatch 삭제 실패");
            return "redirect:/outbounds/read/{id}";
        }

        redirectAttributes.addFlashAttribute("message", "Dispatch 삭제 성공");
        return "redirect:/outbounds/read/{id}";
    }

    @PostMapping("/read/{id}/getAvailableVehicles")
    @ResponseBody  // JSON 형태로 반환
    public List<TransportVehicleDTO> getAvailableVehicles(@RequestParam Integer outboundId) {
        return dispatchService.getAvailableVehicles(outboundId);
    }
}
