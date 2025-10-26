package com.top.effitopia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.top.effitopia.dto.*;
import com.top.effitopia.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/warehouses")
@RequiredArgsConstructor
@Log4j2
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/register")
    public void registerForm(Model model) {
        log.info("GET register");
        List<WarehouseTypeDTO> warehouseTypeDTO = warehouseService.getTypeList();
        List<MemberDTO> warehouseDTOList = warehouseService.getAssignableWarehouseManagerList();
        model.addAttribute("warehouseType",warehouseTypeDTO);
        model.addAttribute("member",warehouseDTOList);
    }

    @GetMapping("/action")
    @ResponseBody
    public ResponseEntity<Map<String, String>> duplicateName(@RequestParam("warehouseName") String warehouseName) {
        Map<String, String> response = new HashMap<>();

        Optional<String> name = warehouseService.get(warehouseName);
        if (name.isPresent())
            response.put("message", "중복된 창고 이름입니다.");
        else
            response.put("message", "사용 가능한 창고 이름입니다.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody WarehouseDTO warehouseDTO, BindingResult bindingResult){
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("message", "등록에 실패했습니다. 오류가 있습니다.");
            return ResponseEntity.badRequest().body(response);
        }
        else{
            boolean result = warehouseService.save(warehouseDTO);

            if(result) {
                response.put("success", true);
                response.put("message", "창고 등록 성공");
                return ResponseEntity.ok(response);
            }
        }

        response.put("success", false);
        response.put("message", "등록에 실패했습니다. 오류가 있습니다.");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/list")
    public void getList(Model model, @Valid @ModelAttribute("pageRequestDTO") PageRequestDTO<WarehouseDTO> pageRequestDTO, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()){
            model.addAttribute("errorMessages", bindingResult.getAllErrors());
            log.info("Errors: " + bindingResult.getAllErrors());
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

    @GetMapping("/cell")
    public void get(@RequestParam("id")Integer id, @ModelAttribute PageRequestDTO pageRequestDTO, Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            model.addAttribute("errorMessages", bindingResult.getAllErrors());
        else {
            pageRequestDTO.setSearchCond(id);
            PageResponseDTO<StockDTO> pageResponseDTO = warehouseService.getStockList(pageRequestDTO);

            model.addAttribute("id", id);
            model.addAttribute("responseDTO", pageResponseDTO);
        }
    }
}
