package com.top.effitopia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.top.effitopia.dto.*;
import com.top.effitopia.service.AuthService;
import com.top.effitopia.service.ContractService;
import com.top.effitopia.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/contract")
@RequiredArgsConstructor
@Log4j2
public class ContractController {

    private final ContractService contractService;
    private final AuthService authService;
    private final WarehouseService warehouseService;

    @GetMapping("/list")
    public void getContractList(Model model, @Valid @ModelAttribute("pageRequestDTO") PageRequestDTO<WarehouseDTO> pageRequestDTO, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()){
            model.addAttribute("errorMessages", bindingResult.getAllErrors());
        }
        else {
            PageResponseDTO<WarehouseDTO> pageResponseDTO = warehouseService.getWarehouseList(pageRequestDTO);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonUtilizationList = objectMapper.writeValueAsString(pageResponseDTO.getDtoList());
            model.addAttribute("jsonUtilizationList", jsonUtilizationList);
            model.addAttribute("responseDTO", pageResponseDTO);
        }
    }

    @GetMapping("/contract")
    public void getList(Model model, @Valid @ModelAttribute("pageRequestDTO") PageRequestDTO<ContractDTO> pageRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            model.addAttribute("errorMessages", bindingResult.getAllErrors());
        else {
            PageResponseDTO<ContractDTO> pageResponseDTO = contractService.getListAll(pageRequestDTO);
            model.addAttribute("responseDTO", pageResponseDTO);
        }
    }

    @GetMapping("/register")
    public void registerForm(@RequestParam("id")Integer id, Model model){
        WarehouseCostDTO warehouseCostDTO = contractService.get(id);
        model.addAttribute("warehouseCostDTO", warehouseCostDTO);
    }

    @PostMapping("/contract")
    public String register(@RequestParam("name")String name, @RequestParam("id") String id, @ModelAttribute ContractDTO contractDTO, Model model){
        int warehouseId = Integer.parseInt(id);
        if(name == null)
            model.addAttribute("error","이름을 입력하세요.");
        else {
            Integer memberId = contractService.check(name);
            MemberDTO memberDTO = authService.getOne(memberId);
            WarehouseDTO warehouseDTO = new WarehouseDTO();
            Integer getId = contractService.getUserId(name);
            memberDTO.setId(getId);
            warehouseDTO.setId(warehouseId);
            contractDTO.setMemberDTO(memberDTO);
            contractDTO.setWarehouseDTO(warehouseDTO);
            boolean result = contractService.save(contractDTO);
            if (result)
                model.addAttribute("clear", "완료");
            else
                model.addAttribute("fail", "실패");
        }

        return "contract/contract";
    }

    @PostMapping("/approve")
    @ResponseBody
    public ResponseEntity<Map<String, String>> approveWarehouseContracts(@RequestBody List<Integer> items){
        boolean complete = contractService.modifyApprovalList(items);

        Map<String, String> response = new HashMap<>();

        if (complete) {
            response.put("message", "승인이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "승인 처리에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/reject")
    @ResponseBody
    public ResponseEntity<Map<String, String>> rejectWarehouseContracts(@RequestBody List<Integer> items){
        boolean complete = contractService.modifyRejectList(items);

        Map<String, String> response = new HashMap<>();

        if (complete) {
            response.put("message", "거절이 완료되었습니다.");
            return ResponseEntity.ok(response);
        }
        else {
            response.put("message", "거절 처리에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
