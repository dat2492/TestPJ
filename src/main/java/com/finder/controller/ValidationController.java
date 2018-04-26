//package com.finder.controller;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.finder.model.*;
//import com.finder.service.*;
//
//@Controller
//public class ValidationController {
//	@RequestMapping(value = "/data/create", method = RequestMethod.POST)	
//	public ModelAndView createNewData(@Valid FinderInfo finderInfo, BindingResult bindingResult) {
//		ModelAndView modelAndView = new ModelAndView();
//		FinderInfo receptiExists = validationService.findReceptiByRecepti(finderInfo.getRecepti());
//		if (receptiExists != null) {
//			bindingResult.rejectValue("This recepti has already used", null);
//		}
//		if (bindingResult.hasErrors()) {
//			modelAndView.setViewName("form");
//		} 
//	}
//	
//}
