package edu.java.lab2.reports;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;

/*
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class SavePDF {

    SavePDF (DefaultTableModel tableModel) {

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        PdfPTable t = new PdfPTable(4);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("./Data/Library/PdfDataLibraryf.pdf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        BaseFont bfComic = null;
        try {
            bfComic = BaseFont.createFont("/Windows/Fonts/Arial.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Font font1 = new Font(bfComic, 12);

        t.addCell(new PdfPCell(new Phrase("Автор",font1)));
        t.addCell(new PdfPCell(new Phrase("Книга",font1)));
        t.addCell(new PdfPCell(new Phrase("Год издания",font1)));
        t.addCell(new PdfPCell(new Phrase("Наличие",font1)));


        for(int i = 0; i < tableModel.getRowCount(); i++){
            t.addCell(new Phrase((String) tableModel.getValueAt(i,0),font1));
            t.addCell(new Phrase((String) tableModel.getValueAt(i,1),font1));
            t.addCell(new Phrase((String) tableModel.getValueAt(i,2),font1));
            t.addCell(new Phrase((String) tableModel.getValueAt(i,3),font1));
        }

        document.open();

        try {
            document.add(t);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();
    }

}
*/