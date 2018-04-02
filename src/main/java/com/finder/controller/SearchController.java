package com.finder.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finder.model.FinderInfo;
import com.finder.service.*;

@Controller
public class SearchController {
	@Autowired
	private FinderService finderService;

	@RequestMapping("/keywordSearch")
    public String kwSearch(@ModelAttribute("finderInfo")FinderInfo finderInfo){
        return "keywordSearch";
    }
	
	@GetMapping("/finderInfo/kwResult")
	public String search(@ModelAttribute("finderInfo")FinderInfo finderInfo, Model model) {
		if (finderInfo.getArea() ==""||finderInfo.getRecepti()==""||finderInfo.getDate()=="") {
			return "redirect:/finderInfo";
		}
        System.out.println(finderInfo.getArea());
        System.out.println(finderInfo.getRecepti());
        System.out.println(finderInfo.getDate());
		model.addAttribute("finderInfos", finderService.kwSearch(finderInfo.getArea(), finderInfo.getRecepti(), finderInfo.getDate()));
		return "list";
	}
	
	@RequestMapping("/idSearch")
    public String idSearch(@ModelAttribute("finderInfo")FinderInfo finderInfo){
        return "idSearch";
    }
	
	@GetMapping("/finderInfo/search")
	public String search(@RequestParam("s") String s, Model model) {
		if (s=="") {
			return "redirect:/finderInfo";
		}

		model.addAttribute("finderInfos", finderService.search(Integer.parseInt(s)));
		return "list";
	}
}

