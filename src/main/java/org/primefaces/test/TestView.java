package org.primefaces.test;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.org.apache.bcel.internal.classfile.Code;

import java.io.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(name = "testView")
@ViewScoped
public class TestView implements Serializable {

    private String testString;

    @PostConstruct
    public void init() {
        testString = "Welcome to PrimeFaces!!!";
    }

    public void imprimer() {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //	... Code to generate InputStream from PDF File from String ...
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk("Hello World", font);
            document.add(chunk);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        InputStream in = new ByteArrayInputStream(out.toByteArray());
        directPrint(in, "pdf", "");
    }

    public void directPrint(InputStream inputStream, String typeFichier, String option) {
        //	... Code to print pdf file
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setCharacterEncoding("ISO-8859-1");

            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "inline;filename=directPrint.pdf" + (typeFichier != null ? (";type=" + typeFichier + ";option=" + option) : "") + ";");

            response.setHeader("Cache-Control", "");
            response.setHeader("Pragma", "");

            DataOutputStream os = new DataOutputStream(response.getOutputStream());
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
            context.responseComplete();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
