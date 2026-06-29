//ingredientsController
package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.madaporc.dto.IngredientDTO;
import com.madaporc.service.IngredientService;

@Controller
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

   @GetMapping("/ingredients")
   public String listIngredients(Model model) {
       model.addAttribute("ingredients", ingredientService.findAllIngredients());
       return "stocks/ingredients";
   }
    
    @GetMapping("/ingredients/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            IngredientDTO dto = ingredientService.getForm(id);
            model.addAttribute("ingredient", dto);
        } else {
            model.addAttribute("ingredient", new IngredientDTO());
        }
        return "stocks/formIngredient";
    }

    @PostMapping("/ingredients/save")
    public String save(@ModelAttribute IngredientDTO dto, Model model, RedirectAttributes redirectAttributes) {
        String message;
        if (dto.getId() != null) {
            message = ingredientService.modifierIngredient(dto.getId(), dto);
        } else {
            message = ingredientService.creerIngredient(dto);
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/ingredients";
    }
}