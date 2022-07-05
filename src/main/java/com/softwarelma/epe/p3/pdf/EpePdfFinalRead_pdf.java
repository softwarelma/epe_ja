package com.softwarelma.epe.p3.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public class EpePdfFinalRead_pdf extends EpePdfAbstract {

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "read_pdf, expected the file name (required).";
		String fileName = getStringAt(listExecResult, 0, postMessage);
		String fileContent = readPdf(fileName);
//		String fileContent = readPdfTable(fileName);
		return createResult(fileContent);
	}

	public static String readPdf(String fileName) throws EpeAppException {
		String fileContent = null;
		try {
			PDDocument document = PDDocument.load(new File(fileName));
			if (!document.isEncrypted()) {
				PDFTextStripper stripper = new PDFTextStripper();
				fileContent = stripper.getText(document);
				readPdfTable(document);
			}
			document.close();
		} catch (IOException e) {
			throw new EpeAppException("readPdf fileName: " + fileName, e);
		}
		return fileContent;
	}

	public static void readPdfTable(PDDocument document) throws EpeAppException {
	}

	public static String readPdfTable(String fileName) throws EpeAppException {
		List<String[]> paraFromPDF = readParaFromPDF(fileName, 0, 1, 8);
		// List<String[]> paraFromPDF = readParaFromPDF(fileName, 0, 1, 4);
		return "...";
	}

	public static List<String[]> readParaFromPDF(String fileName, int pageNoStart, int pageNoEnd, int noOfColumnsInTable) throws EpeAppException {
		ArrayList<String[]> objArrayList = new ArrayList<>();
		try {
			PDDocument document = PDDocument.load(new File(fileName));
			document.getClass();
			if (!document.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);
				PDFTextStripper tStripper = new PDFTextStripper();
				tStripper.setStartPage(pageNoStart);
				tStripper.setEndPage(pageNoEnd);
				String pdfFileInText = tStripper.getText(document);
				System.out.println(pdfFileInText);
				// split by whitespace
				String Documentlines[] = pdfFileInText.split("\\r?\\n");
				for (String line : Documentlines) {
					String lineArr[] = line.split("\\s+");
					if (lineArr.length == noOfColumnsInTable) {
						for (String linedata : lineArr) {
//							System.out.print(linedata + "             ");
						}
//						System.out.println("");
						objArrayList.add(lineArr);
					}
				}
			}
		} catch (Exception e) {
			throw new EpeAppException("readParaFromPDF fileName: " + fileName, e);
		}
		return objArrayList;
	}

}
