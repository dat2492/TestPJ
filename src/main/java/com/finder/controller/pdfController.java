package com.finder.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.css.media.MediaDeviceDescription;
import com.itextpdf.html2pdf.css.media.MediaType;
import com.itextpdf.io.IOException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

@Controller
public class pdfController {
	/** The target folder for the result. */
	public static final String ADDRESS = "https://stackoverflow.com/help/on-topic";
	/** The target folder for the result. */
	public static final String TARGET = "/java/";
	/** The path to the resulting PDF file. */
	public static final String DEST = String.format("%surl2pdf_2.pdf", TARGET);
	@PostMapping("/pdfExport")
    public String home() throws IOException, java.io.IOException{
		String inputFile = "src/main/resources/templates/certification.html";
	    String outputFile = "/java/TestPdf.pdf";

	    generatePDF(inputFile, outputFile);
        return "index";
    }
	public static void generatePDF(String inputHtmlPath, String outputPdfPath)
	{
	    try {
	        String url = new File(inputHtmlPath).toURI().toURL().toString();
	        System.out.println("URL: " + url);

	        OutputStream out = new FileOutputStream(outputPdfPath);

	        //Flying Saucer part
	        ITextRenderer renderer = new ITextRenderer();

	        renderer.setDocument(url);
	        renderer.layout();
	        renderer.createPDF(out);

	        out.close();
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
