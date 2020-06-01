package ru.filippov.neat.parser.excel;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelParser {

    final int ALLOWED_COUNT_OF_EMPTY_ROWS = 2;

    public ExcelFile parseFile(MultipartFile file) throws IOException {

        Workbook workBook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workBook.getSheetAt(0);
        List< List<Object> >  data = new ArrayList<>(50);
        Iterator<Row> rowIterator = sheet.iterator();
        Cell cell;
        Row row;
        int emptyRowCounter = 0;
        int i = 0;
        List<String> headers = new ArrayList<>(10);
        List<Object> legend = new ArrayList<>(50);
        String legendHeader = "";
        int j = 0;
        while (rowIterator.hasNext()){
            if(emptyRowCounter >= ALLOWED_COUNT_OF_EMPTY_ROWS) {
                break;
            }
            row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            List<Object>  rowData = new ArrayList<>(10);
            int emptyCells = 0;

            while(cellIterator.hasNext()){
                cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case STRING:
                    case NUMERIC:
                    case FORMULA:
                        if( i == 0 ) {
                            if(j == 0){
                                legendHeader = cell.getStringCellValue().trim();
                            } else {
                                headers.add(cell.getStringCellValue().trim());
                            }
                        } else {
                            if(j == 0) {
                                legend.add(cell.getStringCellValue().trim());
                            } else {
                                rowData.add(cell.getStringCellValue().trim());
                            }
                        }
                        break;
                    default:
                        ++emptyCells;
                        rowData.add("");
                        break;
                }
            }

            if(rowData.size() != emptyCells){
                data.add(rowData);
            } else {
                emptyRowCounter++;
            }

            i++;

        }

        return new ExcelFile(headers, data, legend, legendHeader);



    }
}
