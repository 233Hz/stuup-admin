package com.poho.stuup.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfUtil {
    public static void main(String[] args) throws Exception {
        createPDF("D:\\stuup\\doc", "pic/111.png", "pic/222.png");
    }

    /**
     * 分割路径
     */
    public static String[] separatePath(String path) {
        if (path == null || "".equals(path.trim())) {
            return null;
        }
        String[] sep = path.split("\\.");
        return new String[]{sep[0], sep[1]};
    }

    /**
     * @param inputFile
     */
    public static void imageWaterMark(String baseDoc, String inputFile) {
        try {
            String[] spe = separatePath(inputFile);
            String outputFile = spe[0] + "_1." + spe[1];
            PdfReader reader = new PdfReader(inputFile);
            File file = new File(outputFile);
            if (file.length() == 0) {
                file.delete();
            }
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
            int total = reader.getNumberOfPages() + 1;
            Image image = Image.getInstance(baseDoc + File.separator + ProjectConstants.PROJECT_COMMON + File.separator + "water_pic.png");
            image.setAbsolutePosition(0, 0);
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.2f);
            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                under = stamper.getUnderContent(i);
                under.beginText();
                under.addImage(image);
                under.setGState(gs);
            }
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成用户报名身份证打印文档
     *
     * @param baseDoc
     * @param idCardFront
     * @param idCardBack
     * @return
     */
    public static String createPDF(String baseDoc, String idCardFront, String idCardBack) {
        String pdf = "";
        try {
            String pdfPath = baseDoc + File.separator + ProjectConstants.PROJECT_PDF;
            MicrovanUtil.createFolder(pdfPath);
            String pdfName = MicrovanUtil.generateShortUuid();
            //输出路径
            String outPath = pdfPath + File.separator + pdfName + ".pdf";
            //设置纸张
            Rectangle rect = new Rectangle(PageSize.A4);
            //创建文档实例
            Document document = new Document(rect);
            //创建输出流
            PdfWriter.getInstance(document, new FileOutputStream(new File(outPath)));
            document.open();
            document.newPage();

            //空行
            Paragraph blankRow = new Paragraph(80, " ");
            document.add(blankRow);

            Image image = Image.getInstance(baseDoc + File.separator + idCardFront);
            image.scaleToFit(300, 400);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);

            blankRow = new Paragraph(40, " ");
            document.add(blankRow);

            image = Image.getInstance(baseDoc + File.separator + idCardBack);
            image.scaleToFit(300, 400);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            document.close();
            imageWaterMark(baseDoc, outPath);
            pdf = ProjectConstants.PROJECT_PDF + "/" + pdfName + "_1.pdf";
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdf;
    }

    /**
     * @param baseDoc
     * @param idCardFront
     * @param idCardBack
     * @param idCardPdf
     * @return
     */
    public static String createPDFAgain(String baseDoc, String idCardFront, String idCardBack, String idCardPdf) {
        String pdfName = "";
        if (MicrovanUtil.isNotEmpty(idCardPdf)) {
            String pdfPath = baseDoc + File.separator;
            File file = new File(pdfPath + idCardPdf);
            if (file.length() <= 0) {
                String pdfTemp = idCardPdf.split("_")[0];
                String outPath = pdfPath + pdfTemp + ".pdf";
                imageWaterMark(baseDoc, outPath);
                pdfName = idCardPdf;
            }
        } else {
            pdfName = createPDF(baseDoc, idCardFront, idCardBack);
        }
        return pdfName;
    }
}