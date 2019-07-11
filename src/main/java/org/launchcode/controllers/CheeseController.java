package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    CheeseDao cheeseDao;

    @Autowired
    CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors,
                                       @RequestParam int categoryId,
                                       Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }

    @RequestMapping(value = "category", method = RequestMethod.GET)
    public String categories(Model model, @RequestParam int id){

        Category cat = categoryDao.findOne(id);
        List<Cheese> cheeses = cat.getCheeses();
        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title",
                "Cheeses in category: " + cat.getName());
        return "cheese/index";
    }

    //Bonus mission 1

    //Displaying  all the cheeses of particular category.

    //This particular handler development taught me how to get a variable
    //from browser to web server.

    @RequestMapping(value = "category/{categoryId}", method = RequestMethod.GET)
    public String categoryCheeses(@PathVariable int categoryId,
                           Model model){

        Iterable<Cheese> listOfCheeses = cheeseDao.findAll();
        List<Cheese> theCheeses = new ArrayList<>();

        for(Cheese cheese : listOfCheeses ){

            if(cheese.getCategory().getId() == categoryId){
                theCheeses.add(cheese);
            }

        }

        model.addAttribute("title",
                "All the cheeses of particular category");
        model.addAttribute("cheeses", theCheeses);
        return "cheese/index";
    }

    //Bonus mission 2
    //Building the functionality to edit the added cheese and description.

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model,
                                  @PathVariable int cheeseId){
        model.addAttribute("title", "Edit Cheese ");
        model.addAttribute("cheese", cheeseDao.findOne(cheeseId));
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/edit";

    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.POST)
    public String processEditForm(@ModelAttribute  @Valid Cheese newCheese,
                                  Errors errors,
                                  @PathVariable int cheeseId,
                                  @RequestParam int categoryId,
                                  Model model){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/edit";
        }

        Cheese theCheese = cheeseDao.findOne(cheeseId);
        theCheese.setName(newCheese.getName());
        theCheese.setDescription(newCheese.getDescription());

        Category theCategory = categoryDao.findOne(categoryId);
        theCheese.setCategory(theCategory);
        cheeseDao.save(theCheese);

        return "redirect:http://localhost:8081/cheese";

    }

}
