package com.softwarelma.epe.p3.office;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public class EpeOfficeFinalOffice_write_xlsx extends EpeOfficeAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "office_write_xlsx, expected the file name (required), the sheet names (list, required) "
                + "and the sheet's contents (list of list of str, required), in that order.";
        String fileName = getStringAt(listExecResult, 0, postMessage);
        List<String> listSheet = getListStringAt(listExecResult, 1, postMessage);
        List<List<List<String>>> listListListStr = new ArrayList<>();
        for (int i = 2; i < listExecResult.size(); i++)
            listListListStr.add(getListListStringAt(listExecResult, i, postMessage));
        retrieveAndSaveWorkbook(fileName, listSheet, listListListStr);
        File f = new File(fileName);
        if (!f.exists() || !f.isFile())
            throw new EpeAppException("The file does not exist or is it not a file: " + fileName);
        log(execParams, "created: " + fileName);
        return createResult("true");
    }

    public static void retrieveAndSaveWorkbook(String fileName, List<String> listSheet,
            List<List<List<String>>> listListListStr) throws EpeAppException {
        Workbook workbook = retrieveWorkbook(listSheet, listListListStr);
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        String step = "workbook.write";

        try {
            workbook.write(fileOut);
            step = "fileOut.close";
            fileOut.close();
            step = "fileOut.toByteArray";
            byte[] arrayByte = fileOut.toByteArray();
            step = "arrayByte to file";
            new EpeEncodings().write(arrayByte, fileName);
        } catch (IOException e) {
            throw new EpeAppException("On step: " + step, e);
        }
    }

    public static Workbook retrieveWorkbook(List<String> listSheet, List<List<List<String>>> listListListStr)
            throws EpeAppException {
        EpeAppUtils.checkEquals("listSheet.size", "listListListStr.size", listSheet.size(), listListListStr.size());
        Map<String, List<List<String>>> mapSheetAndListListCell = new HashMap<>();
        for (int i = 0; i < listSheet.size(); i++)
            mapSheetAndListListCell.put(listSheet.get(i), listListListStr.get(i));
        return retrieveWorkbook(mapSheetAndListListCell);
    }

    public static Workbook retrieveWorkbook(Map<String, List<List<String>>> mapSheetAndListListCell)
            throws EpeAppException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet;
        Row row;
        Cell cell;
        List<List<String>> listListCell;
        List<String> listCell;
        String cellValue;
        Font fontBold = workbook.createFont();
        Font fontRiga = workbook.createFont();
        fontBold.setBold(true);
        fontRiga.setBold(false);
        CellStyle stileCellaTestata = retrieveCellStyle(workbook, BorderStyle.THIN, IndexedColors.GREY_25_PERCENT,
                HorizontalAlignment.CENTER, fontBold, true, null);
        CellStyle stileCellaRighe = retrieveCellStyle(workbook, BorderStyle.THIN, IndexedColors.WHITE,
                HorizontalAlignment.LEFT, fontRiga, true, null);
        int width;

        for (String sheetName : mapSheetAndListListCell.keySet()) {
            listListCell = mapSheetAndListListCell.get(sheetName);
            sheet = workbook.createSheet(sheetName);

            for (int rowInd = 0; rowInd < listListCell.size(); rowInd++) {
                listCell = listListCell.get(rowInd);
                row = sheet.createRow(rowInd);

                for (int colInd = 0; colInd < listCell.size(); colInd++) {
                    cellValue = listCell.get(colInd);
                    cell = row.createCell(colInd);
                    cell.setCellValue(cellValue);

                    if (rowInd == 0) {
                        cell.setCellStyle(stileCellaTestata);
                        width = cellValue.length();
                        width *= width < 10 ? 550 : (width < 20 ? 450 : 350);
                        sheet.setColumnWidth(colInd, width);
                    } else {
                        cell.setCellStyle(stileCellaRighe);
                    }
                }
            }
        }

        return workbook;
    }

    public static CellStyle retrieveCellStyle(Workbook wb, BorderStyle border, IndexedColors backgruond,
            HorizontalAlignment alignment, Font font, boolean wrapText, Short currencyFormat) throws EpeAppException {
        CellStyle stile = wb.createCellStyle();
        stile.setBorderBottom(border);
        stile.setBorderLeft(border);
        stile.setBorderRight(border);
        stile.setBorderTop(border);
        stile.setFillForegroundColor(backgruond.index);
        stile.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stile.setAlignment(alignment);
        stile.setWrapText(wrapText);
        if (currencyFormat != null)
            stile.setDataFormat(currencyFormat);
        stile.setFont(font);
        return stile;
    }

}
