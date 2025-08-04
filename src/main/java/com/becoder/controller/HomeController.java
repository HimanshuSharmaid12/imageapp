
package com.becoder.controller;

import com.becoder.model.Images;

import com.becoder.repository.uploadRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private uploadRepository uploadRepo;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/")
    public String homeRedirect() {
        return "image"; // image.html
    }

    @PostMapping("/imageUpload")
    public String imageUpload(@RequestParam MultipartFile img, HttpSession session) {
        Images im = new Images();
        try {
            Map uploadResult = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            im.setImageName(imageUrl);
            uploadRepo.save(im);
            session.setAttribute("msg", "✅ Image uploaded successfully to Cloudinary");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "❌ Image upload failed");
        }
        return "redirect:/";
    }

    @GetMapping("/view")
    public String viewImages(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 6;
        Page<Images> imgPage = uploadRepo.findAll(PageRequest.of(page, pageSize));
        model.addAttribute("list", imgPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", imgPage.getTotalPages());
        return "view"; // view.html
    }
}



























 
 


/*
package com.becoder.controller;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.model.Images;
import com.becoder.repository.uploadRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private uploadRepository uploadRepo;

    // Default URL mapping
    @GetMapping("/")
    public String homeRedirect() {
    	
        return "image"; // image.html as home page
    }

    // Handle image upload
    @PostMapping("/imageUpload")
    public String imageUpload(@RequestParam MultipartFile img, HttpSession session) {
        Images im = new Images();
        im.setImageName(img.getOriginalFilename());

        Images uploadedImg = uploadRepo.save(im);

        if (uploadedImg != null) {
            try {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
               session.setAttribute("msg", "✅ Image uploaded successfully");
                
        } 
        
        return "redirect:/";
    }

   /* Show all uploaded images
    @GetMapping("/view")
    public String viewImages(Model m) {
        List<Images> list = uploadRepo.findAll();
        m.addAttribute("list", list);
        return "view"; // view.html
    }*//*
    



    @GetMapping("/view")
    public String viewImages(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 6; // change this as needed
        Page<Images> imgPage = uploadRepo.findAll(PageRequest.of(page, pageSize));
        model.addAttribute("list", imgPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", imgPage.getTotalPages());
        return "view";
    }

  
}
*/
