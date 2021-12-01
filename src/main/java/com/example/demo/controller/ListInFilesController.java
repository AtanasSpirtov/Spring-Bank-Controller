package com.example.demo.controller;

import com.example.demo.fileConverter.ExcelConverter;
import com.example.demo.fileConverter.PdfConverter;
import com.example.demo.fileConverter._BaseConverter;
import com.itextpdf.text.DocumentException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Controller
@RequestMapping("/ViewInFile")
public class ListInFilesController extends _BaseController {

    @GetMapping(value = "/ExcelReport", produces = "application/zip")
    public void excelReport(HttpServletResponse response, boolean isZipped) throws IOException, DocumentException {
        if (isZipped) {
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            ZipEntry entry = new ZipEntry("transactions.xlsx");
            zipOutputStream.putNextEntry(entry);
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=transactions.zip");
            ((_BaseConverter) new ExcelConverter(transactionService.listAllTransactions(1L)))
                    .export(zipOutputStream);
            zipOutputStream.close();
        } else {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=transactions.xlsx");
            ((_BaseConverter) new ExcelConverter(transactionService.listAllTransactions(1L)))
                    .export(response.getOutputStream());
        }
    }

    @GetMapping(value = "/PdfReport")
    @PreAuthorize("hasAuthority('admin')")
    public void pdfReport(HttpServletResponse response, boolean isZipped) throws IOException, DocumentException {
        if(isZipped)
        {
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            ZipEntry entry = new ZipEntry("transactions.pdf");
            zipOutputStream.putNextEntry(entry);
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=transactions.zip");
            ((_BaseConverter) new PdfConverter(transactionService.listAllTransactions(1L)))
                    .export(zipOutputStream);
            zipOutputStream.close();
        }
        else {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");

            ((_BaseConverter) new PdfConverter(transactionService.listAllTransactions(1L))).export(response.getOutputStream());
        }
    }

}