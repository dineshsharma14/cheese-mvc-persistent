package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    MenuDao menuDao; //As we need to access these objects from db.

    @Autowired
    CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model){
       model.addAttribute("menus", menuDao.findAll());
       model.addAttribute("title", "Cheese Menus");
       return "menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu newMenu,
                      Errors errors, Model model){

        if(errors.hasErrors()){
            model.addAttribute("title", "Add Menu");
            //model.addAttribute(new Menu());
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();

    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable int menuId, Model model){

        Menu newMenu = menuDao.findOne(menuId);
        //I could have sent the whole object and got everything in th view
        model.addAttribute("title", newMenu.getName());
        model.addAttribute("cheeses", newMenu.getCheeses());
        model.addAttribute("menuId", newMenu.getId());
        return "menu/view";

    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(@PathVariable int menuId, Model model){

        Menu newMenu = menuDao.findOne(menuId);
        Iterable<Cheese> listOfCheeses = cheeseDao.findAll();

        AddMenuItemForm form = new AddMenuItemForm(newMenu, listOfCheeses);

        model.addAttribute("title", "Add item to menu " +
                newMenu.getName());
        model.addAttribute("form", form);
        return "menu/add-item";

    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(Model model,
                          @ModelAttribute @Valid AddMenuItemForm form,
                          Errors errors
                          ){

        if(errors.hasErrors()){
            model.addAttribute("form", form);
            return "menu/add-item";
        }

        Cheese theCheese = cheeseDao.findOne(form.getCheeseId());
        Menu theMenu = menuDao.findOne(form.getMenuId());
        theMenu.addItem(theCheese);
        menuDao.save(theMenu);

        return "redirect:/menu/view/" + theMenu.getId();
    }

}
