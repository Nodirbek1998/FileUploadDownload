package uz.cas.fileuploaddownload.controller;


import antlr.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.cas.fileuploaddownload.entity.Document;
import uz.cas.fileuploaddownload.repository.DocumentRepository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class AppController {

    @Autowired
    private DocumentRepository documentRepository;
    @GetMapping("/")
    public String viewHomePage(Model model){
        List<Document> documents = documentRepository.findAllBy();
        model.addAttribute("documents", documents);
        return "home";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document")MultipartFile multipartFile, RedirectAttributes ra) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        Document document = new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUploadTime(new Date());
        documentRepository.save(document);
        ra.addFlashAttribute("message", "The file has been uploaded successfully.");
        return "redirect:/";
    }

    @GetMapping("/download")
    public void downloadFile(@Param("id") Long id, HttpServletResponse response) throws Exception {
        Optional<Document> byId = documentRepository.findById(id);
        if (!byId.isPresent()){
            throw new Exception("Cloud not find document with ID: " + id);
        }
        Document document = byId.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+document.getName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(document.getContent());
        servletOutputStream.close();
    }
}
