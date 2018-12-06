package br.lhardt.bov;

import android.content.Context;
import android.widget.Toast;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class InformationExporter {
//	/** The font used in the title */
//	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//	/** The font used in the subtitle */
//	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
//	/** The font used in the subtitle */
//	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.RED);
//	/* Text font */
//	private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
//	/** Bold font */
//	private static Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
//
//	private void addMetadata(Document doc) {
//		doc.addTitle("Relatório SPVG");
//	}
//
    private static final char LINE_FEED = '\n';
	private static String addFileHeader( String doc ){
		doc += "%PDF-1.3" + LINE_FEED;
		doc += "<<" + LINE_FEED + "/Title (Relatório)" + LINE_FEED + "/Creator (Gerado automaticamente pelo programa)" + LINE_FEED;
		doc += ">>" + LINE_FEED;
		return doc;
	}
//
//
	public static boolean exportToPdf(List<Cow> allCows, String filename, Context context ) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, context.openFileOutput(filename, context.MODE_PRIVATE));
            document.open();
            document.add(new Paragraph("Hello, World!"));
//            for(int i = 0; i < allCows.size(); i++){
//                document.add(new Paragraph(allCows.get(i).toString()));
//            }
        } catch( DocumentException exc ) {
	        document.close();
	        return false;
        } catch( FileNotFoundException exc ){
            Toast.makeText(context, "WTF didn read file", Toast.LENGTH_LONG);
        }
        document.close();
        return true;
	}
}
