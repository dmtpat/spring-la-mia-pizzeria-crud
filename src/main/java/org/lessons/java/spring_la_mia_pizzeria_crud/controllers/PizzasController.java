package org.lessons.java.spring_la_mia_pizzeria_crud.controllers;

import java.util.List;

import org.lessons.java.spring_la_mia_pizzeria_crud.model.Pizza;
import org.lessons.java.spring_la_mia_pizzeria_crud.repo.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/pizza")
public class PizzasController {
    
    @Autowired
    private PizzaRepository repository;

    @GetMapping
    public String index(@RequestParam(value = "valoreRicerca", required= false) String valore,  Model model) {
        List<Pizza> pizzas = repository.findAll();

        if (valore != null) {
            pizzas = repository.findByNameContainingIgnoreCase(valore);
        }
        model.addAttribute("pizzas", pizzas);
        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        Pizza selected = null;
        if (repository.findById(id).isPresent()) {
            selected = repository.findById(id).get();
        }
        model.addAttribute("selected", selected);
        
        return "detail";
    }
    
    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("pizza", new Pizza());

        return "create";
    }
    
    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("pizza")Pizza pizza,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "/create";
        }
        repository.save(pizza);
        return "redirect:/pizza";
    }
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("pizza", repository.findById(id).get());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
        @Valid @ModelAttribute("pizza") Pizza pizza,
                BindingResult bindingResult,
                        Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        repository.save(pizza);

        return "redirect:/pizza";
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        repository.deleteById(id);
        return "redirect:/pizza";
    }

}
